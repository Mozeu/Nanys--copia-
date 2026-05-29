package com.nanys.care.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookings",
    indices = [
        Index("caregiverEmail"),
        Index("tutorEmail"),
        Index("date"),
        Index("status")
    ]
)
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tutorEmail: String,
    val caregiverEmail: String,
    val date: String,
    val timeSlot: String,
    val durationHours: Int = 1,
    val location: String = "",
    val childId: Long? = null,
    @ColumnInfo(defaultValue = "''")
    val childIds: String = "",
    val additionalNotes: String = "",
    val totalPrice: Double = 0.0,
    val status: String = "pending",
    val colorHex: String = "#5B8DEF",
    val createdAt: Long = System.currentTimeMillis()
)
