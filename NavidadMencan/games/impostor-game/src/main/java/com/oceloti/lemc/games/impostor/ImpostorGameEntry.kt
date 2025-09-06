package com.oceloti.lemc.games.impostor

import androidx.compose.runtime.Composable
import com.oceloti.lemc.games.impostor.presentation.views.ImpostorGameMenuScreen

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
        ImpostorGameMenuScreen(
            onCreateGame = {
                // TODO: Navigate to game setup screen
            },
            onJoinGame = {
                // TODO: Navigate to join game screen
            },
            onViewStatistics = {
                // TODO: Navigate to statistics screen
            },
            onSettings = {
                // TODO: Navigate to settings screen
            },
            onNavigateBack = onNavigateBack
        )
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