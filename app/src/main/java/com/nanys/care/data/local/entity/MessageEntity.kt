package com.nanys.care.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    indices = [Index("senderEmail"), Index("receiverEmail"), Index("timestamp")]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderEmail: String,
    val receiverEmail: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val flagged: Boolean = false
)
