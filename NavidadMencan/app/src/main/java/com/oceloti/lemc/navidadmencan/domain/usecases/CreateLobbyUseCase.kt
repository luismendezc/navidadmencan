package com.oceloti.lemc.navidadmencan.domain.usecases

import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.domain.models.GameLobby
import com.oceloti.lemc.navidadmencan.domain.models.Player
import com.oceloti.lemc.navidadmencan.domain.repositories.LobbyRepository

class CreateLobbyUseCase(
    private val lobbyRepository: LobbyRepository
) {
    suspend operator fun invoke(
        gameId: String,
        hostPlayer: Player,
        isPrivate: Boolean = false,
        password: String? = null
    ): Result<GameLobby> {
        return lobbyRepository.createLobby(gameId, hostPlayer, isPrivate, password)
    }
}