package com.nanys.care.core.session

import android.content.Context
import com.nanys.care.domain.model.UserRole

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_LOGGED_IN, value).apply()

    var userEmail: String?
        get() = prefs.getString(KEY_EMAIL, null)
        set(value) = prefs.edit().putString(KEY_EMAIL, value).apply()

    var userRole: UserRole?
        get() = prefs.getString(KEY_ROLE, null)?.let { UserRole.fromString(it) }
        set(value) = prefs.edit().putString(KEY_ROLE, value?.name).apply()

    var darkThemeEnabled: Boolean
        get() = prefs.getBoolean(KEY_DARK_THEME, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_THEME, value).apply()

    fun saveSession(email: String, role: UserRole) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_EMAIL, email)
            .putString(KEY_ROLE, role.name)
            .apply()
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_LOGGED_IN)
            .remove(KEY_EMAIL)
            .remove(KEY_ROLE)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "nanys_session"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_EMAIL = "email"
        private const val KEY_ROLE = "role"
        private const val KEY_DARK_THEME = "dark_theme"
    }
}
