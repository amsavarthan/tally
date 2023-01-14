package com.amsavarthan.tally.domain.usecase

import com.amsavarthan.tally.domain.repository.AppDataRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOnBoardingStatusUseCase @Inject constructor(
    private val appDataRepository: AppDataRepository,
) {

    operator fun invoke() = appDataRepository.appData.map { it.hasOnboarded }

}