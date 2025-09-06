package com.oceloti.lemc.navidadmencan.domain.repository

import com.oceloti.lemc.navidadmencan.domain.model.GameLobby
import com.oceloti.lemc.navidadmencan.domain.model.GameSettings
import com.oceloti.lemc.navidadmencan.domain.model.Player
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for lobby-related operations
 */
interface LobbyRepository {
    
    /**
     * Create a new lobby
     */
    suspend fun createLobby(
        gameId: String,
        host: Player,
        isPrivate: Boolean = false,
        passcode: String? = null,
        settings: GameSettings = GameSettings()
    ): Result<GameLobby>
    
    /**
     * Join an existing lobby
     */
    suspend fun joinLobby(lobbyId: String, player: Player, passcode: String? = null): Result<GameLobby>
    
    /**
     * Leave a lobby
     */
    suspend fun leaveLobby(lobbyId: String, playerId: String): Result<Unit>
    
    /**
     * Get current lobby state
     */
    suspend fun getLobby(lobbyId: String): GameLobby?
    
    /**
     * Observe lobby changes
     */
    suspend fun observeLobby(lobbyId: String): Flow<GameLobby>
    
    /**
     * Get all available lobbies
     */
    suspend fun getAvailableLobbies(): Flow<List<GameLobby>>
    
    /**
     * Update player ready status
     */
    suspend fun updatePlayerReadyStatus(
        lobbyId: String,
        playerId: String,
        isReady: Boolean
    ): Result<Unit>
    
    /**
     * Update lobby settings (host only)
     */
    suspend fun updateLobbySettings(
        lobbyId: String,
        settings: GameSettings,
        hostId: String
    ): Result<Unit>
    
    /**
     * Start the game (host only)
     */
    suspend fun startGame(lobbyId: String, hostId: String): Result<Unit>
    
    /**
     * Cancel/close the lobby (host only)
     */
    suspend fun closeLobby(lobbyId: String, hostId: String): Result<Unit>
}