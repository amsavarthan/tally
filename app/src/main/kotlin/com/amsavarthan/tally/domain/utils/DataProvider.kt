package com.amsavarthan.tally.domain.utils

import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.entity.CategoryType

object DataProvider {

    fun getDefaultCategories(): List<Category> {
        return listOf(
            Category(name = "Groceries", emoji = "🥑", type = CategoryType.Expense),
            Category(name = "Snacks", emoji = "🍪", type = CategoryType.Expense),
            Category(name = "Eating Out", emoji = "🍽️", type = CategoryType.Expense),
            Category(name = "Coffee", emoji = "☕️", type = CategoryType.Expense),
            Category(name = "Drinks", emoji = "🍹", type = CategoryType.Expense),
            Category(name = "Beauty", emoji = "💄", type = CategoryType.Expense),
            Category(name = "Clothing", emoji = "👕", type = CategoryType.Expense),
            Category(name = "Accessories", emoji = "💍", type = CategoryType.Expense),
            Category(name = "Gifts", emoji = "🎁", type = CategoryType.Expense),
            Category(name = "Entertainment", emoji = "🍿", type = CategoryType.Expense),
            Category(name = "Home", emoji = "🏠", type = CategoryType.Expense),
            Category(name = "Tech", emoji = "📱", type = CategoryType.Expense),
            Category(name = "Subscription", emoji = "📅", type = CategoryType.Expense),
            Category(name = "Car", emoji = "🚗", type = CategoryType.Expense),
            Category(name = "Taxi", emoji = "🚕", type = CategoryType.Expense),
            Category(name = "Charity", emoji = "🎗️", type = CategoryType.Expense),
            Category(name = "Education", emoji = "📚", type = CategoryType.Expense),
            Category(name = "Health", emoji = "💊", type = CategoryType.Expense),
            Category(name = "Travel", emoji = "🏝️", type = CategoryType.Expense),
            Category(name = "Pets", emoji = "🐶", type = CategoryType.Expense),
            Category(name = "Miscellaneous", emoji = "🤷", type = CategoryType.Expense),

            Category(name = "Salary", emoji = "👔", type = CategoryType.Income),
            Category(name = "Business", emoji = "💼", type = CategoryType.Income),
            Category(name = "Other", emoji = "💸", type = CategoryType.Income),
        )
    }

}