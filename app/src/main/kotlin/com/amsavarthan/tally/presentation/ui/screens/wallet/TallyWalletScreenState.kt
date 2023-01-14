package com.amsavarthan.tally.presentation.ui.screens.wallet

import android.os.Parcelable
import com.amsavarthan.tally.domain.entity.WalletAmountDetail
import kotlinx.parcelize.Parcelize

@Parcelize
data class TallyWalletScreenState(
    val walletAmountDetail: WalletAmountDetail = WalletAmountDetail(),
) : Parcelable