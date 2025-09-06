package com.oceloti.lemc.navidadmencan.domain.usecase

import com.oceloti.lemc.navidadmencan.domain.model.GameLobby
import com.oceloti.lemc.navidadmencan.domain.model.GameSettings
import com.oceloti.lemc.navidadmencan.domain.model.Player
import com.oceloti.lemc.navidadmencan.domain.repository.GameRepository
import com.oceloti.lemc.navidadmencan.domain.repository.LobbyRepository
import com.oceloti.lemc.navidadmencan.domain.repository.NetworkRepository

/**
 * Use case to create a new game lobby
 */
class CreateLobbyUseCase(
    private val lobbyRepository: LobbyRepository,
    private val gameRepository: GameRepository,
    private val networkRepository: NetworkRepository
) {
    
    suspend operator fun invoke(
        gameId: String,
        host: Player,
        isPrivate: Boolean = false,
        passcode: String? = null,
        settings: GameSettings = GameSettings()
    ): Result<GameLobby> {
        // Validate that the game exists and is available
        val game = gameRepository.getGameById(gameId)
            ?: return Result.failure(IllegalArgumentException("Game not found: $gameId"))
        
        if (!game.isEnabled) {
            return Result.failure(IllegalStateException("Game is currently disabled: ${game.name}"))
        }
        
        if (!game.isInstalled) {
            return Result.failure(IllegalStateException("Game is not installed: ${game.name}"))
        }
        
        // Start hosting network service if not already hosting
        if (!networkRepository.isHosting()) {
            val hostResult = networkRepository.startHosting()
            if (hostResult.isFailure) {
                return Result.failure(
                    Exception("Failed to start network hosting", hostResult.exceptionOrNull())
                )
            }
        }
        
        // Create the lobby
        return lobbyRepository.createLobby(
            gameId = gameId,
            host = host,
            isPrivate = isPrivate,
            passcode = passcode,
            settings = settings
        )
    }
}