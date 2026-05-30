package com.nanys.care.data.local.seed

import com.nanys.care.core.util.HashUtil
import com.nanys.care.data.local.db.NanysDatabase
import com.nanys.care.data.local.entity.BookingEntity
import com.nanys.care.data.local.entity.CaregiverProfileEntity
import com.nanys.care.data.local.entity.CatalogItemEntity
import com.nanys.care.data.local.entity.ChildEntity
import com.nanys.care.data.local.entity.MessageEntity
import com.nanys.care.data.local.entity.ReviewEntity
import com.nanys.care.data.local.entity.TutorProfileEntity
import com.nanys.care.data.local.entity.UserEntity
import com.nanys.care.domain.model.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class DatabaseSeeder(private val database: NanysDatabase) {

    suspend fun seedIfEmpty() = withContext(Dispatchers.IO) {
        val userDao = database.userDao()
        if (userDao.getByEmail("admin@nanys.com") != null) return@withContext

        seedCatalogs()
        seedUsers()
        seedProfiles()
        seedChildren()
        seedBookings()
        seedMessages()
        seedReviews()
    }

    private suspend fun seedCatalogs() {
        val dao = database.catalogDao()
        listOf("Básica", "Intermedia", "Avanzada").forEach {
            dao.insert(CatalogItemEntity(category = "experience", name = it, value = it))
        }
        listOf("Primeros auxilios", "RCP", "Educación inicial").forEach {
            dao.insert(CatalogItemEntity(category = "certification", name = it, value = it))
        }
        val locations = listOf(
            "Madrid" to "Comunidad de Madrid",
            "Barcelona" to "Cataluña",
            "Valencia" to "Comunidad Valenciana"
        )
        locations.forEach { (city, state) ->
            dao.insert(CatalogItemEntity(category = "city", name = city, value = city))
            dao.insert(CatalogItemEntity(category = "state", name = state, value = state, extra = city))
        }
    }

    private suspend fun seedUsers() {
        val dao = database.userDao()
        val users = listOf(
            UserEntity("cuidador1@test.com", HashUtil.hash("123"), UserRole.CUIDADOR.name, "Ana García", "600111111"),
            UserEntity("cuidador2@test.com", HashUtil.hash("123"), UserRole.CUIDADOR.name, "Luis Pérez", "600222222"),
            UserEntity("tutor1@test.com", HashUtil.hash("123"), UserRole.TUTOR.name, "Carlos López", "600333333"),
            UserEntity("tutor2@test.com", HashUtil.hash("123"), UserRole.TUTOR.name, "María Ruiz", "600444444"),
            UserEntity("admin@nanys.com", HashUtil.hash("admin123"), UserRole.ADMIN.name, "Admin Nanys"),
            UserEntity("supervisor@nanys.com", HashUtil.hash("super123"), UserRole.SUPERVISOR.name, "Supervisor Nanys")
        )
        users.forEach { dao.insert(it) }
    }

    private suspend fun seedProfiles() {
        database.caregiverProfileDao().insert(
            CaregiverProfileEntity(
                email = "cuidador1@test.com",
                experienceYears = 3,
                certifications = "Primeros auxilios",
                availability = "08:00-18:00",
                availabilityStart = "08:00",
                availabilityEnd = "18:00",
                availabilityExceptions = "No disponible domingos",
                hourlyRate = 150.0,
                extraChildRate = 50.0,
                city = "Madrid",
                state = "Comunidad de Madrid",
                verified = true
            )
        )
        database.caregiverProfileDao().insert(
            CaregiverProfileEntity(
                email = "cuidador2@test.com",
                experienceYears = 5,
                certifications = "Educación inicial",
                availability = "14:00-20:00",
                availabilityStart = "14:00",
                availabilityEnd = "20:00",
                availabilityExceptions = "Sábados solo con reserva anticipada",
                hourlyRate = 200.0,
                extraChildRate = 75.0,
                city = "Barcelona",
                state = "Cataluña",
                verified = false
            )
        )
        database.tutorProfileDao().insert(
            TutorProfileEntity(
                email = "tutor1@test.com",
                city = "Madrid",
                state = "Comunidad de Madrid",
                notes = "Sofía es tranquila; prefiere actividades creativas.",
                preferences = "Cuidador con experiencia en primeros auxilios"
            )
        )
        database.tutorProfileDao().insert(
            TutorProfileEntity(
                email = "tutor2@test.com",
                city = "Barcelona",
                state = "Cataluña",
                notes = "Lucas requiere paciencia; tiene TDAH leve.",
                preferences = "Disponibilidad en tardes"
            )
        )
    }

    private suspend fun seedChildren() {
        database.childDao().insert(
            ChildEntity(tutorEmail = "tutor1@test.com", name = "Sofía", age = 5, specialNeeds = "Ninguna")
        )
        database.childDao().insert(
            ChildEntity(tutorEmail = "tutor2@test.com", name = "Lucas", age = 8, specialNeeds = "TDAH leve")
        )
    }

    private suspend fun seedBookings() {
        val tomorrow = LocalDate.now().plusDays(1).toString()
        val nextWeek = LocalDate.now().plusDays(7).toString()
        database.bookingDao().insert(
            BookingEntity(
                tutorEmail = "tutor1@test.com",
                caregiverEmail = "cuidador1@test.com",
                date = tomorrow,
                timeSlot = "10:00-11:00",
                durationHours = 2,
                location = "Calle Mayor 10, Madrid",
                childId = 1,
                childIds = "1",
                additionalNotes = "Llegar 10 min antes",
                totalPrice = 300.0,
                status = "pending",
                colorHex = "#5B8DEF"
            )
        )
        database.bookingDao().insert(
            BookingEntity(
                tutorEmail = "tutor2@test.com",
                caregiverEmail = "cuidador2@test.com",
                date = nextWeek,
                timeSlot = "16:00-18:00",
                durationHours = 2,
                location = "Av. Diagonal 100, Barcelona",
                childId = 2,
                childIds = "2",
                additionalNotes = "Traer material de arte",
                totalPrice = 400.0,
                status = "accepted",
                colorHex = "#7BC67E"
            )
        )
    }

    private suspend fun seedMessages() {
        val dao = database.messageDao()
        dao.insert(
            MessageEntity(
                senderEmail = "tutor1@test.com",
                receiverEmail = "cuidador1@test.com",
                content = "Hola Ana, ¿estás disponible el sábado por la tarde?",
                timestamp = System.currentTimeMillis() - 86_400_000
            )
        )
        dao.insert(
            MessageEntity(
                senderEmail = "cuidador1@test.com",
                receiverEmail = "tutor1@test.com",
                content = "¡Hola Carlos! Sí, puedo el sábado de 14:00 a 18:00.",
                timestamp = System.currentTimeMillis() - 43_200_000
            )
        )
    }

    private suspend fun seedReviews() {
        database.reviewDao().insert(
            ReviewEntity(
                fromTutorEmail = "tutor2@test.com",
                toCaregiverEmail = "cuidador2@test.com",
                bookingId = 2,
                rating = 4,
                comment = "Muy profesional y puntual.",
                timestamp = System.currentTimeMillis() - 7_200_000
            )
        )
    }
}
