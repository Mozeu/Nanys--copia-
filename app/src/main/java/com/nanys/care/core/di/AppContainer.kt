package com.nanys.care.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nanys.care.core.notification.LocalNotificationHelper
import com.nanys.care.core.notification.SimulationService
import com.nanys.care.core.session.SessionManager
import com.nanys.care.data.local.db.NanysDatabase
import com.nanys.care.data.local.seed.DatabaseSeeder
import com.nanys.care.data.repository.AuthRepository
import com.nanys.care.data.repository.BookingRepository
import com.nanys.care.data.repository.CaregiverRepository
import com.nanys.care.data.repository.CatalogRepository
import com.nanys.care.data.repository.ChatRepository
import com.nanys.care.data.repository.PrivateNoteRepository
import com.nanys.care.data.repository.ReviewRepository
import com.nanys.care.data.repository.TutorRepository
import com.nanys.care.data.repository.UserRepository

class AppContainer(context: Context) {
    val sessionManager = SessionManager(context)
    val notificationHelper = LocalNotificationHelper(context)
    val simulationService = SimulationService(context)

    private val database: NanysDatabase = Room.databaseBuilder(
        context,
        NanysDatabase::class.java,
        "nanys_care.db"
    )
        .addMigrations(MIGRATION_1_2)
        .addMigrations(MIGRATION_2_3)
        .build()

    val authRepository = AuthRepository(database, sessionManager)
    val userRepository = UserRepository(database)
    val bookingRepository = BookingRepository(context, database, simulationService, notificationHelper)
    val caregiverRepository = CaregiverRepository(database)
    val tutorRepository = TutorRepository(database)
    val chatRepository = ChatRepository(database, simulationService)
    val catalogRepository = CatalogRepository(database)
    val reviewRepository = ReviewRepository(database)
    val privateNoteRepository = PrivateNoteRepository(database)

    val databaseSeeder = DatabaseSeeder(database)

    val db: NanysDatabase get() = database

    private companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE bookings ADD COLUMN childIds TEXT NOT NULL DEFAULT ''")
                db.execSQL("UPDATE bookings SET childIds = CAST(childId AS TEXT) WHERE childId IS NOT NULL")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE caregiver_profiles ADD COLUMN availabilityStart TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE caregiver_profiles ADD COLUMN availabilityEnd TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE caregiver_profiles ADD COLUMN availabilityExceptions TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE caregiver_profiles ADD COLUMN extraChildRate REAL NOT NULL DEFAULT 0.0")
            }
        }
    }
}
