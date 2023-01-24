package com.amsavarthan.tally.di

import com.amsavarthan.tally.data.repository.DefaultAccountsRepository
import com.amsavarthan.tally.data.repository.DefaultAppDataRepository
import com.amsavarthan.tally.data.repository.DefaultCategoryRepository
import com.amsavarthan.tally.data.repository.DefaultTransactionRepository
import com.amsavarthan.tally.domain.repository.AccountsRepository
import com.amsavarthan.tally.domain.repository.AppDataRepository
import com.amsavarthan.tally.domain.repository.CategoryRepository
import com.amsavarthan.tally.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsAccountsRepository(
        accountsRepository: DefaultAccountsRepository,
    ): AccountsRepository

    @Binds
    fun bindsCategoryRepository(
        categoryRepository: DefaultCategoryRepository,
    ): CategoryRepository

    @Binds
    fun bindsTransactionRepository(
        transactionRepository: DefaultTransactionRepository,
    ): TransactionRepository

    @Binds
    fun bindsAppPreferenceRepository(
        appPreferenceRepository: DefaultAppDataRepository,
    ): AppDataRepository

}