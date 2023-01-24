package com.amsavarthan.tally.data.source.local

import androidx.room.*
import com.amsavarthan.tally.domain.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(vararg category: Category)


    @Delete
    suspend fun deleteCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategoryEntity(id: Long): Category

    @Query("SELECT * FROM category ORDER BY name COLLATE NOCASE ASC")
    fun getCategories(): Flow<List<Category>>

}