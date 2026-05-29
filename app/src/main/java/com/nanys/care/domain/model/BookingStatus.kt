package com.nanys.care.domain.model

enum class BookingStatus(val value: String) {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    COMPLETED("completed");

    companion object {
        fun fromString(value: String): BookingStatus =
            entries.firstOrNull { it.value.equals(value, ignoreCase = true) } ?: PENDING
    }
}
