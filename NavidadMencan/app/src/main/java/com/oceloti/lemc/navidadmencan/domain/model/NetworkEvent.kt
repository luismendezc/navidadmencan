package com.oceloti.lemc.navidadmencan.domain.model

/**
 * Domain entity representing network events for LAN multiplayer communication
 */
sealed class NetworkEvent {
    data class PlayerJoined(val player: Player) : NetworkEvent()
    data class PlayerLeft(val playerId: String) : NetworkEvent()
    data class PlayerReadyChanged(val playerId: String, val isReady: Boolean) : NetworkEvent()
    data class GameStarted(val gameId: String) : NetworkEvent()
    data class GameEnded(val result: GameResult) : NetworkEvent()
    data class ChatMessage(val fromPlayerId: String, val message: String, val timestamp: Long) : NetworkEvent()
    data class LobbySettingsChanged(val settings: GameSettings) : NetworkEvent()
    data object ConnectionLost : NetworkEvent()
    data object ConnectionRestored : NetworkEvent()
}

data class GameResult(
    val gameId: String,
    val winners: List<String>, // Player IDs
    val scores: Map<String, Int> = emptyMap(), // Player ID to score
    val duration: Long, // Game duration in milliseconds
    val completedAt: Long = System.currentTimeMillis()
)