package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.repository.CategoryRepository
import javax.inject.Inject

class AddOrUpdateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(category: Category): Boolean {
        if (category.emoji.isBlank() || category.name.length < 3) return false

        when (category.id) {
            null -> categoryRepository.insertCategory(category)
            else -> categoryRepository.updateCategory(category)
        }

        return true
    }

}