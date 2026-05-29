package com.nanys.care.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nanys.care.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun exists(email: String): Int

    @Query("SELECT COUNT(*) FROM users WHERE role = :role")
    fun countByRole(role: String): Flow<Int>

    @Query("SELECT * FROM users WHERE role = :role")
    suspend fun getByRole(role: String): List<UserEntity>

    @Query("UPDATE users SET fullName = :fullName, phone = :phone WHERE email = :email")
    suspend fun updateBasicInfo(email: String, fullName: String, phone: String)
}
