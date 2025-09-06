package com.oceloti.lemc.navidadmencan.data.repositories

import com.oceloti.lemc.navidadmencan.core.util.AppLog
import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.data.datasources.GameDataSource
import com.oceloti.lemc.navidadmencan.data.mappers.GameMapper
import com.oceloti.lemc.navidadmencan.domain.models.Game
import com.oceloti.lemc.navidadmencan.domain.models.GameCategory
import com.oceloti.lemc.navidadmencan.domain.repositories.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepositoryImpl(
    private val gameDataSource: GameDataSource
) : GameRepository {
    
    override fun getAvailableGames(): Flow<Result<List<Game>>> {
        AppLog.d("GameRepositoryImpl", "Getting available games")
        return gameDataSource.getAllGames().map { result ->
            when (result) {
                is Result.Success -> {
                    val games = GameMapper.toDomainList(result.data)
                    AppLog.d("GameRepositoryImpl", "Mapped ${games.size} games to domain")
                    Result.Success(games)
                }
                is Result.Error -> {
                    AppLog.e("GameRepositoryImpl", "Error getting games", result.exception)
                    result
                }
                is Result.Loading -> result
            }
        }
    }
    
    override fun getGamesByCategory(category: GameCategory): Flow<Result<List<Game>>> {
        AppLog.d("GameRepositoryImpl", "Getting games by category: $category")
        return getAvailableGames().map { result ->
            when (result) {
                is Result.Success -> {
                    val filteredGames = result.data.filter { it.category == category }
                    AppLog.d("GameRepositoryImpl", "Found ${filteredGames.size} games in category $category")
                    Result.Success(filteredGames)
                }
                is Result.Error -> result
                is Result.Loading -> result
            }
        }
    }
    
    override suspend fun getGameById(gameId: String): Result<Game> {
        AppLog.d("GameRepositoryImpl", "Getting game by id: $gameId")
        return when (val result = gameDataSource.getGameById(gameId)) {
            is Result.Success -> {
                result.data?.let { gameDto ->
                    val game = GameMapper.toDomain(gameDto)
                    AppLog.d("GameRepositoryImpl", "Found game: ${game.name}")
                    Result.Success(game)
                } ?: run {
                    val error = IllegalArgumentException("Game with id $gameId not found")
                    AppLog.e("GameRepositoryImpl", "Game not found: $gameId", error)
                    Result.Error(error)
                }
            }
            is Result.Error -> {
                AppLog.e("GameRepositoryImpl", "Error getting game by id: $gameId", result.exception)
                result
            }
            is Result.Loading -> result
        }
    }
    
    override suspend fun installGame(gameId: String): Result<Unit> {
        AppLog.d("GameRepositoryImpl", "Installing game: $gameId")
        return gameDataSource.updateGameInstallationStatus(gameId, true)
    }
    
    override suspend fun uninstallGame(gameId: String): Result<Unit> {
        AppLog.d("GameRepositoryImpl", "Uninstalling game: $gameId")
        return gameDataSource.updateGameInstallationStatus(gameId, false)
    }
    
    override suspend fun updateGameInstallationStatus(gameId: String, isInstalled: Boolean): Result<Unit> {
        AppLog.d("GameRepositoryImpl", "Updating installation status for game $gameId to $isInstalled")
        return gameDataSource.updateGameInstallationStatus(gameId, isInstalled)
    }
}