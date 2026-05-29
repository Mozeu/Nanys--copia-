package com.nanys.care.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    indices = [Index("toCaregiverEmail"), Index("bookingId")]
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fromTutorEmail: String,
    val toCaregiverEmail: String,
    val bookingId: Long,
    val rating: Int,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
