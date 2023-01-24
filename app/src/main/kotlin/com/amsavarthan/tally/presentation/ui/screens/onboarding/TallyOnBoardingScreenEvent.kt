package com.amsavarthan.tally.presentation.ui.screens.onboarding

sealed interface TallyOnBoardingScreenEvent {
    object OnActionButtonClicked : TallyOnBoardingScreenEvent
    object OnBackPressed : TallyOnBoardingScreenEvent
    data class OnCashAmountEntered(val amount: String) : TallyOnBoardingScreenEvent
}