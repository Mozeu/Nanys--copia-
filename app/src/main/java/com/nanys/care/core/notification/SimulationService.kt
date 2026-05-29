package com.nanys.care.core.notification

import android.content.Context
import android.widget.Toast

/** Centraliza simulaciones de correo y pago para la demo. */
class SimulationService(private val context: Context) {

    fun simulateEmail(to: String, subject: String) {
        Toast.makeText(
            context,
            "📧 Correo simulado a $to: $subject",
            Toast.LENGTH_LONG
        ).show()
    }

    fun simulatePayment(amount: Double) {
        Toast.makeText(
            context,
            "Pago simulado de $${String.format("%.2f", amount)} – Reserva confirmada",
            Toast.LENGTH_LONG
        ).show()
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
