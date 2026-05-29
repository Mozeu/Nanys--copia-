package com.nanys.care.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nanys.care.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: MessageEntity): Long

    @Query(
        """
        SELECT * FROM messages
        WHERE (senderEmail = :email1 AND receiverEmail = :email2)
           OR (senderEmail = :email2 AND receiverEmail = :email1)
        ORDER BY timestamp ASC
        """
    )
    fun observeConversation(email1: String, email2: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<MessageEntity>>

    @Query("UPDATE messages SET flagged = 1 WHERE id = :id")
    suspend fun flagMessage(id: Long)

    @Query(
        """
        SELECT COUNT(*) FROM messages
        WHERE timestamp >= :startOfDay
        """
    )
    fun countTodayMessages(startOfDay: Long): Flow<Int>
}
