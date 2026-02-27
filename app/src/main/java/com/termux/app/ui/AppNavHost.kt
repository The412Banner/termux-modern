package com.termux.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.termux.app.TermuxService
import com.termux.app.ui.screens.TerminalScreen

sealed class Screen(val route: String) {
    object Terminal : Screen("terminal")
    object Settings : Screen("settings")
}

@Composable
fun AppNavHost(
    termuxService: TermuxService?,
    onOpenOldSettings: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Terminal.route) {
        composable(Screen.Terminal.route) {
            TerminalScreen(
                service = termuxService,
                onNavigateToSettings = onOpenOldSettings
            )
        }
    }
}
