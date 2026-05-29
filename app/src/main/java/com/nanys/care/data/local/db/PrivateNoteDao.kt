package com.nanys.care.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nanys.care.data.local.entity.PrivateNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrivateNoteDao {
    @Insert
    suspend fun insert(note: PrivateNoteEntity): Long

    @Query(
        """
        SELECT * FROM private_notes
        WHERE caregiverEmail = :caregiverEmail
        ORDER BY timestamp DESC
        """
    )
    fun observeByCaregiver(caregiverEmail: String): Flow<List<PrivateNoteEntity>>

    @Query(
        """
        SELECT * FROM private_notes
        WHERE caregiverEmail = :caregiverEmail AND tutorEmail = :tutorEmail
        ORDER BY timestamp DESC
        """
    )
    fun observeByPair(caregiverEmail: String, tutorEmail: String): Flow<List<PrivateNoteEntity>>
}
