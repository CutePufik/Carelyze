package com.example.carelyze.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.lifecycle.ViewModelProvider
import com.example.carelyze.data.local.PreferencesManager
import com.example.carelyze.di.ViewModelFactory
import com.example.carelyze.presentation.screen.*
import com.example.carelyze.presentation.viewmodel.OnboardingViewModel

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Auth : Screen("auth")
    object Scanner : Screen("scanner")
    object Advice : Screen("advice")
    object AdviceDetail : Screen("advice_detail/{adviceId}") {
        fun createRoute(adviceId: String) = "advice_detail/$adviceId"
    }
    object ScanProcess : Screen("scan_process/{scannerId}") {
        fun createRoute(scannerId: String) = "scan_process/$scannerId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    viewModelFactory: ViewModelProvider.Factory,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                viewModel = viewModel<OnboardingViewModel>(factory = viewModelFactory),
                onComplete = {
                    preferencesManager.setOnboardingShown(true)
                    val nextRoute = if (preferencesManager.getAccessToken().isNullOrBlank()) {
                        Screen.Auth.route
                    } else {
                        Screen.Scanner.route
                    }
                    navController.navigate(nextRoute) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Auth.route) {
            AuthScreen(
                viewModelFactory = viewModelFactory,
                onAuthSuccess = {
                    navController.navigate(Screen.Scanner.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Scanner.route) {
            ScannerScreen(
                viewModelFactory = viewModelFactory,
                onScannerClick = { scannerId ->
                    navController.navigate(Screen.ScanProcess.createRoute(scannerId))
                },
                onNavigateToAdvice = {
                    navController.navigate(Screen.Advice.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(Screen.Advice.route) {
            AdviceScreen(
                viewModelFactory = viewModelFactory,
                onAdviceClick = { adviceId ->
                    navController.navigate(Screen.AdviceDetail.createRoute(adviceId))
                },
                onNavigateToScanner = {
                    navController.navigate(Screen.Scanner.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        
        composable(
            route = Screen.AdviceDetail.route,
            arguments = listOf(navArgument("adviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val adviceId = backStackEntry.arguments?.getString("adviceId") ?: ""
            AdviceDetailScreen(
                adviceId = adviceId,
                viewModelFactory = viewModelFactory,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.ScanProcess.route,
            arguments = listOf(navArgument("scannerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val scannerId = backStackEntry.arguments?.getString("scannerId") ?: ""
            ScanProcessScreen(
                scannerId = scannerId,
                viewModelFactory = viewModelFactory,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
