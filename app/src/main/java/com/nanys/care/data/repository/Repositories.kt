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
        childId: Long?,
        additionalNotes: String,
        hourlyRate: Double
    ): Long {
        val endHour = startHour + durationHours
        val timeSlot = String.format("%02d:%02d-%02d:%02d", startHour, startMinute, endHour, startMinute)
        val total = hourlyRate * durationHours
        val id = db.bookingDao().insert(
            BookingEntity(
                tutorEmail = tutorEmail,
                caregiverEmail = caregiverEmail,
                date = date,
                timeSlot = timeSlot,
                durationHours = durationHours,
                location = location,
                childId = childId,
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

    suspend fun respondBooking(bookingId: Long, accept: Boolean) {
        val booking = db.bookingDao().getById(bookingId) ?: return
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
    }

    suspend fun completeBooking(bookingId: Long) {
        val booking = db.bookingDao().getById(bookingId) ?: return
        db.bookingDao().update(booking.copy(status = BookingStatus.COMPLETED.value))
    }

    suspend fun enrichBooking(booking: BookingEntity) = run {
        val tutor = db.userDao().getByEmail(booking.tutorEmail)
        val caregiver = db.userDao().getByEmail(booking.caregiverEmail)
        val child = booking.childId?.let { db.childDao().getById(it) }
        val tutorProfile = db.tutorProfileDao().getByEmail(booking.tutorEmail)
        booking.toDomain(
            tutorName = tutor?.fullName ?: "",
            caregiverName = caregiver?.fullName ?: "",
            childName = child?.name ?: "",
            tutorNotes = tutorProfile?.notes ?: ""
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
