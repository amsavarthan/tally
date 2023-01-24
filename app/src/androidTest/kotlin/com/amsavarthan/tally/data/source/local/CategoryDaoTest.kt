package com.amsavarthan.tally.data.source.local

import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.entity.CategoryType
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
internal class CategoryDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: TallyDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        hiltRule.inject()
        categoryDao = database.categoryDao()
    }

    @Test
    fun insertCategoryItem() = runTest {
        val incomeCategory = Category(
            name = "test-category-income",
            emoji = "🎊",
            type = CategoryType.Income
        )

        //Inserting a category
        categoryDao.insertCategory(incomeCategory)

        //Checking if the list contains the category
        val incomeCategories = categoryDao.getCategories()
            .first()
            .filter { it.type == CategoryType.Income }
        assertThat(incomeCategories).containsExactly(incomeCategory)
    }

    @Test
    fun updateCategoryItem() = runTest {
        val category = Category(
            name = "test-category",
            emoji = "🎊",
            type = CategoryType.Income
        )

        //Inserting a category
        categoryDao.insertCategory(category)

        val updatedCategory = Category(
            name = "test-category-edit",
            emoji = "🟢",
            type = CategoryType.Income
        )

        //Updating the inserted category
        categoryDao.updateCategory(
            oldCategory = category,
            newCategory = updatedCategory
        )

        //Checking if the list contains exactly the updated category
        val incomeCategories = categoryDao.getCategories()
            .first()
            .filter { it.type == CategoryType.Income }
        assertThat(incomeCategories).containsExactly(updatedCategory)
    }

    @Test
    fun deleteCategoryItem() = runTest {
        val category = Category(
            name = "test-category",
            emoji = "🎊",
            type = CategoryType.Expense
        )

        //Inserting a category
        categoryDao.insertCategory(category)

        //Deleting the category
        categoryDao.deleteCategory(category)

        //Checking if the list does not contain the category
        val categories = categoryDao.getCategories()
            .first()
            .filter { it.type == CategoryType.Expense }
        assertThat(categories).doesNotContain(category)
    }

    @Test
    fun getExpenseCategoriesOrderedAlphabetically() = runTest {
        //Dummy category list
        val expenseItems = listOf(
            Category(
                name = "b",
                emoji = "🎊",
                type = CategoryType.Expense
            ), Category(
                name = "A",
                emoji = "🎊",
                type = CategoryType.Expense
            )
        )

        //Inserting the categories
        expenseItems.forEach { category ->
            categoryDao.insertCategory(category)
        }

        //Checking if the list is sorted by name (lowercase)
        val expenseCategories = categoryDao.getCategories()
            .first()
            .filter { it.type == CategoryType.Expense }
        assertThat(expenseCategories[0].name).isLessThan(expenseCategories[1].name)
    }

    @Test
    fun getIncomeCategoriesOrderedAlphabetically() = runTest {
        //Dummy category list
        val incomeItems = listOf(
            Category(
                name = "b",
                emoji = "🎊",
                type = CategoryType.Income
            ), Category(
                name = "A",
                emoji = "🎊",
                type = CategoryType.Income
            )
        )

        //Inserting the categories
        incomeItems.forEach { category ->
            categoryDao.insertCategory(category)
        }

        //Checking if the list is sorted by name (lowercase)
        val incomeCategories = categoryDao.getCategories()
            .first()
            .filter { it.type == CategoryType.Income }
        assertThat(incomeCategories[0].name).isLessThan(incomeCategories[1].name)
    }

    @After
    fun tearDown() {
        database.close()
    }

}