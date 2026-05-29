package com.nanys.care.core.util

object ColorUtil {
    private val bookingColors = listOf(
        "#5B8DEF", "#7BC67E", "#F59E0B", "#A78BFA", "#EC4899", "#14B8A6"
    )

    /** Asigna un color consistente por id de reserva para el calendario. */
    fun assignBookingColor(bookingId: Long): String {
        return bookingColors[(bookingId % bookingColors.size).toInt()]
    }

    fun parseHex(hex: String): androidx.compose.ui.graphics.Color {
        val clean = hex.removePrefix("#")
        return androidx.compose.ui.graphics.Color(("FF$clean").toLong(16))
    }
}
