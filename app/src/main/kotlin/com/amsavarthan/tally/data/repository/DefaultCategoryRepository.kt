package com.amsavarthan.tally.data.repository

import com.amsavarthan.tally.data.source.local.CategoryDao
import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.repository.CategoryRepository
import javax.inject.Inject

class DefaultCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    override suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)

    override suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    override suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)

    override suspend fun getCategory(id: Long) = categoryDao.getCategoryEntity(id)

    override fun getCategories() = categoryDao.getCategories()

}