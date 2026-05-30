package com.nanys.care.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nanys.care.data.local.entity.BookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(booking: BookingEntity): Long

    @Update
    suspend fun update(booking: BookingEntity)

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): BookingEntity?

    @Query(
        """
        SELECT * FROM bookings
        WHERE caregiverEmail = :caregiverEmail
          AND date = :date
          AND status = 'accepted'
          AND id != :excludeId
        """
    )
    suspend fun getAcceptedByCaregiverAndDate(
        caregiverEmail: String,
        date: String,
        excludeId: Long
    ): List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE caregiverEmail = :email ORDER BY date ASC, timeSlot ASC")
    fun observeByCaregiver(email: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE tutorEmail = :email ORDER BY date ASC, timeSlot ASC")
    fun observeByTutor(email: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE caregiverEmail = :email AND status = :status")
    fun observeByCaregiverAndStatus(email: String, status: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE tutorEmail = :email AND status = :status")
    fun observeByTutorAndStatus(email: String, status: String): Flow<List<BookingEntity>>

    @Query("SELECT COUNT(*) FROM bookings WHERE caregiverEmail = :email AND status = 'pending'")
    fun countPendingForCaregiver(email: String): Flow<Int>

    @Query(
        """
        SELECT COUNT(*) FROM bookings
        WHERE (caregiverEmail = :email OR tutorEmail = :email)
          AND status = 'accepted'
          AND date = :today
        """
    )
    fun countTodayAccepted(email: String, today: String): Flow<Int>

    @Query("SELECT * FROM bookings WHERE status = 'accepted'")
    fun observeAllAccepted(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings")
    fun observeAll(): Flow<List<BookingEntity>>
}
