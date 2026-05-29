package com.nanys.care.core.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtil {
    private val isoDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    fun todayIso(): String = LocalDate.now().format(isoDateFormatter)

    fun formatDisplay(timestamp: Long): String {
        val dt = LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        )
        return dt.format(displayFormatter)
    }

    fun formatDate(isoDate: String): String {
        return LocalDate.parse(isoDate).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    fun startOfTodayMillis(): Long {
        return LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun parseBookingDateTime(date: String, timeSlot: String): LocalDateTime? {
        return try {
            val startTime = timeSlot.split("-").firstOrNull()?.trim() ?: return null
            LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(startTime))
        } catch (_: Exception) {
            null
        }
    }

    fun reminderDelayMillis(date: String, timeSlot: String): Long? {
        val bookingDateTime = parseBookingDateTime(date, timeSlot) ?: return null
        val reminderTime = bookingDateTime.minusMinutes(15)
        val now = LocalDateTime.now()
        if (reminderTime.isBefore(now)) return null
        return java.time.Duration.between(now, reminderTime).toMillis()
    }
}
