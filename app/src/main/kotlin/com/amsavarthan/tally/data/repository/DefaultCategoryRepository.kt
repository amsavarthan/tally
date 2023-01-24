package com.amsavarthan.tally.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.amsavarthan.tally.data.source.local.CategoryDao
import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.repository.CategoryRepository
import javax.inject.Inject

class DefaultCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    @Throws(SQLiteConstraintException::class)
    override suspend fun insertCategory(category: Category) {
        try {
            categoryDao.insertCategory(category)
        } catch (e: SQLiteConstraintException) {
            throw e
        }
    }

    override suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    @Throws(SQLiteConstraintException::class)
    override suspend fun updateCategory(category: Category) {
        try {
            categoryDao.updateCategory(category)
        } catch (e: SQLiteConstraintException) {
            throw e
        }
    }

    override suspend fun getCategory(id: Long) = categoryDao.getCategoryEntity(id)

    override fun getCategories() = categoryDao.getCategories()

}