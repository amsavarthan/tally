package com.amsavarthan.tally.data.source.local

import android.database.sqlite.SQLiteConstraintException
import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.entity.CategoryType
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
internal class CategoryDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: TallyDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setup() {
        hiltRule.inject()
        categoryDao = database.categoryDao()
    }

    @Test
    fun insertsCategoryWithSameNameButDifferentType() = runTest {
        val income = Category(
            id = 0,
            emoji = "🛍️",
            name = "Shopping",
            type = CategoryType.Income
        )

        val expense = Category(
            id = 1,
            emoji = "🛍️",
            name = "Shopping",
            type = CategoryType.Expense
        )

        //Inserting an account
        categoryDao.insertCategory(income)
        categoryDao.insertCategory(expense)

        //Checking if the list contains the account
        val categories = categoryDao.getCategories().first()
        assertThat(categories).containsExactly(income, expense)
    }

    @Test
    fun failsToInsertsCategoryWithSameNameAndType() = runTest {
        val income = Category(
            id = 0,
            emoji = "🛍️",
            name = "Shopping",
            type = CategoryType.Income
        )

        val anotherIncome = Category(
            id = 1,
            emoji = "🛍️",
            name = "Shopping",
            type = CategoryType.Income
        )

        categoryDao.insertCategory(income)

        //Should fail
        try {
            categoryDao.insertCategory(anotherIncome)
        } catch (e: SQLiteConstraintException) {
            assertThat(e).isInstanceOf(SQLiteConstraintException::class.java)
        }
    }

    @Test
    fun getCategoriesOrderedAlphabetically() = runTest {

        //Dummy account list
        val categoryItems = listOf(
            Category(
                id = 0,
                emoji = "🛍️",
                name = "f",
                type = CategoryType.Income
            ), Category(
                id = 1,
                emoji = "🛍️",
                name = "a",
                type = CategoryType.Expense
            )
        )

        //Inserting the account
        categoryItems.forEach { category ->
            categoryDao.insertCategory(category)
        }

        //Checking if the returned list is sorted by name (lowercase)
        val categories = categoryDao.getCategories().first()
        assertThat(categories[0].name).isLessThan(categories[1].name)
    }

    @After
    fun tearDown() {
        database.close()
    }

}