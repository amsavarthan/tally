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

        runTest {
            database.accountDao().insertAccount(testAccount)
        }
    }

    @Test
    fun insertEntryItem() = runTest {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val transaction = Transaction(
            id = 1,
            amount = 10.0,
            dateTime = now,
            category = Category(
                name = "test-category",
                emoji = "🍔",
                type = CategoryType.Expense
            ),
            accountId = 0
        )

        //Inserting an entry into database
        transactionDao.insertEntry(transaction)

        //Getting entries
        val entries = transactionDao.getEntries().first()

        //Checking if the returned list contains the inserted entry
        assertThat(entries).contains(transaction)
    }

    @Test
    fun deleteEntryItem() = runTest {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val transaction = Transaction(
            id = 1,
            amount = 10.0,
            dateTime = now,
            category = Category(
                name = "test-category",
                emoji = "🍔",
                type = CategoryType.Expense
            ),
            accountId = 0
        )

        //Inserting an entry into database
        transactionDao.insertEntry(transaction)

        //Deleting the entry
        transactionDao.deleteEntry(transaction)

        //Getting entries
        val entries = transactionDao.getEntries().first()

        //Checking if the returned list does not contain the entry
        assertThat(entries).doesNotContain(transaction)
    }

    @Test
    fun updateEntryItem() = runTest {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val transaction = Transaction(
            id = 1,
            amount = 10.0,
            dateTime = now,
            category = Category(
                name = "test-category",
                emoji = "🍔",
                type = CategoryType.Expense
            ),
            accountId = 0
        )

        //Inserting an entry into database
        transactionDao.insertEntry(transaction)

        val yesterday = Clock.System.now()
            .minus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val updatedTransaction = Transaction(
            id = 1,
            amount = 20.0,
            dateTime = yesterday,
            category = Category(
                name = "test-category-edited",
                emoji = "🍔",
                type = CategoryType.Expense
            ),
            accountId = 0
        )

        //Deleting the entry
        transactionDao.insertEntry(updatedTransaction)

        //Getting entries
        val entries = transactionDao.getEntries().first()

        //Checking if the returned list contain the updated entry
        assertThat(entries[0]).isEqualTo(updatedTransaction)
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
                    category = Category(
                        name = "test-category",
                        emoji = "🍔",
                        type = CategoryType.Expense
                    ),
                    accountId = 0
                )
                add(transaction)
            }
        }.shuffled()

        //Inserting into database
        testEntries.forEach { entry ->
            transactionDao.insertEntry(entry)
        }

        //Getting entries
        val entries = transactionDao.getEntries().first()

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
            category = Category(
                name = "test-category",
                emoji = "🍔",
                type = CategoryType.Expense
            ),
            accountId = 0
        )

        val currentWeekIncome = Transaction(
            id = 2,
            amount = 1.50,
            dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault()),
            category = Category(
                name = "test-category",
                emoji = "🍔",
                type = CategoryType.Income
            ),
            accountId = 0
        )

        //Previous week tally
        val lastWeekExpense = Transaction(
            id = 3,
            amount = 10.0,
            dateTime = now
                .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            category = Category(
                name = "test-category",
                emoji = "🍔",
                type = CategoryType.Expense
            ),
            accountId = 0
        )

        val lastWeekIncome = Transaction(
            id = 4,
            amount = 15.0,
            dateTime = now
                .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                .toLocalDateTime(TimeZone.currentSystemDefault()),
            category = Category(
                name = "test-category",
                emoji = "🍔",
                type = CategoryType.Income
            ),
            accountId = 0
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
                category = Category(
                    name = "test-category",
                    emoji = "🍔",
                    type = CategoryType.Expense
                ),
                accountId = 0
            )
            add(currentWeekExpense)

            val currentWeekIncome = Transaction(
                amount = 1.50,
                dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault()),
                category = Category(
                    name = "test-category",
                    emoji = "🍔",
                    type = CategoryType.Income
                ),
                accountId = 0
            )
            add(currentWeekIncome)

            //Previous week tally
            val lastWeekExpense = Transaction(
                amount = 10.0,
                dateTime = now
                    .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                category = Category(
                    name = "test-category",
                    emoji = "🍔",
                    type = CategoryType.Expense
                ),
                accountId = 0
            )
            add(lastWeekExpense)

            val lastWeekIncome = Transaction(
                amount = 15.0,
                dateTime = now
                    .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                category = Category(
                    name = "test-category",
                    emoji = "🍔",
                    type = CategoryType.Income
                ),
                accountId = 0
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
                category = Category(
                    name = "test-category",
                    emoji = "🍔",
                    type = CategoryType.Expense
                ),
                accountId = 0
            )
            add(currentWeekExpense)

            val currentWeekIncome = Transaction(
                amount = 3.0,
                dateTime = now.toLocalDateTime(TimeZone.currentSystemDefault()),
                category = Category(
                    name = "test-category",
                    emoji = "🍔",
                    type = CategoryType.Income
                ),
                accountId = 0
            )
            add(currentWeekIncome)

            //Previous week tally
            val lastWeekExpense = Transaction(
                amount = 10.0,
                dateTime = now
                    .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                category = Category(
                    name = "test-category",
                    emoji = "🍔",
                    type = CategoryType.Expense
                ),
                accountId = 0
            )
            add(lastWeekExpense)

            val lastWeekIncome = Transaction(
                amount = 15.0,
                dateTime = now
                    .minus(1, DateTimeUnit.WEEK, TimeZone.currentSystemDefault())
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                category = Category(
                    name = "test-category",
                    emoji = "🍔",
                    type = CategoryType.Income
                ),
                accountId = 0
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