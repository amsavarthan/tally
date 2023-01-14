package com.amsavarthan.tally.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.amsavarthan.tally.presentation.ui.screens.NavGraphs
import com.amsavarthan.tally.presentation.ui.theme.TallyTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val TallyPageTransisitions by lazy {
        RootNavGraphDefaultAnimations(
            enterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Start)
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentScope.SlideDirection.End)
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.End)
            }
        )
    }

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            TallyTheme {
                val navController = rememberAnimatedNavController()
                val navHostEngine = rememberAnimatedNavHostEngine(
                    rootDefaultAnimations = TallyPageTransisitions
                )
                DestinationsNavHost(
                    engine = navHostEngine,
                    navGraph = NavGraphs.root,
                    navController = navController,
                )
            }
        }
    }
}