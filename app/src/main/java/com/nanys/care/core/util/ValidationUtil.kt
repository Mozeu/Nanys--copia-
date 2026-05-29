package com.nanys.care.core.util

import android.util.Patterns

object ValidationUtil {
    fun isValidEmail(email: String): Boolean =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

    fun isValidPassword(password: String): Boolean = password.length >= 4
}
