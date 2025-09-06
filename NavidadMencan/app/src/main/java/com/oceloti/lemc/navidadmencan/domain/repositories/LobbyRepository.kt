package com.oceloti.lemc.navidadmencan.domain.repositories

import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.domain.models.GameLobby
import com.oceloti.lemc.navidadmencan.domain.models.Player
import kotlinx.coroutines.flow.Flow

interface LobbyRepository {
    fun getAvailableLobbies(): Flow<Result<List<GameLobby>>>
    suspend fun createLobby(gameId: String, hostPlayer: Player, isPrivate: Boolean = false, password: String? = null): Result<GameLobby>
    suspend fun joinLobby(lobbyId: String, player: Player, password: String? = null): Result<GameLobby>
    suspend fun leaveLobby(lobbyId: String, playerId: String): Result<Unit>
    suspend fun startGame(lobbyId: String): Result<Unit>
    suspend fun closeLobby(lobbyId: String): Result<Unit>
    fun getLobbyById(lobbyId: String): Flow<Result<GameLobby?>>
}