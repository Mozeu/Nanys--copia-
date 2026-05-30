package com.nanys.care.data.repository

import android.content.Context
import com.nanys.care.core.notification.LocalNotificationHelper
import com.nanys.care.core.notification.SimulationService
import com.nanys.care.core.session.SessionManager
import com.nanys.care.core.util.ColorUtil
import com.nanys.care.core.util.HashUtil
import com.nanys.care.core.util.ValidationUtil
import com.nanys.care.data.local.db.NanysDatabase
import com.nanys.care.core.worker.BookingReminderWorker
import com.nanys.care.data.local.entity.BookingEntity
import com.nanys.care.data.local.entity.CaregiverProfileEntity
import com.nanys.care.data.local.entity.TutorProfileEntity
import com.nanys.care.data.local.entity.UserEntity
import com.nanys.care.data.mapper.toDomain
import com.nanys.care.domain.model.BookingStatus
import com.nanys.care.domain.model.User
import com.nanys.care.domain.model.UserRole
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val db: NanysDatabase,
    private val sessionManager: SessionManager
) {
    suspend fun login(email: String, password: String): Result<User> {
        val user = db.userDao().getByEmail(email.trim().lowercase())
            ?: return Result.failure(Exception("Credenciales incorrectas"))
        if (!HashUtil.verify(password, user.passwordHash)) {
            return Result.failure(Exception("Credenciales incorrectas"))
        }
        val domain = user.toDomain()
        sessionManager.saveSession(domain.email, domain.role)
        return Result.success(domain)
    }

    suspend fun register(
        email: String,
        password: String,
        role: UserRole,
        fullName: String
    ): Result<User> {
        val normalizedEmail = email.trim().lowercase()
        if (!ValidationUtil.isValidEmail(normalizedEmail)) {
            return Result.failure(Exception("Correo inválido"))
        }
        if (!ValidationUtil.isValidPassword(password)) {
            return Result.failure(Exception("La contraseña debe tener al menos 4 caracteres"))
        }
        if (db.userDao().exists(normalizedEmail) > 0) {
            return Result.failure(Exception("El correo ya está registrado"))
        }
        if (role == UserRole.ADMIN || role == UserRole.SUPERVISOR) {
            return Result.failure(Exception("Este rol no puede registrarse"))
        }
        val entity = UserEntity(
            email = normalizedEmail,
            passwordHash = HashUtil.hash(password),
            role = role.name,
            fullName = fullName.trim()
        )
        db.userDao().insert(entity)
        when (role) {
            UserRole.CUIDADOR -> db.caregiverProfileDao().insert(CaregiverProfileEntity(email = normalizedEmail))
            UserRole.TUTOR -> db.tutorProfileDao().insert(TutorProfileEntity(email = normalizedEmail))
            else -> Unit
        }
        sessionManager.saveSession(normalizedEmail, role)
        return Result.success(entity.toDomain())
    }

    fun logout() = sessionManager.clearSession()

    fun currentSession() = sessionManager.userEmail to sessionManager.userRole
}

class UserRepository(private val db: NanysDatabase) {
    suspend fun getUser(email: String) = db.userDao().getByEmail(email)?.toDomain()

    suspend fun updateUser(email: String, fullName: String, phone: String) {
        db.userDao().updateBasicInfo(email, fullName, phone)
    }
}

