package com.oceloti.lemc.games.impostor

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oceloti.lemc.games.impostor.data.network.BluetoothService
import com.oceloti.lemc.games.impostor.presentation.viewmodels.CreateGameViewModel
import com.oceloti.lemc.games.impostor.presentation.viewmodels.CreateGameUiEvent
import com.oceloti.lemc.games.impostor.presentation.views.*
import kotlinx.coroutines.flow.collectLatest

/**
 * Public entry point for the Impostor Game module
 * This is the main interface that external modules use to access the game
 */
object ImpostorGameEntry {
    
    /**
     * Main composable entry point for the Impostor game
     * 
     * @param onNavigateBack Callback to return to the main game hub
     */
    @Composable
    fun ImpostorGameScreen(
        onNavigateBack: () -> Unit = {}
    ) {
        val navController = rememberNavController()
        
        NavHost(
            navController = navController,
            startDestination = "menu"
        ) {
            composable("menu") {
                ImpostorGameMenuScreen(
                    onCreateGame = {
                        navController.navigate("create_game")
                    },
                    onJoinGame = {
                        navController.navigate("join_game")
                    },
                    onViewStatistics = {
                        navController.navigate("statistics")
                    },
                    onSettings = {
                        navController.navigate("settings")
                    },
                    onNavigateBack = onNavigateBack
                )
            }
            
            composable("create_game") {
                CreateGameScreenRoot(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToLobby = {
                        navController.navigate("lobby") {
                            popUpTo("create_game") { inclusive = true }
                        }
                    }
                )
            }
            
            composable("lobby") {
                GameLobbyScreenRoot(
                    onNavigateBack = {
                        navController.popBackStack("menu", false)
                    },
                    onStartGame = { gameSession ->
                        navController.navigate("game/${gameSession.id.value}")
                    }
                )
            }
            
            composable("join_game") {
                // TODO: Implement join game screen
                ImpostorGameMenuScreen(onNavigateBack = { navController.popBackStack() })
            }
            
            composable("statistics") {
                // TODO: Implement statistics screen
                ImpostorGameMenuScreen(onNavigateBack = { navController.popBackStack() })
            }
            
            composable("settings") {
                // TODO: Implement settings screen
                ImpostorGameMenuScreen(onNavigateBack = { navController.popBackStack() })
            }
            
            composable("game/{gameId}") { backStackEntry ->
                val gameId = backStackEntry.arguments?.getString("gameId") ?: ""
                // TODO: Implement actual game screen
                ImpostorGameMenuScreen(onNavigateBack = { navController.popBackStack("menu", false) })
            }
        }
    }
    
    @Composable
    private fun CreateGameScreenRoot(
        onNavigateBack: () -> Unit,
        onNavigateToLobby: () -> Unit
    ) {
        // TODO: In a real app, this would be injected via DI
        // For now, we'll create a placeholder implementation
        val context = androidx.compose.ui.platform.LocalContext.current
        val bluetoothService = remember { BluetoothService(context) }
        
        val viewModel: CreateGameViewModel = viewModel {
            CreateGameViewModel(bluetoothService)
        }
        
        val uiState by viewModel.uiState.collectAsState()
        
        // Handle navigation events
        LaunchedEffect(Unit) {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    is CreateGameUiEvent.NavigateToLobby -> {
                        onNavigateToLobby()
                    }
                    is CreateGameUiEvent.ShowError -> {
                        // TODO: Show snackbar or error dialog
                    }
                    is CreateGameUiEvent.NavigateToGame -> {
                        // TODO: Navigate to actual game screen
                    }
                }
            }
        }
        
        CreateGameScreen(
            onNavigateBack = onNavigateBack,
            onStartHosting = { config ->
                viewModel.startHosting(config)
            },
            availableCategories = uiState.availableCategories
        )
    }
    
    @Composable
    private fun GameLobbyScreenRoot(
        onNavigateBack: () -> Unit,
        onStartGame: (com.oceloti.lemc.games.impostor.domain.models.GameSession) -> Unit
    ) {
        // TODO: In a real app, this would be injected via DI
        // For now, we'll create a placeholder implementation
        val context = androidx.compose.ui.platform.LocalContext.current
        val bluetoothService = remember { BluetoothService(context) }
        
        val viewModel: CreateGameViewModel = viewModel {
            CreateGameViewModel(bluetoothService)
        }
        
        val uiState by viewModel.uiState.collectAsState()
        
        // Handle navigation events
        LaunchedEffect(Unit) {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    is CreateGameUiEvent.NavigateToGame -> {
                        onStartGame(event.gameSession)
                    }
                    is CreateGameUiEvent.ShowError -> {
                        // TODO: Show snackbar or error dialog
                    }
                    else -> { /* Handle other events */ }
                }
            }
        }
        
        uiState.gameConfig?.let { config ->
            GameLobbyScreen(
                gameConfig = config,
                players = uiState.playersInLobby,
                gameId = uiState.gameId ?: com.oceloti.lemc.games.impostor.domain.models.GameId(""),
                connectionStatus = uiState.connectionStatus,
                onNavigateBack = {
                    viewModel.stopHosting()
                    onNavigateBack()
                },
                onStartGame = {
                    viewModel.startGame()
                },
                onKickPlayer = { playerId ->
                    viewModel.kickPlayer(playerId)
                }
            )
        }
    }
    
    /**
     * Get information about this game for the main hub
     */
    fun getGameInfo(): GameModuleInfo {
        return GameModuleInfo(
            id = "impostor_game",
            name = "¿Quién es el impostor?",
            description = "Un jugador es el impostor y debe adivinar la palabra secreta mientras los demás intentan descubrirlo",
            minPlayers = 3,
            maxPlayers = 12,
            estimatedDuration = 15,
            category = "PARTY",
            isInstalled = true,
            difficulty = "EASY"
        )
    }
}

/**
 * Information about a game module for the main hub
 */
data class GameModuleInfo(
    val id: String,
    val name: String,
    val description: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val estimatedDuration: Int,
    val category: String,
    val isInstalled: Boolean,
    val difficulty: String
)