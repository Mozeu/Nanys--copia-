package com.nanys.care.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nanys.care.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review: ReviewEntity): Long

    @Query("SELECT AVG(rating) FROM reviews WHERE toCaregiverEmail = :email")
    fun averageRating(email: String): Flow<Double?>

    @Query("SELECT COUNT(*) FROM reviews WHERE toCaregiverEmail = :email")
    fun countReviews(email: String): Flow<Int>

    @Query("SELECT * FROM reviews WHERE toCaregiverEmail = :email ORDER BY timestamp DESC")
    fun observeByCaregiver(email: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE bookingId = :bookingId LIMIT 1")
    suspend fun getByBooking(bookingId: Long): ReviewEntity?
}
