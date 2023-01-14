package com.amsavarthan.tally.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.amsavarthan.tally.AppPreference
import com.amsavarthan.tally.data.utils.AppPreferenceSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    private const val APP_PREFERENCE_STORE_FILE_NAME = "app_pref.proto"

    @Provides
    @Singleton
    fun provideAppPreferencesDatastore(
        @ApplicationContext context: Context,
        appPreferenceSerializer: AppPreferenceSerializer,
    ): DataStore<AppPreference> {
        return DataStoreFactory.create(
            serializer = appPreferenceSerializer,
            produceFile = { context.dataStoreFile(APP_PREFERENCE_STORE_FILE_NAME) }
        )
    }

}