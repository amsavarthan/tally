package com.amsavarthan.tally.domain.repository

import com.amsavarthan.tally.AppPreference
import kotlinx.coroutines.flow.Flow

interface AppDataRepository {

    val appData: Flow<AppPreference>

    suspend fun updateOnBoardingState(hasOnBoarded: Boolean)

}