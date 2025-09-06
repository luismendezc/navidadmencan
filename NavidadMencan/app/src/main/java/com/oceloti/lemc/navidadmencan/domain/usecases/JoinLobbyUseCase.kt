package com.oceloti.lemc.navidadmencan.domain.usecases

import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.domain.models.GameLobby
import com.oceloti.lemc.navidadmencan.domain.models.Player
import com.oceloti.lemc.navidadmencan.domain.repositories.LobbyRepository

class JoinLobbyUseCase(
    private val lobbyRepository: LobbyRepository
) {
    suspend operator fun invoke(
        lobbyId: String,
        player: Player,
        password: String? = null
    ): Result<GameLobby> {
        return lobbyRepository.joinLobby(lobbyId, player, password)
    }
}