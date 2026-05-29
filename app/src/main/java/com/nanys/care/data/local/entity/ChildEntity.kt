package com.nanys.care.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "children",
    foreignKeys = [
        ForeignKey(
            entity = TutorProfileEntity::class,
            parentColumns = ["email"],
            childColumns = ["tutorEmail"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tutorEmail")]
)
data class ChildEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tutorEmail: String,
    val name: String,
    val age: Int,
    val specialNeeds: String = ""
)
