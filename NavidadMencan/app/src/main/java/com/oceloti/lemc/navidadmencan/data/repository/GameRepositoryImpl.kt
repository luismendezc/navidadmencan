package com.oceloti.lemc.navidadmencan.data.repository

import com.oceloti.lemc.navidadmencan.core.util.AppLog.logD
import com.oceloti.lemc.navidadmencan.core.util.AppLog.logE
import com.oceloti.lemc.navidadmencan.data.datasource.GameDataSource
import com.oceloti.lemc.navidadmencan.data.mapper.GameMapper.toDomain
import com.oceloti.lemc.navidadmencan.domain.model.Game
import com.oceloti.lemc.navidadmencan.domain.model.GameCategory
import com.oceloti.lemc.navidadmencan.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of GameRepository using local data source
 */
class GameRepositoryImpl(
    private val gameDataSource: GameDataSource
) : GameRepository {
    
    override suspend fun getAllGames(): Flow<List<Game>> {
        logD("GameRepositoryImpl", "Getting all games")
        return gameDataSource.getAllGames().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }
    
    override suspend fun getGamesByCategory(category: GameCategory): Flow<List<Game>> {
        logD("GameRepositoryImpl", "Getting games by category: $category")
        return gameDataSource.getAllGames().map { dtoList ->
            dtoList.filter { it.category == category.name }
                .map { it.toDomain() }
        }
    }
    
    override suspend fun getGameById(gameId: String): Game? {
        logD("GameRepositoryImpl", "Getting game by ID: $gameId")
        return try {
            gameDataSource.getGameById(gameId)?.toDomain()
        } catch (e: Exception) {
            logE("GameRepositoryImpl", "Error getting game by ID: $gameId", e)
            null
        }
    }
    
    override suspend fun getInstalledGames(): Flow<List<Game>> {
        logD("GameRepositoryImpl", "Getting installed games")
        return gameDataSource.getAllGames().map { dtoList ->
            dtoList.filter { it.isInstalled }
                .map { it.toDomain() }
        }
    }
    
    override suspend fun searchGames(query: String): Flow<List<Game>> {
        logD("GameRepositoryImpl", "Searching games with query: $query")
        return gameDataSource.searchGames(query).map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }
    
    override suspend fun installGame(gameId: String): Result<Unit> {
        logD("GameRepositoryImpl", "Installing game: $gameId")
        return try {
            // Check if game exists
            if (!gameDataSource.gameExists(gameId)) {
                return Result.failure(IllegalArgumentException("Game not found: $gameId"))
            }
            
            // Update installation status
            gameDataSource.updateGameInstallationStatus(gameId, true)
        } catch (e: Exception) {
            logE("GameRepositoryImpl", "Error installing game: $gameId", e)
            Result.failure(e)
        }
    }
    
    override suspend fun uninstallGame(gameId: String): Result<Unit> {
        logD("GameRepositoryImpl", "Uninstalling game: $gameId")
        return try {
            // Check if game exists
            if (!gameDataSource.gameExists(gameId)) {
                return Result.failure(IllegalArgumentException("Game not found: $gameId"))
            }
            
            // Update installation status
            gameDataSource.updateGameInstallationStatus(gameId, false)
        } catch (e: Exception) {
            logE("GameRepositoryImpl", "Error uninstalling game: $gameId", e)
            Result.failure(e)
        }
    }
    
    override suspend fun isGameCompatible(gameId: String): Boolean {
        logD("GameRepositoryImpl", "Checking compatibility for game: $gameId")
        // For now, assume all games are compatible
        // In a real implementation, this would check device capabilities, Android version, etc.
        return gameDataSource.gameExists(gameId)
    }
    
    override suspend fun getRecommendedGames(playerCount: Int): Flow<List<Game>> {
        logD("GameRepositoryImpl", "Getting recommended games for $playerCount players")
        return gameDataSource.getAllGames().map { dtoList ->
            dtoList.filter { game ->
                playerCount >= game.minPlayers && playerCount <= game.maxPlayers
            }
            .map { it.toDomain() }
            .sortedBy { it.estimatedDurationMinutes } // Sort by duration for better recommendations
        }
    }
}