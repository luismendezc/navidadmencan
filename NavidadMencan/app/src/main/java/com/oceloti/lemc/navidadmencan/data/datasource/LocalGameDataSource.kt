package com.oceloti.lemc.navidadmencan.data.datasource

import com.oceloti.lemc.navidadmencan.core.util.AppLog.logD
import com.oceloti.lemc.navidadmencan.core.util.AppLog.logE
import com.oceloti.lemc.navidadmencan.data.model.GameDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Local implementation of GameDataSource with in-memory storage
 * In a real app, this would use Room database or other persistent storage
 */
class LocalGameDataSource : GameDataSource {
    
    // In-memory game storage - in real app this would be Room database
    private val _games = MutableStateFlow(getInitialGames())
    
    override suspend fun getAllGames(): Flow<List<GameDto>> {
        logD("LocalGameDataSource", "Getting all games")
        return _games
    }
    
    override suspend fun getGameById(gameId: String): GameDto? {
        logD("LocalGameDataSource", "Getting game by ID: $gameId")
        return _games.value.find { it.id == gameId }
    }
    
    override suspend fun updateGameInstallationStatus(
        gameId: String,
        isInstalled: Boolean
    ): Result<Unit> {
        logD("LocalGameDataSource", "Updating installation status for $gameId: $isInstalled")
        return try {
            val currentGames = _games.value.toMutableList()
            val gameIndex = currentGames.indexOfFirst { it.id == gameId }
            
            if (gameIndex == -1) {
                return Result.failure(IllegalArgumentException("Game not found: $gameId"))
            }
            
            currentGames[gameIndex] = currentGames[gameIndex].copy(isInstalled = isInstalled)
            _games.value = currentGames
            
            Result.success(Unit)
        } catch (e: Exception) {
            logE("LocalGameDataSource", "Error updating installation status", e)
            Result.failure(e)
        }
    }
    
    override suspend fun searchGames(query: String): Flow<List<GameDto>> {
        logD("LocalGameDataSource", "Searching games with query: $query")
        return _games.map { games ->
            if (query.isBlank()) {
                games
            } else {
                games.filter {
                    it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
                }
            }
        }
    }
    
    override suspend fun gameExists(gameId: String): Boolean {
        return _games.value.any { it.id == gameId }
    }
    
    /**
     * Initial games data - in real app this would come from network/database
     */
    private fun getInitialGames(): List<GameDto> = listOf(
        GameDto(
            id = "uno",
            name = "UNO",
            description = "El clásico juego de cartas donde el objetivo es quedarte sin cartas",
            minPlayers = 2,
            maxPlayers = 10,
            estimatedDurationMinutes = 15,
            category = "CARD_GAME",
            isInstalled = true,
            difficulty = "EASY"
        ),
        GameDto(
            id = "trivia_navidad",
            name = "Trivia Navideña",
            description = "Preguntas sobre tradiciones navideñas, películas y canciones",
            minPlayers = 2,
            maxPlayers = 8,
            estimatedDurationMinutes = 20,
            category = "TRIVIA",
            isInstalled = false,
            difficulty = "MEDIUM"
        ),
        GameDto(
            id = "charades",
            name = "Charadas Familiares",
            description = "Actúa palabras y frases para que tu equipo adivine",
            minPlayers = 4,
            maxPlayers = 12,
            estimatedDurationMinutes = 30,
            category = "PARTY_GAME",
            isInstalled = true,
            difficulty = "EASY"
        ),
        GameDto(
            id = "pictionary",
            name = "Pictionary",
            description = "Dibuja y adivina palabras en equipos",
            minPlayers = 4,
            maxPlayers = 8,
            estimatedDurationMinutes = 25,
            category = "PARTY_GAME",
            isInstalled = false,
            difficulty = "MEDIUM"
        ),
        GameDto(
            id = "monopoly_express",
            name = "Monopoly Express",
            description = "Versión rápida del clásico juego de propiedades",
            minPlayers = 2,
            maxPlayers = 6,
            estimatedDurationMinutes = 45,
            category = "BOARD_GAME",
            isInstalled = false,
            difficulty = "HARD"
        )
    )
}