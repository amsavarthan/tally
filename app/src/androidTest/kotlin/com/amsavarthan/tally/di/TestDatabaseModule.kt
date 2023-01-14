package com.amsavarthan.tally.di

import android.content.Context
import androidx.room.Room
import com.amsavarthan.tally.data.source.local.TallyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
internal object TestDatabaseModule {

    @Provides
    @Singleton
    fun providesTallyDatabase(@ApplicationContext context: Context): TallyDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            TallyDatabase::class.java
        ).allowMainThreadQueries().build()
    }

}