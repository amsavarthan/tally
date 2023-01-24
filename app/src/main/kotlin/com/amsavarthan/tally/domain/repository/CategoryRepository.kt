package com.amsavarthan.tally.domain.repository

import com.amsavarthan.tally.domain.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun insertCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun updateCategory(category: Category)
    fun getCategories(): Flow<List<Category>>
    suspend fun getCategory(id: Long): Category

}