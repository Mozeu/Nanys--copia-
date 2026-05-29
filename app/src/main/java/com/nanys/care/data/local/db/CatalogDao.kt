package com.nanys.care.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nanys.care.data.local.entity.CatalogItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CatalogItemEntity): Long

    @Update
    suspend fun update(item: CatalogItemEntity)

    @Delete
    suspend fun delete(item: CatalogItemEntity)

    @Query("SELECT * FROM catalog_items WHERE category = :category ORDER BY name ASC")
    fun observeByCategory(category: String): Flow<List<CatalogItemEntity>>

    @Query("SELECT * FROM catalog_items WHERE category = :category ORDER BY name ASC")
    suspend fun getByCategory(category: String): List<CatalogItemEntity>

    @Query("SELECT COUNT(*) FROM catalog_items")
    fun countAll(): Flow<Int>
}
