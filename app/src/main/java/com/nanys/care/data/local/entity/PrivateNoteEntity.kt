package com.nanys.care.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "private_notes",
    indices = [Index("caregiverEmail"), Index("tutorEmail")]
)
data class PrivateNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val caregiverEmail: String,
    val tutorEmail: String,
    val note: String,
    val rating: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
