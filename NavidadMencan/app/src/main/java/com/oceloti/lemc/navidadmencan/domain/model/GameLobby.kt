package com.oceloti.lemc.navidadmencan.domain.model

/**
 * Domain entity representing a game lobby where players gather before starting a game
 */
data class GameLobby(
    val id: String,
    val game: Game,
    val host: Player,
    val players: List<Player> = emptyList(),
    val status: LobbyStatus = LobbyStatus.WAITING_FOR_PLAYERS,
    val maxPlayers: Int = game.maxPlayers,
    val isPrivate: Boolean = false,
    val passcode: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val settings: GameSettings = GameSettings()
) {
    val currentPlayerCount: Int
        get() = players.size + 1 // +1 for host
    
    val canStartGame: Boolean
        get() = currentPlayerCount >= game.minPlayers && 
               status == LobbyStatus.WAITING_FOR_PLAYERS &&
               players.all { it.isReady }
}

enum class LobbyStatus {
    WAITING_FOR_PLAYERS,
    STARTING_GAME,
    IN_GAME,
    GAME_FINISHED,
    CANCELLED
}

data class GameSettings(
    val enableChat: Boolean = true,
    val allowSpectators: Boolean = false,
    val customRules: Map<String, Any> = emptyMap()
)