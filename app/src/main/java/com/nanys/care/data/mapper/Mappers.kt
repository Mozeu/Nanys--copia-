package com.nanys.care.data.mapper

import com.nanys.care.data.local.entity.*
import com.nanys.care.domain.model.*

fun UserEntity.toDomain() = User(
    email = email,
    fullName = fullName,
    phone = phone,
    role = UserRole.fromString(role)
)

fun CaregiverProfileEntity.toDomain(
    fullName: String,
    averageRating: Double = 0.0,
    reviewCount: Int = 0
) = CaregiverProfile(
    email = email,
    fullName = fullName,
    photoUri = photoUri,
    experienceYears = experienceYears,
    certifications = certifications,
    availability = availability,
    availabilityStart = availabilityStart,
    availabilityEnd = availabilityEnd,
    availabilityExceptions = availabilityExceptions,
    hourlyRate = hourlyRate,
    extraChildRate = extraChildRate,
    city = city,
    state = state,
    verified = verified,
    averageRating = averageRating,
    reviewCount = reviewCount
)

fun TutorProfileEntity.toDomain(fullName: String, children: List<Child> = emptyList()) = TutorProfile(
    email = email,
    fullName = fullName,
    city = city,
    state = state,
    notes = notes,
    preferences = preferences,
    photoUri = photoUri,
    children = children
)

fun ChildEntity.toDomain() = Child(id, tutorEmail, name, age, specialNeeds)

fun BookingEntity.toDomain(
    tutorName: String = "",
    caregiverName: String = "",
    tutorPhotoUri: String = "default",
    caregiverPhotoUri: String = "default",
    childName: String = "",
    childIdsParam: List<Long> = childIds.split(",").mapNotNull { it.toLongOrNull() }
        .ifEmpty { childId?.let { listOf(it) } ?: emptyList() },
    tutorNotes: String = "",
    hourlyRate: Double = 0.0,
    extraChildRate: Double = 0.0
) = Booking(
    id = id,
    tutorEmail = tutorEmail,
    caregiverEmail = caregiverEmail,
    tutorName = tutorName,
    caregiverName = caregiverName,
    tutorPhotoUri = tutorPhotoUri,
    caregiverPhotoUri = caregiverPhotoUri,
    date = date,
    timeSlot = timeSlot,
    durationHours = durationHours,
    location = location,
    childId = childId,
    childIds = childIdsParam,
    childName = childName,
    additionalNotes = additionalNotes,
    tutorNotes = tutorNotes,
    totalPrice = totalPrice,
    hourlyRate = hourlyRate,
    extraChildRate = extraChildRate,
    status = BookingStatus.fromString(status),
    colorHex = colorHex
)

fun MessageEntity.toDomain() = Message(id, senderEmail, receiverEmail, content, timestamp, flagged)

fun CatalogItemEntity.toDomain() = CatalogItem(id, category, name, value, extra)

fun ReviewEntity.toDomain() = Review(id, fromTutorEmail, toCaregiverEmail, bookingId, rating, comment, timestamp)

fun PrivateNoteEntity.toDomain(tutorName: String) = PrivateNote(
    id, caregiverEmail, tutorEmail, tutorName, note, rating, timestamp
)
