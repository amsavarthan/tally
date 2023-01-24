package com.amsavarthan.tally.data.source.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.amsavarthan.tally.AppPreference
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

class TallyPreferencesDataSource @Inject constructor(
    private val appPreference: DataStore<AppPreference>,
) {

    val appData = appPreference.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(AppPreference.getDefaultInstance())
            } else {
                throw exception
            }
        }

    suspend fun updateOnBoardingState(hasOnBoarded: Boolean) {
        try {
            appPreference.updateData { currentPreferences ->
                currentPreferences.toBuilder()
                    .setHasOnboarded(hasOnBoarded)
                    .build()
            }
        } catch (ioException: IOException) {
            Log.e("TallyPreferences", "Failed to update app preferences", ioException)
        }
    }

    suspend fun updateLastSelectedCategoryId(id: Long) {
        try {
            appPreference.updateData { currentPreferences ->
                currentPreferences.toBuilder()
                    .setLastSelectedCategoryId(id)
                    .build()
            }
        } catch (ioException: IOException) {
            Log.e("TallyPreferences", "Failed to update app preferences", ioException)
        }
    }
}