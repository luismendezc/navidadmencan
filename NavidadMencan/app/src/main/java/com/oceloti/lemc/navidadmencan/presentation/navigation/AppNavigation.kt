package com.oceloti.lemc.navidadmencan.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oceloti.lemc.navidadmencan.presentation.views.GameHubScreenRoot
import com.oceloti.lemc.games.impostor.ImpostorGameEntry


@Composable
fun AppScaffold() {
    val nav = rememberNavController()
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        AppNav(
            navController = nav,
            contentPadding = innerPadding
        )
    }
}

@Composable
private fun AppNav(
    navController: NavHostController,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Route.GameHub.path,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
    ) {
        composable(Route.GameHub.path) {
            GameHubScreenRoot(
                onNavigateToGame = { game ->
                    when (game.id) {
                        "impostor_game" -> {
                            navController.navigate(Route.ImpostorGame.path)
                        }
                        else -> {
                            // TODO: Navigate to other games
                        }
                    }
                }
            )
        }
        
        composable(Route.ImpostorGame.path) {
            ImpostorGameEntry.ImpostorGameScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}