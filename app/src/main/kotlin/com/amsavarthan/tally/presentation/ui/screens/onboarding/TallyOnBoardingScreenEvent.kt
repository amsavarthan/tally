package com.amsavarthan.tally.presentation.ui.screens.onboarding

sealed class TallyOnBoardingScreenEvent {
    object OnActionButtonClicked : TallyOnBoardingScreenEvent()
    object OnBackPressed : TallyOnBoardingScreenEvent()
    data class OnCashAmountEntered(val amount: String) : TallyOnBoardingScreenEvent()
}