package com.nanys.care.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nanys.care.data.local.entity.TutorProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TutorProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: TutorProfileEntity)

    @Update
    suspend fun update(profile: TutorProfileEntity)

    @Query("SELECT * FROM tutor_profiles WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): TutorProfileEntity?

    @Query("SELECT * FROM tutor_profiles WHERE email = :email LIMIT 1")
    fun observeByEmail(email: String): Flow<TutorProfileEntity?>
}
