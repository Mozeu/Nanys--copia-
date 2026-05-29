package com.nanys.care.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tutor_profiles",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["email"],
            childColumns = ["email"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TutorProfileEntity(
    @PrimaryKey val email: String,
    val city: String = "",
    val state: String = "",
    val notes: String = "",
    val preferences: String = ""
)
