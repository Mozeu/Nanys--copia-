package com.nanys.care.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nanys.care.data.local.entity.ChildEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(child: ChildEntity): Long

    @Update
    suspend fun update(child: ChildEntity)

    @Query("DELETE FROM children WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM children WHERE tutorEmail = :tutorEmail")
    fun observeByTutor(tutorEmail: String): Flow<List<ChildEntity>>

    @Query("SELECT * FROM children WHERE tutorEmail = :tutorEmail")
    suspend fun getByTutor(tutorEmail: String): List<ChildEntity>

    @Query("SELECT * FROM children WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ChildEntity?
}
