package com.amsavarthan.tally.presentation.ui.screens.chooser

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.Account
import com.amsavarthan.tally.domain.entity.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyChooserScreenState(
    val cashAccount: Account? = null,
    val accounts: List<Account> = emptyList(),
    val categories: List<Category> = emptyList(),
) : Parcelable