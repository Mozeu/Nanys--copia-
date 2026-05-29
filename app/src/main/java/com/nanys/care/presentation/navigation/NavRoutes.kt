package com.nanys.care.presentation.navigation

object NavRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val COMPLETE_PROFILE = "complete_profile"

    const val CAREGIVER_DASH = "caregiver_dashboard"
    const val CAREGIVER_REQUESTS = "caregiver_requests"
    const val CAREGIVER_AGENDA = "caregiver_agenda"
    const val CAREGIVER_PROFILE = "caregiver_profile"
    const val CAREGIVER_REGULATIONS = "caregiver_regulations"
    const val CAREGIVER_PRIVATE_NOTES = "caregiver_private_notes"

    const val TUTOR_DASH = "tutor_dashboard"
    const val TUTOR_SEARCH = "tutor_search"
    const val TUTOR_BOOKINGS = "tutor_bookings"
    const val TUTOR_AGENDA = "tutor_agenda"
    const val TUTOR_PROFILE = "tutor_profile"
    const val BOOK_APPOINTMENT = "book_appointment/{caregiverEmail}"
    const val SUBMIT_REVIEW = "submit_review/{bookingId}"

    const val ADMIN_DASH = "admin_dashboard"
    const val ADMIN_CATALOG = "admin_catalog/{category}"

    const val SUPERVISOR_DASH = "supervisor_dashboard"
    const val SUPERVISOR_CHATS = "supervisor_chats"
    const val SUPERVISOR_VERIFICATION = "supervisor_verification"

    const val CAREGIVER_PUBLIC = "caregiver_public/{email}"
    const val TUTOR_PRIVATE = "tutor_private/{email}"
    const val CHAT_LIST = "chat_list"
    const val CHAT_DETAIL = "chat_detail/{otherEmail}"
    const val SETTINGS = "settings"

    fun bookAppointment(email: String) = "book_appointment/$email"
    fun caregiverPublic(email: String) = "caregiver_public/$email"
    fun tutorPrivate(email: String) = "tutor_private/$email"
    fun chatDetail(email: String) = "chat_detail/$email"
    fun adminCatalog(category: String) = "admin_catalog/$category"
    fun submitReview(bookingId: Long) = "submit_review/$bookingId"
}
