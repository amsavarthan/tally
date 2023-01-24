package com.amsavarthan.tally.di

import com.amsavarthan.tally.data.source.local.AccountDao
import com.amsavarthan.tally.data.source.local.CategoryDao
import com.amsavarthan.tally.data.source.local.TallyDatabase
import com.amsavarthan.tally.data.source.local.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun provideEntryDao(
        database: TallyDatabase,
    ): TransactionDao = database.transactionDao()

    @Provides
    fun provideCategoryDao(
        database: TallyDatabase,
    ): CategoryDao = database.categoryDao()

    @Provides
    fun provideAccountDao(
        database: TallyDatabase,
    ): AccountDao = database.accountDao()

}