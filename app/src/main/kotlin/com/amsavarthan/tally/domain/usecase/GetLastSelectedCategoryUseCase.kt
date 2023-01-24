package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.repository.AppDataRepository
import com.amsavarthan.tally.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetLastSelectedCategoryUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository,
    private val categoryRepository: CategoryRepository,
) {

    suspend operator fun invoke(): Category {
        val lastSelectedCategoryId = appDataRepository
            .appData
            .first()
            .lastSelectedCategoryId

        return categoryRepository.getCategory(lastSelectedCategoryId)
    }

}