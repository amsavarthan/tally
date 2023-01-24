package com.amsavarthan.tally.presentation.ui.screens.manage_category

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.Category
import com.amsavarthan.tally.domain.entity.CategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyManageCategoryScreenState(
    val category: Category = Category(
        name = "",
        emoji = "",
        type = CategoryType.Income
    ),
    val shouldShowDeleteConfirmationDialog: Boolean = false,
) : Parcelable {

    companion object {
        fun with(categoryType: CategoryType): TallyManageCategoryScreenState {
            return TallyManageCategoryScreenState(
                category = Category(
                    name = "",
                    emoji = "",
                    type = categoryType
                ),
                shouldShowDeleteConfirmationDialog = false,
            )
        }
    }

}