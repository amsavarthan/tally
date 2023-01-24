package com.amsavarthan.tally.di

import android.content.Context
import androidx.room.Room
import com.amsavarthan.tally.data.source.local.CategoryDao
import com.amsavarthan.tally.data.source.local.TallyDatabase
import com.amsavarthan.tally.domain.utils.RoomCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesTallyDatabase(
        @ApplicationContext context: Context,
        provider: Provider<CategoryDao>,
    ): TallyDatabase {
        return Room.databaseBuilder(
            context,
            TallyDatabase::class.java,
            TallyDatabase.DB_NAME,
        )
            .addCallback(RoomCallback(provider))
            .build()
    }

}