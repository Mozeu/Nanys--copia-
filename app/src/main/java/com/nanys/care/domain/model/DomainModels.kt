package com.nanys.care.domain.model

data class User(
    val email: String,
    val fullName: String,
    val phone: String,
    val role: UserRole
)

data class CaregiverProfile(
    val email: String,
    val fullName: String,
    val photoUri: String,
    val experienceYears: Int,
    val certifications: String,
    val availability: String,
    val hourlyRate: Double,
    val city: String,
    val state: String,
    val verified: Boolean,
    val averageRating: Double = 0.0,
    val reviewCount: Int = 0
)

data class TutorProfile(
    val email: String,
    val fullName: String,
    val city: String,
    val state: String,
    val notes: String,
    val preferences: String,
    val children: List<Child> = emptyList()
)

data class Child(
    val id: Long,
    val tutorEmail: String,
    val name: String,
    val age: Int,
    val specialNeeds: String
)

data class Booking(
    val id: Long,
    val tutorEmail: String,
    val caregiverEmail: String,
    val tutorName: String = "",
    val caregiverName: String = "",
    val date: String,
    val timeSlot: String,
    val durationHours: Int,
    val location: String,
    val childId: Long?,
    val childName: String = "",
    val additionalNotes: String,
    val tutorNotes: String = "",
    val totalPrice: Double,
    val status: BookingStatus,
    val colorHex: String
)

data class Message(
    val id: Long,
    val senderEmail: String,
    val receiverEmail: String,
    val content: String,
    val timestamp: Long,
    val flagged: Boolean = false
)

data class ConversationSummary(
    val otherEmail: String,
    val otherName: String,
    val lastMessage: String,
    val lastTimestamp: Long
)

data class CatalogItem(
    val id: Long,
    val category: String,
    val name: String,
    val value: String,
    val extra: String
)

data class Review(
    val id: Long,
    val fromTutorEmail: String,
    val toCaregiverEmail: String,
    val bookingId: Long,
    val rating: Int,
    val comment: String,
    val timestamp: Long
)

data class PrivateNote(
    val id: Long,
    val caregiverEmail: String,
    val tutorEmail: String,
    val tutorName: String,
    val note: String,
    val rating: Int,
    val timestamp: Long
)

data class CaregiverSearchFilter(
    val city: String = "",
    val state: String = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = 1000.0,
    val minExperience: Int = 0,
    val minRating: Float = 0f,
    val availability: String = "",
    val query: String = ""
)
