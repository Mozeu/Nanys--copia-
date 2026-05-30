package com.nanys.care.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nanys.care.data.local.entity.CaregiverProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CaregiverProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: CaregiverProfileEntity)

    @Update
    suspend fun update(profile: CaregiverProfileEntity)

    @Query("SELECT * FROM caregiver_profiles WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): CaregiverProfileEntity?

    @Query("SELECT * FROM caregiver_profiles WHERE email = :email LIMIT 1")
    fun observeByEmail(email: String): Flow<CaregiverProfileEntity?>

    @Query("SELECT * FROM caregiver_profiles")
    fun observeAll(): Flow<List<CaregiverProfileEntity>>

    @Query(
        """
        SELECT cp.* FROM caregiver_profiles cp
        INNER JOIN users u ON u.email = cp.email
        WHERE (:city = '' OR cp.city = :city)
          AND (:state = '' OR cp.state = :state)
          AND cp.hourlyRate >= :minPrice
          AND cp.hourlyRate <= :maxPrice
          AND cp.experienceYears >= :minExperience
          AND (
              :availability = ''
              OR cp.availability LIKE '%' || :availability || '%'
              OR cp.availabilityStart LIKE '%' || :availability || '%'
              OR cp.availabilityEnd LIKE '%' || :availability || '%'
              OR cp.availabilityExceptions LIKE '%' || :availability || '%'
          )
        """
    )
    fun search(
        city: String,
        state: String,
        minPrice: Double,
        maxPrice: Double,
        minExperience: Int,
        availability: String
    ): Flow<List<CaregiverProfileEntity>>

    @Query("UPDATE caregiver_profiles SET verified = :verified WHERE email = :email")
    suspend fun updateVerified(email: String, verified: Boolean)

    @Query("SELECT COUNT(*) FROM caregiver_profiles WHERE verified = 1")
    fun countVerified(): Flow<Int>
}
