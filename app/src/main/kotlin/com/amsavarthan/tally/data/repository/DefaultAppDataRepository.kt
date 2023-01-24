package com.amsavarthan.tally.data.repository

import com.amsavarthan.tally.AppPreference
import com.amsavarthan.tally.data.source.datastore.TallyPreferencesDataSource
import com.amsavarthan.tally.domain.repository.AppDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultAppDataRepository @Inject constructor(
    private val tallyPreferencesDataSource: TallyPreferencesDataSource,
) : AppDataRepository {

    override val appData: Flow<AppPreference>
        get() = tallyPreferencesDataSource.appData

    override suspend fun setOnBoardingState(hasOnBoarded: Boolean) =
        tallyPreferencesDataSource.updateOnBoardingState(hasOnBoarded)

    override suspend fun setLastSelectedCategoryId(id: Long) =
        tallyPreferencesDataSource.updateLastSelectedCategoryId(id)


}