class BookingRepository(
    private val context: Context,
    private val db: NanysDatabase,
    private val simulationService: SimulationService,
    private val notificationHelper: LocalNotificationHelper
) {
    suspend fun createBooking(
        tutorEmail: String,
        caregiverEmail: String,
        date: String,
        startHour: Int,
        startMinute: Int,
        durationHours: Int,
        location: String,
        childIds: List<Long>,
        additionalNotes: String,
        hourlyRate: Double,
        extraChildRate: Double
    ): Long {
        require(childIds.isNotEmpty()) { "Selecciona al menos un hijo/a" }
        val endHour = startHour + durationHours
        val timeSlot = String.format("%02d:%02d-%02d:%02d", startHour, startMinute, endHour, startMinute)
        val extraChildren = (childIds.size - 1).coerceAtLeast(0)
        val total = (hourlyRate + extraChildren * extraChildRate) * durationHours
        val id = db.bookingDao().insert(
            BookingEntity(
                tutorEmail = tutorEmail,
                caregiverEmail = caregiverEmail,
                date = date,
                timeSlot = timeSlot,
                durationHours = durationHours,
                location = location,
                childId = childIds.first(),
                childIds = childIds.joinToString(","),
                additionalNotes = additionalNotes,
                totalPrice = total,
                status = BookingStatus.PENDING.value,
                colorHex = ColorUtil.assignBookingColor(System.currentTimeMillis())
            )
        )
        simulationService.simulateEmail(tutorEmail, "Reserva creada #$id")
        simulationService.simulatePayment(total)
        notificationHelper.showNotification(
            "Nueva solicitud de reserva",
            "${db.userDao().getByEmail(tutorEmail)?.fullName ?: "Un tutor"} solicitó una cita",
            id.toInt()
        )
        simulationService.simulateEmail(caregiverEmail, "Nueva solicitud de reserva")
        return id
    }

    suspend fun respondBooking(bookingId: Long, accept: Boolean): Result<Unit> {
        val booking = db.bookingDao().getById(bookingId)
            ?: return Result.failure(Exception("Reserva no encontrada"))
        if (accept && hasAcceptedOverlap(booking)) {
            return Result.failure(Exception("No puedes aceptar esta cita porque se empalma con otra cita aceptada."))
        }
        val newStatus = if (accept) BookingStatus.ACCEPTED else BookingStatus.REJECTED
        val color = if (accept) ColorUtil.assignBookingColor(bookingId) else booking.colorHex
        db.bookingDao().update(
            booking.copy(status = newStatus.value, colorHex = color)
        )
        val tutorEmail = booking.tutorEmail
        val msg = if (accept) "Tu reserva fue aceptada" else "Tu reserva fue rechazada"
        simulationService.simulateEmail(tutorEmail, msg)
        notificationHelper.showNotification("Actualización de reserva", msg, bookingId.toInt())
        if (accept) {
            BookingReminderWorker.schedule(
                context = context,
                bookingId = bookingId,
                date = booking.date,
                timeSlot = booking.timeSlot,
                tutorEmail = booking.tutorEmail,
                caregiverEmail = booking.caregiverEmail
            )
        }
        return Result.success(Unit)
    }

    private suspend fun hasAcceptedOverlap(booking: BookingEntity): Boolean {
        val requestedRange = parseTimeRange(booking.timeSlot) ?: return true
        val acceptedBookings = db.bookingDao().getAcceptedByCaregiverAndDate(
            caregiverEmail = booking.caregiverEmail,
            date = booking.date,
            excludeId = booking.id
        )
        return acceptedBookings.any { accepted ->
            val acceptedRange = parseTimeRange(accepted.timeSlot) ?: return@any true
            rangesOverlap(requestedRange, acceptedRange)
        }
    }

    private fun parseTimeRange(timeSlot: String): IntRange? {
        val parts = timeSlot.split("-")
        if (parts.size != 2) return null
        val start = parseMinutes(parts[0]) ?: return null
        val end = parseMinutes(parts[1]) ?: return null
        if (end <= start) return null
        return start until end
    }

    private fun parseMinutes(value: String): Int? {
        val parts = value.trim().split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: return null
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: return null
        if (hour < 0 || minute !in 0..59) return null
        return hour * 60 + minute
    }

    private fun rangesOverlap(first: IntRange, second: IntRange): Boolean =
        first.first <= second.last && second.first <= first.last

    suspend fun completeBooking(bookingId: Long) {
        val booking = db.bookingDao().getById(bookingId) ?: return
        db.bookingDao().update(booking.copy(status = BookingStatus.COMPLETED.value))
    }

    suspend fun enrichBooking(booking: BookingEntity) = run {
        val tutor = db.userDao().getByEmail(booking.tutorEmail)
        val caregiver = db.userDao().getByEmail(booking.caregiverEmail)
        val selectedChildIds = booking.childIds.split(",")
            .mapNotNull { it.toLongOrNull() }
            .ifEmpty { booking.childId?.let { listOf(it) } ?: emptyList() }
        val children = selectedChildIds.mapNotNull { db.childDao().getById(it) }
        val tutorProfile = db.tutorProfileDao().getByEmail(booking.tutorEmail)
        val caregiverProfile = db.caregiverProfileDao().getByEmail(booking.caregiverEmail)
        booking.toDomain(
            tutorName = tutor?.fullName ?: "",
            caregiverName = caregiver?.fullName ?: "",
            tutorPhotoUri = tutorProfile?.photoUri ?: "default",
            caregiverPhotoUri = caregiverProfile?.photoUri ?: "default",
            childName = children.joinToString(", ") { it.name },
            childIdsParam = selectedChildIds,
            tutorNotes = tutorProfile?.notes ?: "",
            hourlyRate = caregiverProfile?.hourlyRate ?: 0.0,
            extraChildRate = caregiverProfile?.extraChildRate ?: 0.0
        )
    }
    fun observeByCaregiver(email: String) = db.bookingDao().observeByCaregiver(email)
    fun observeByTutor(email: String) = db.bookingDao().observeByTutor(email)
    fun observePendingForCaregiver(email: String) =
        db.bookingDao().observeByCaregiverAndStatus(email, BookingStatus.PENDING.value)

    fun countPendingForCaregiver(email: String) = db.bookingDao().countPendingForCaregiver(email)
    fun countTodayForUser(email: String) =
        db.bookingDao().countTodayAccepted(email, com.nanys.care.core.util.DateUtil.todayIso())
}
