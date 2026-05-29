package com.nanys.care.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val passwordHash: String,
    val role: String,
    val fullName: String,
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
