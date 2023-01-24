package com.amsavarthan.tally.data.source.local

import com.amsavarthan.tally.domain.entity.*
import com.google.common.truth.Truth.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class TransactionDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: TallyDatabase
    private lateinit var transactionDao: TransactionDao

    @Before
    fun setUp() {
        hiltRule.inject()
        transactionDao = database.transactionDao()

        val testAccount = Account(
            id = 0,
            name = "test-account",
            limit = 0.0,
            type = AccountType.Cash,
        )

        val testIncome = Category(
            id = 0,
            name = "test-category",
            emoji = "🎁",
            type = CategoryType.Income
        )

        val testExpense = Category(
            id = 1,
            name = "test-category-expense",
            emoji = "💸",
            type = CategoryType.Expense
        )

        runTest {
            database.accountDao().insertAccount(testAccount)
            database.categoryDao().insertCategory(testIncome, testExpense)
        }
    }

    @Test
    fun getEntriesOrderedDescendingByDate() = runTest {

        //builds a list of entries with time incremented by one hour for each iteration
        val testEntries = buildList {
            repeat(5) { incrementFactor ->
                val dateTime = Clock.System.now()
                    .plus(incrementFactor, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                val transaction = Transaction(
                    amount = 10.0,
                    dateTime = dateTime,
                    categoryId = 0,
                    accountId = 0,
                    transactionType = CategoryType.Income
                )
                add(transaction)
            }
        }.shuffled()

        //Inserting into database
        testEntries.forEach { entry ->
            transactionDao.insertEntry(entry)
        }

        //Getting entries
        val entries = transactionDao.getTransactionEntries().first()

        //Checking if the returned list is in descending order
        assertThat(entries[0].dateTime).isGreaterThan(entries[1].dateTime)

    }

    @Test
    fun getEntriesOfCurrentWeek() = runTest {

        val now = Clock.System.now()

        //Current week tally
        val currentWeekExpense = Transaction(
            id = 1,
            amount = 2.0,
            dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault()),
            categoryId = 1,
            accountId = 0,
            transactionType = CategoryType.Expense
        )

        val currentWeekIncome = Transaction(
            id = 2,
            amount = 1.50,
            dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault()),
            categoryId = 0,
            accountId = 0,
            transactionType = CategoryType.Income
        )

        //Previous week tally
        val lastWeekExpense = Transaction(
            id = 3,
            amount = 10.0,
            dateTime = now
                .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            categoryId = 1,
            accountId = 0,
            transactionType = CategoryType.Expense
        )

        val lastWeekIncome = Transaction(
            id = 4,
            amount = 15.0,
            dateTime = now
                .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            categoryId = 0,
            accountId = 0,
            transactionType = CategoryType.Income
        )

        val testEntries = listOf(
            currentWeekExpense,
            currentWeekIncome,
            lastWeekExpense,
            lastWeekIncome
        )

        //Inserting into database
        testEntries.forEach { entry ->
            transactionDao.insertEntry(entry)
        }

        //Getting entries
        val entries = transactionDao.getEntriesOfCurrentWeek().first()

        //Checking if the returned list has only current week entries
        assertThat(entries).containsExactly(currentWeekIncome, currentWeekExpense)

    }

    @Test
    fun getAmountSpentInCurrentWeek() = runTest {

        val now = Clock.System.now()
        val testEntries = buildList {

            //Current week tally
            val currentWeekExpense = Transaction(
                amount = 2.0,
                dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault()),
                categoryId = 1,
                accountId = 0,
                transactionType = CategoryType.Expense
            )
            add(currentWeekExpense)

            val currentWeekIncome = Transaction(
                amount = 1.50,
                dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault()),
                categoryId = 0,
                accountId = 0,
                transactionType = CategoryType.Income
            )
            add(currentWeekIncome)

            //Previous week tally
            val lastWeekExpense = Transaction(
                amount = 10.0,
                dateTime = now
                    .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                categoryId = 1,
                accountId = 0,
                transactionType = CategoryType.Expense
            )
            add(lastWeekExpense)

            val lastWeekIncome = Transaction(
                amount = 15.0,
                dateTime = now
                    .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                categoryId = 0,
                accountId = 0,
                transactionType = CategoryType.Income
            )
            add(lastWeekIncome)

        }

        //Inserting into database
        testEntries.forEach { entry ->
            transactionDao.insertEntry(entry)
        }

        val amountSpentInCurrentWeek = transactionDao.getAmountSpentInCurrentWeek().first()

        // Income - 1.50, Expense - 2.00 thus spent is 0.50
        assertThat(amountSpentInCurrentWeek).isEqualTo(0.50)

    }

    @Test
    fun getAmountSpentInCurrentWeekAsZeroIfIncomeIsGreaterThanExpense() = runTest {
        val now = Clock.System.now()
        val testEntries = buildList {

            //Current week tally
            val currentWeekExpense = Transaction(
                amount = 2.0,
                dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault()),
                categoryId = 1,
                accountId = 0,
                transactionType = CategoryType.Expense
            )
            add(currentWeekExpense)

            val currentWeekIncome = Transaction(
                amount = 3.0,
                dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault()),
                categoryId = 0,
                accountId = 0,
                transactionType = CategoryType.Income
            )
            add(currentWeekIncome)

            //Previous week tally
            val lastWeekExpense = Transaction(
                amount = 10.0,
                dateTime = now
                    .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                categoryId = 1,
                accountId = 0,
                transactionType = CategoryType.Expense
            )
            add(lastWeekExpense)

            val lastWeekIncome = Transaction(
                amount = 15.0,
                dateTime = now
                    .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                categoryId = 0,
                accountId = 0,
                transactionType = CategoryType.Income
            )
            add(lastWeekIncome)

        }

        //Inserting into database
        testEntries.forEach { entry ->
            transactionDao.insertEntry(entry)
        }

        val amountSpentInCurrentWeek = transactionDao.getAmountSpentInCurrentWeek().first()

        // Income - 3.00, Expense - 2.00 thus spent is -1 which should give 0
        assertThat(amountSpentInCurrentWeek).isEqualTo(0)

    }

    @After
    fun tearDown() {
        database.close()
    }
}