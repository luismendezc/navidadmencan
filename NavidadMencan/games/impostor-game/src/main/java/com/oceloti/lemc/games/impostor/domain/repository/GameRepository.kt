package com.oceloti.lemc.games.impostor.domain.repository

import com.oceloti.lemc.games.impostor.domain.models.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing game sessions and game state
 */
interface GameRepository {
    /**
     * Create a new game session
     */
    suspend fun createGameSession(config: GameConfig, hostPlayer: Player): Result<GameSession>
    
    /**
     * Get a game session by ID
     */
    suspend fun getGameSession(gameId: GameId): GameSession?
    
    /**
     * Update a game session
     */
    suspend fun updateGameSession(session: GameSession): Result<Unit>
    
    /**
     * Add a player to a game session
     */
    suspend fun addPlayer(gameId: GameId, player: Player): Result<GameSession>
    
    /**
     * Remove a player from a game session
     */
    suspend fun removePlayer(gameId: GameId, playerId: PlayerId): Result<GameSession>
    
    /**
     * Update game state
     */
    suspend fun updateGameState(gameId: GameId, newState: GameState): Result<Unit>
    
    /**
     * Submit a vote
     */
    suspend fun submitVote(gameId: GameId, vote: Vote): Result<Unit>
    
    /**
     * Submit a drawing
     */
    suspend fun submitDrawing(gameId: GameId, drawing: Drawing): Result<Unit>
    
    /**
     * Submit impostor guess
     */
    suspend fun submitImpostorGuess(gameId: GameId, guess: ImpostorGuess): Result<Unit>
    
    /**
     * Observe game session changes
     */
    fun observeGameSession(gameId: GameId): Flow<GameSession?>
    
    /**
     * Get game statistics for a player
     */
    suspend fun getPlayerStatistics(playerId: PlayerId): GameStatistics?
    
    /**
     * Update game statistics after a game ends
     */
    suspend fun updatePlayerStatistics(playerId: PlayerId, gameResult: GameWinner): Result<Unit>
}

/**
 * In-memory implementation of GameRepository
 * In a real app, this would be backed by Room database or remote API
 */
class InMemoryGameRepository : GameRepository {
    
    private val gameSessions = mutableMapOf<GameId, GameSession>()
    private val playerStats = mutableMapOf<PlayerId, GameStatistics>()
    
    override suspend fun createGameSession(config: GameConfig, hostPlayer: Player): Result<GameSession> {
        return try {
            val gameId = GameId(generateGameId())
            val session = GameSession(
                id = gameId,
                gameConfig = config,
                players = listOf(hostPlayer),
                currentState = GameState.WaitingForPlayers,
                currentRound = 1,
                totalRounds = 1,
                impostorId = hostPlayer.id, // Temporary, will be randomized when game starts
                secretWords = emptyList(),
                selectedCategory = WordCategory.getSampleCategories().first() // Temporary
            )
            
            gameSessions[gameId] = session
            Result.success(session)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getGameSession(gameId: GameId): GameSession? {
        return gameSessions[gameId]
    }
    
    override suspend fun updateGameSession(session: GameSession): Result<Unit> {
        return try {
            gameSessions[session.id] = session.copy(updatedAt = System.currentTimeMillis())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun addPlayer(gameId: GameId, player: Player): Result<GameSession> {
        return try {
            val session = gameSessions[gameId] 
                ?: return Result.failure(IllegalArgumentException("Game not found"))
            
            if (session.players.size >= session.gameConfig.maxPlayers) {
                return Result.failure(IllegalStateException("Game is full"))
            }
            
            val updatedSession = session.copy(
                players = session.players + player,
                updatedAt = System.currentTimeMillis()
            )
            
            gameSessions[gameId] = updatedSession
            Result.success(updatedSession)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun removePlayer(gameId: GameId, playerId: PlayerId): Result<GameSession> {
        return try {
            val session = gameSessions[gameId] 
                ?: return Result.failure(IllegalArgumentException("Game not found"))
            
            val updatedSession = session.copy(
                players = session.players.filterNot { it.id == playerId },
                updatedAt = System.currentTimeMillis()
            )
            
            gameSessions[gameId] = updatedSession
            Result.success(updatedSession)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateGameState(gameId: GameId, newState: GameState): Result<Unit> {
        return try {
            val session = gameSessions[gameId] 
                ?: return Result.failure(IllegalArgumentException("Game not found"))
            
            val updatedSession = session.copy(
                currentState = newState,
                updatedAt = System.currentTimeMillis()
            )
            
            gameSessions[gameId] = updatedSession
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun submitVote(gameId: GameId, vote: Vote): Result<Unit> {
        return try {
            val session = gameSessions[gameId] 
                ?: return Result.failure(IllegalArgumentException("Game not found"))
            
            val updatedSession = session.copy(
                votes = session.votes + vote,
                updatedAt = System.currentTimeMillis()
            )
            
            gameSessions[gameId] = updatedSession
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun submitDrawing(gameId: GameId, drawing: Drawing): Result<Unit> {
        return try {
            val session = gameSessions[gameId] 
                ?: return Result.failure(IllegalArgumentException("Game not found"))
            
            val updatedSession = session.copy(
                drawings = session.drawings + drawing,
                updatedAt = System.currentTimeMillis()
            )
            
            gameSessions[gameId] = updatedSession
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun submitImpostorGuess(gameId: GameId, guess: ImpostorGuess): Result<Unit> {
        // Implementation would depend on having ImpostorGuess model defined
        return Result.success(Unit)
    }
    
    override fun observeGameSession(gameId: GameId): Flow<GameSession?> {
        // In a real implementation, this would use Room's observe functionality
        // or WebSocket/EventSource for remote data
        TODO("Implement with proper reactive data source")
    }
    
    override suspend fun getPlayerStatistics(playerId: PlayerId): GameStatistics? {
        return playerStats[playerId]
    }
    
    override suspend fun updatePlayerStatistics(playerId: PlayerId, gameResult: GameWinner): Result<Unit> {
        // Implementation would update player statistics based on game result
        return Result.success(Unit)
    }
    
    private fun generateGameId(): String {
        return (1..8).map { ('A'..'Z').random() }.joinToString("")
    }
}