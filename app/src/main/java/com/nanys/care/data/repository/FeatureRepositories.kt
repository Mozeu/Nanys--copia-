package com.nanys.care.data.repository

import com.nanys.care.core.notification.SimulationService
import com.nanys.care.core.util.DateUtil
import com.nanys.care.data.local.db.NanysDatabase
import com.nanys.care.data.local.entity.CatalogItemEntity
import com.nanys.care.data.local.entity.MessageEntity
import com.nanys.care.data.local.entity.PrivateNoteEntity
import com.nanys.care.data.local.entity.ReviewEntity
import com.nanys.care.data.mapper.toDomain
import com.nanys.care.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CaregiverRepository(private val db: NanysDatabase) {
    suspend fun searchCaregivers(filter: CaregiverSearchFilter): List<CaregiverProfile> {
        val profiles = db.caregiverProfileDao().search(
            city = filter.city,
            state = filter.state,
            minPrice = filter.minPrice,
            maxPrice = filter.maxPrice,
            minExperience = filter.minExperience,
            availability = filter.availability
        ).first()
        return profiles.mapNotNull { profile ->
            val user = db.userDao().getByEmail(profile.email) ?: return@mapNotNull null
            val rating = db.reviewDao().averageRating(profile.email).first() ?: 0.0
            val reviewCount = db.reviewDao().countReviews(profile.email).first()
            if (rating < filter.minRating) return@mapNotNull null
            if (filter.query.isNotBlank() &&
                !user.fullName.contains(filter.query, ignoreCase = true) &&
                !profile.city.contains(filter.query, ignoreCase = true)
            ) return@mapNotNull null
            profile.toDomain(user.fullName, rating, reviewCount)
        }.sortedByDescending { it.averageRating }
    }

    suspend fun getCaregiverPublic(email: String): CaregiverProfile? {
        val profile = db.caregiverProfileDao().getByEmail(email) ?: return null
        val user = db.userDao().getByEmail(email) ?: return null
        val rating = db.reviewDao().averageRating(email).first() ?: 0.0
        val count = db.reviewDao().countReviews(email).first()
        return profile.toDomain(user.fullName, rating, count)
    }

    suspend fun updateProfile(profile: com.nanys.care.data.local.entity.CaregiverProfileEntity) {
        db.caregiverProfileDao().update(profile)
    }

    fun countVerified(): Flow<Int> = db.caregiverProfileDao().countVerified()

    suspend fun setVerified(email: String, verified: Boolean) {
        db.caregiverProfileDao().updateVerified(email, verified)
    }

    fun observeAllProfiles(): Flow<List<CaregiverProfile>> {
        return db.caregiverProfileDao().observeAll().flatMapLatest { profiles ->
            flow {
                val result = profiles.mapNotNull { p ->
                    val user = db.userDao().getByEmail(p.email) ?: return@mapNotNull null
                    p.toDomain(user.fullName)
                }
                emit(result)
            }
        }
    }
}

class TutorRepository(private val db: NanysDatabase) {
    suspend fun getTutorPrivate(email: String): TutorProfile? {
        val profile = db.tutorProfileDao().getByEmail(email) ?: return null
        val user = db.userDao().getByEmail(email) ?: return null
        val children = db.childDao().getByTutor(email).map { it.toDomain() }
        return profile.toDomain(user.fullName, children)
    }

    suspend fun saveProfile(profile: com.nanys.care.data.local.entity.TutorProfileEntity) {
        db.tutorProfileDao().update(profile)
    }

    suspend fun saveChild(child: com.nanys.care.data.local.entity.ChildEntity): Long {
        return if (child.id == 0L) db.childDao().insert(child) else {
            db.childDao().update(child)
            child.id
        }
    }

    suspend fun deleteChild(id: Long) = db.childDao().delete(id)
}

class ChatRepository(
    private val db: NanysDatabase,
    private val simulationService: SimulationService
) {
    fun observeConversation(email1: String, email2: String): Flow<List<Message>> =
        db.messageDao().observeConversation(email1, email2).map { list -> list.map { it.toDomain() } }

    fun observeAllMessages(): Flow<List<Message>> =
        db.messageDao().observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun sendMessage(sender: String, receiver: String, content: String) {
        db.messageDao().insert(MessageEntity(senderEmail = sender, receiverEmail = receiver, content = content))
        simulationService.simulateEmail(receiver, "Nuevo mensaje de $sender")
    }

    suspend fun flagMessage(id: Long) = db.messageDao().flagMessage(id)

    fun conversationsForUser(email: String): Flow<List<ConversationSummary>> {
        return db.messageDao().observeAll().flatMapLatest { messages ->
            flow {
                val grouped = messages.groupBy { msg ->
                    if (msg.senderEmail == email) msg.receiverEmail else msg.senderEmail
                }
                val summaries = grouped.mapNotNull { (other, msgs) ->
                    val last = msgs.maxByOrNull { it.timestamp } ?: return@mapNotNull null
                    val user = db.userDao().getByEmail(other)
                    ConversationSummary(
                        otherEmail = other,
                        otherName = user?.fullName ?: other,
                        lastMessage = last.content,
                        lastTimestamp = last.timestamp
                    )
                }.sortedByDescending { it.lastTimestamp }
                emit(summaries)
            }
        }
    }

    fun countTodayConversations(): Flow<Int> =
        db.messageDao().countTodayMessages(DateUtil.startOfTodayMillis())
}

class CatalogRepository(private val db: NanysDatabase) {
    fun observeCategory(category: String): Flow<List<CatalogItem>> =
        db.catalogDao().observeByCategory(category).map { it.map { e -> e.toDomain() } }

    suspend fun add(item: CatalogItemEntity) = db.catalogDao().insert(item)
    suspend fun update(item: CatalogItemEntity) = db.catalogDao().update(item)
    suspend fun delete(item: CatalogItemEntity) = db.catalogDao().delete(item)
    fun countAll(): Flow<Int> = db.catalogDao().countAll()
}

class ReviewRepository(private val db: NanysDatabase) {
    suspend fun submitReview(review: ReviewEntity) = db.reviewDao().insert(review)
    suspend fun hasReview(bookingId: Long) = db.reviewDao().getByBooking(bookingId) != null
    fun observeByCaregiver(email: String) =
        db.reviewDao().observeByCaregiver(email).map { it.map { r -> r.toDomain() } }
}

class PrivateNoteRepository(private val db: NanysDatabase) {
    suspend fun add(note: PrivateNoteEntity) = db.privateNoteDao().insert(note)
    fun observeByCaregiver(email: String): Flow<List<PrivateNote>> =
        db.privateNoteDao().observeByCaregiver(email).flatMapLatest { notes ->
            flow {
                val mapped = notes.map { n ->
                    val tutor = db.userDao().getByEmail(n.tutorEmail)
                    n.toDomain(tutor?.fullName ?: n.tutorEmail)
                }
                emit(mapped)
            }
        }
}
