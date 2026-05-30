package com.nanys.care.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "caregiver_profiles",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["email"],
            childColumns = ["email"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("city"), Index("state"), Index("hourlyRate")]
)
data class CaregiverProfileEntity(
    @PrimaryKey val email: String,
    val photoUri: String = "default",
    val experienceYears: Int = 0,
    val certifications: String = "",
    val availability: String = "",
    val availabilityStart: String = "",
    val availabilityEnd: String = "",
    val availabilityExceptions: String = "",
    val hourlyRate: Double = 0.0,
    val extraChildRate: Double = 0.0,
    val city: String = "",
    val state: String = "",
    val verified: Boolean = false
)
