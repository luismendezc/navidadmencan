package com.oceloti.lemc.navidadmencan.domain.models

import java.util.Date

data class GameLobby(
    val id: String,
    val gameId: String,
    val gameName: String,
    val hostPlayer: Player,
    val players: List<Player>,
    val maxPlayers: Int,
    val isPrivate: Boolean = false,
    val password: String? = null,
    val status: LobbyStatus = LobbyStatus.WAITING,
    val createdAt: Date = Date()
)

enum class LobbyStatus {
    WAITING,
    FULL,
    IN_GAME,
    FINISHED,
    CANCELLED
}