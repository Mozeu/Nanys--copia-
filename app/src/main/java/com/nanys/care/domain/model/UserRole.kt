package com.nanys.care.domain.model

enum class UserRole {
    CUIDADOR,
    TUTOR,
    ADMIN,
    SUPERVISOR;

    companion object {
        fun fromString(value: String): UserRole =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: TUTOR
    }
}
