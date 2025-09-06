package com.oceloti.lemc.navidadmencan.data.datasource

import com.oceloti.lemc.navidadmencan.data.model.GameDto
import kotlinx.coroutines.flow.Flow

/**
 * Data source interface for game data operations
 */
interface GameDataSource {
    
    /**
     * Get all games from data source
     */
    suspend fun getAllGames(): Flow<List<GameDto>>
    
    /**
     * Get a specific game by ID
     */
    suspend fun getGameById(gameId: String): GameDto?
    
    /**
     * Update game installation status
     */
    suspend fun updateGameInstallationStatus(gameId: String, isInstalled: Boolean): Result<Unit>
    
    /**
     * Search games by query
     */
    suspend fun searchGames(query: String): Flow<List<GameDto>>
    
    /**
     * Check if game exists
     */
    suspend fun gameExists(gameId: String): Boolean
}