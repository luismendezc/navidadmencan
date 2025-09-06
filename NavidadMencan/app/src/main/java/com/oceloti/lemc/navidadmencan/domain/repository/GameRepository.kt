package com.oceloti.lemc.navidadmencan.domain.repository

import com.oceloti.lemc.navidadmencan.domain.model.Game
import com.oceloti.lemc.navidadmencan.domain.model.GameCategory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for game-related operations
 */
interface GameRepository {
    
    /**
     * Get all available games
     */
    suspend fun getAllGames(): Flow<List<Game>>
    
    /**
     * Get games by category
     */
    suspend fun getGamesByCategory(category: GameCategory): Flow<List<Game>>
    
    /**
     * Get a specific game by ID
     */
    suspend fun getGameById(gameId: String): Game?
    
    /**
     * Get installed games only
     */
    suspend fun getInstalledGames(): Flow<List<Game>>
    
    /**
     * Search games by name or description
     */
    suspend fun searchGames(query: String): Flow<List<Game>>
    
    /**
     * Install a game (mark as installed)
     */
    suspend fun installGame(gameId: String): Result<Unit>
    
    /**
     * Uninstall a game (mark as not installed)
     */
    suspend fun uninstallGame(gameId: String): Result<Unit>
    
    /**
     * Check if game is compatible with current device
     */
    suspend fun isGameCompatible(gameId: String): Boolean
    
    /**
     * Get recommended games based on player count
     */
    suspend fun getRecommendedGames(playerCount: Int): Flow<List<Game>>
}