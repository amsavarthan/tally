package com.amsavarthan.tally.data.source.local

import androidx.room.*
import com.amsavarthan.tally.domain.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Transaction
    suspend fun updateCategory(oldCategory: Category, newCategory: Category) {
        deleteCategory(oldCategory)
        insertCategory(newCategory)
    }

    @Query("SELECT * FROM category WHERE type = 'income' ORDER BY name COLLATE NOCASE ASC")
    fun getIncomeCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE type = 'expense' ORDER BY name COLLATE NOCASE ASC")
    fun getExpenseCategories(): Flow<List<Category>>

}