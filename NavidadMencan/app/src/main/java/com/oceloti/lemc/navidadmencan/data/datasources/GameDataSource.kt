package com.oceloti.lemc.navidadmencan.data.datasources

import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.data.models.GameDto
import kotlinx.coroutines.flow.Flow

interface GameDataSource {
    fun getAllGames(): Flow<Result<List<GameDto>>>
    suspend fun getGameById(gameId: String): Result<GameDto?>
    suspend fun updateGameInstallationStatus(gameId: String, isInstalled: Boolean): Result<Unit>
}