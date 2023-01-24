package com.amsavarthan.tally.domain.usecase

import android.database.sqlite.SQLiteConstraintException
import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.repository.CategoryRepository
import com.amsavarthan.tally.domain.utils.UseCaseResult
import javax.inject.Inject


class AddOrUpdateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(category: Category): UseCaseResult {
        if (category.emoji.isBlank()) return false to "Please enter a valid emoji"
        if (category.name.length < 3) return false to "Please enter a valid category name"

        return try {
            when (category.id) {
                null -> categoryRepository.insertCategory(category)
                else -> categoryRepository.updateCategory(category)
            }
            true to ""
        } catch (e: SQLiteConstraintException) {
            false to "Category with this name already exists"
        }
    }

}