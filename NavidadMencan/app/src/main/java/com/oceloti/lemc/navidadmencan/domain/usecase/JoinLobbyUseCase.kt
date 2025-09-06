package com.oceloti.lemc.navidadmencan.domain.usecase

import com.oceloti.lemc.navidadmencan.domain.model.GameLobby
import com.oceloti.lemc.navidadmencan.domain.model.Player
import com.oceloti.lemc.navidadmencan.domain.repository.LobbyRepository
import com.oceloti.lemc.navidadmencan.domain.repository.NetworkRepository

/**
 * Use case to join an existing game lobby
 */
class JoinLobbyUseCase(
    private val lobbyRepository: LobbyRepository,
    private val networkRepository: NetworkRepository
) {
    
    suspend operator fun invoke(
        lobbyId: String,
        player: Player,
        passcode: String? = null
    ): Result<GameLobby> {
        // Get lobby information first
        val lobby = lobbyRepository.getLobby(lobbyId)
            ?: return Result.failure(IllegalArgumentException("Lobby not found: $lobbyId"))
        
        // Validate lobby can accept new players
        if (lobby.currentPlayerCount >= lobby.maxPlayers) {
            return Result.failure(IllegalStateException("Lobby is full"))
        }
        
        // Check if lobby requires passcode
        if (lobby.isPrivate && lobby.passcode != passcode) {
            return Result.failure(IllegalArgumentException("Invalid passcode"))
        }
        
        // Connect to the host if not already connected
        if (!networkRepository.isConnected()) {
            val hostDevice = lobby.host.deviceInfo
                ?: return Result.failure(IllegalStateException("Host device information not available"))
            
            val connectionResult = networkRepository.connectToHost(hostDevice)
            if (connectionResult.isFailure) {
                return Result.failure(
                    Exception("Failed to connect to host", connectionResult.exceptionOrNull())
                )
            }
        }
        
        // Join the lobby
        return lobbyRepository.joinLobby(
            lobbyId = lobbyId,
            player = player,
            passcode = passcode
        )
    }
}