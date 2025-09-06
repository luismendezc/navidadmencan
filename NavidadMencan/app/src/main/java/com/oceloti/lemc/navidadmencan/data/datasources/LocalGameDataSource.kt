package com.oceloti.lemc.navidadmencan.data.datasources

import com.oceloti.lemc.navidadmencan.core.util.AppLog
import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.data.models.GameDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalGameDataSource : GameDataSource {
    
    private val sampleGames = mutableListOf(
        GameDto(
            id = "impostor_game",
            name = "¿Quién es el impostor?",
            description = "Un jugador es el impostor y debe adivinar la palabra secreta mientras los demás intentan descubrirlo",
            category = "PARTY",
            minPlayers = 3,
            maxPlayers = 12,
            estimatedDuration = 15,
            isInstalled = true,
            difficulty = "EASY"
        ),
        GameDto(
            id = "uno",
            name = "UNO Navideño",
            description = "El clásico juego de cartas UNO con temática navideña",
            category = "CARD",
            minPlayers = 2,
            maxPlayers = 10,
            estimatedDuration = 30,
            isInstalled = true,
            difficulty = "EASY"
        ),
        GameDto(
            id = "trivia_navidena",
            name = "Trivia Navideña",
            description = "Preguntas y respuestas sobre la Navidad y tradiciones familiares",
            category = "TRIVIA",
            minPlayers = 2,
            maxPlayers = 8,
            estimatedDuration = 45,
            isInstalled = true,
            difficulty = "MEDIUM"
        ),
        GameDto(
            id = "charadas_familiares",
            name = "Charadas Familiares",
            description = "Adivina películas, canciones y tradiciones navideñas actuando",
            category = "PARTY",
            minPlayers = 4,
            maxPlayers = 12,
            estimatedDuration = 60,
            isInstalled = false,
            difficulty = "EASY"
        ),
        GameDto(
            id = "pictionary",
            name = "Pictionary Navideño",
            description = "Dibuja y adivina conceptos navideños",
            category = "PARTY",
            minPlayers = 4,
            maxPlayers = 8,
            estimatedDuration = 40,
            isInstalled = false,
            difficulty = "EASY"
        ),
        GameDto(
            id = "monopoly_express",
            name = "Monopoly Express",
            description = "Versión rápida del clásico juego de mesa",
            category = "BOARD",
            minPlayers = 2,
            maxPlayers = 6,
            estimatedDuration = 90,
            isInstalled = false,
            difficulty = "MEDIUM"
        )
    )
    
    override fun getAllGames(): Flow<Result<List<GameDto>>> = flow {
        try {
            emit(Result.Loading)
            delay(500) // Simulate network delay
            AppLog.d("LocalGameDataSource", "Fetching ${sampleGames.size} games")
            emit(Result.Success(sampleGames))
        } catch (e: Exception) {
            AppLog.e("LocalGameDataSource", "Error fetching games", e)
            emit(Result.Error(e))
        }
    }
    
    override suspend fun getGameById(gameId: String): Result<GameDto?> {
        return try {
            delay(100) // Simulate network delay
            val game = sampleGames.find { it.id == gameId }
            AppLog.d("LocalGameDataSource", "Fetching game with id: $gameId, found: ${game != null}")
            Result.Success(game)
        } catch (e: Exception) {
            AppLog.e("LocalGameDataSource", "Error fetching game with id: $gameId", e)
            Result.Error(e)
        }
    }
    
    override suspend fun updateGameInstallationStatus(gameId: String, isInstalled: Boolean): Result<Unit> {
        return try {
            val gameIndex = sampleGames.indexOfFirst { it.id == gameId }
            if (gameIndex != -1) {
                sampleGames[gameIndex] = sampleGames[gameIndex].copy(isInstalled = isInstalled)
                AppLog.d("LocalGameDataSource", "Updated game $gameId installation status to $isInstalled")
                Result.Success(Unit)
            } else {
                val error = IllegalArgumentException("Game with id $gameId not found")
                AppLog.e("LocalGameDataSource", "Game not found: $gameId", error)
                Result.Error(error)
            }
        } catch (e: Exception) {
            AppLog.e("LocalGameDataSource", "Error updating installation status for game: $gameId", e)
            Result.Error(e)
        }
    }
}