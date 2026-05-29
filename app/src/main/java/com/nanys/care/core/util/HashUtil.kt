package com.nanys.care.core.util

import java.security.MessageDigest

object HashUtil {
    /** Simula cifrado de contraseña con SHA-256 para la demo. */
    fun hash(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun verify(password: String, hash: String): Boolean = hash(password) == hash
}
