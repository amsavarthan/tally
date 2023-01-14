package com.amsavarthan.tally.data.source.local

import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.AccountType
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class AccountDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: TallyDatabase
    private lateinit var accountDao: AccountDao

    @Before
    fun setUp() {
        hiltRule.inject()
        accountDao = database.accountDao()
    }

    @Test
    fun insertsAccountWithSameNameButDifferentType() = runTest {
        val debitAccount = Account(
            id = 0,
            name = "test-account",
            limit = 100.0,
            type = AccountType.DebitCard
        )

        val creditAccount = Account(
            id = 1,
            name = "test-account",
            limit = 100.0,
            type = AccountType.CreditCard
        )

        //Inserting an account
        accountDao.insertAccount(debitAccount)
        accountDao.insertAccount(creditAccount)

        //Checking if the list contains the account
        val accounts = accountDao.getAccountEntities().first()
        assertThat(accounts).containsExactly(debitAccount, creditAccount)
    }

    @Test
    fun updateAccountItem() = runTest {
        val account = Account(
            id = 0,
            name = "test-account",
            limit = 100.0,
            type = AccountType.DebitCard
        )

        //Inserting an account
        accountDao.insertAccount(account)

        val updatedAccount = Account(
            id = 0,
            name = "test-account-edited",
            limit = 200.0,
            type = AccountType.CreditCard
        )

        //Updating the account
        accountDao.updateAccount(updatedAccount)

        //Checking if the list contains only the updated account
        val accounts = accountDao.getAccountEntities().first()
        assertThat(accounts).containsExactly(updatedAccount)
    }

    @Test
    fun deleteAccountItem() = runTest {
        val account = Account(
            id = 0,
            name = "test-account",
            limit = 100.0,
            type = AccountType.DebitCard
        )

        //Inserting an account
        accountDao.insertAccount(account)

        //Deleting the inserted account
        accountDao.deleteAccount(account)

        //Checking if the list does not contain the account
        val accounts = accountDao.getAccountEntities().first()
        assertThat(accounts).isEmpty()
    }

    @Test
    fun getAccountsOrderedAlphabetically() = runTest {

        //Dummy account list
        val accountItems = listOf(
            Account(
                name = "b",
                limit = 100.0,
                type = AccountType.DebitCard
            ), Account(
                name = "A",
                limit = 100.0,
                type = AccountType.DebitCard
            )
        )

        //Inserting the account
        accountItems.forEach { account ->
            accountDao.insertAccount(account)
        }


        //Checking if the returned list is sorted by name (lowercase)
        val accounts = accountDao.getAccountEntities().first()
        assertThat(accounts[0].name).isLessThan(accounts[1].name)
    }

    @After
    fun tearDown() {
        database.close()
    }

}