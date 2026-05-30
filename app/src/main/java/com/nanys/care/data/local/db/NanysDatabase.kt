package com.nanys.care.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nanys.care.data.local.entity.BookingEntity
import com.nanys.care.data.local.entity.CaregiverProfileEntity
import com.nanys.care.data.local.entity.CatalogItemEntity
import com.nanys.care.data.local.entity.ChildEntity
import com.nanys.care.data.local.entity.MessageEntity
import com.nanys.care.data.local.entity.PrivateNoteEntity
import com.nanys.care.data.local.entity.ReviewEntity
import com.nanys.care.data.local.entity.TutorProfileEntity
import com.nanys.care.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CaregiverProfileEntity::class,
        TutorProfileEntity::class,
        ChildEntity::class,
        BookingEntity::class,
        ReviewEntity::class,
        PrivateNoteEntity::class,
        MessageEntity::class,
        CatalogItemEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class NanysDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun caregiverProfileDao(): CaregiverProfileDao
    abstract fun tutorProfileDao(): TutorProfileDao
    abstract fun childDao(): ChildDao
    abstract fun bookingDao(): BookingDao
    abstract fun reviewDao(): ReviewDao
    abstract fun privateNoteDao(): PrivateNoteDao
    abstract fun messageDao(): MessageDao
    abstract fun catalogDao(): CatalogDao
}
