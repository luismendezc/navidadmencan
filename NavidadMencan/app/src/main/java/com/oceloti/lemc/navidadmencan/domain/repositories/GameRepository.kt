package com.oceloti.lemc.navidadmencan.domain.repositories

import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.domain.models.Game
import com.oceloti.lemc.navidadmencan.domain.models.GameCategory
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getAvailableGames(): Flow<Result<List<Game>>>
    fun getGamesByCategory(category: GameCategory): Flow<Result<List<Game>>>
    suspend fun getGameById(gameId: String): Result<Game>
    suspend fun installGame(gameId: String): Result<Unit>
    suspend fun uninstallGame(gameId: String): Result<Unit>
    suspend fun updateGameInstallationStatus(gameId: String, isInstalled: Boolean): Result<Unit>
}