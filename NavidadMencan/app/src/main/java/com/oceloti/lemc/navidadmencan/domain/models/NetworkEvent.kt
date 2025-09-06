package com.oceloti.lemc.navidadmencan.domain.models

sealed class NetworkEvent {
    data class PlayerJoined(val player: Player, val lobbyId: String) : NetworkEvent()
    data class PlayerLeft(val playerId: String, val lobbyId: String) : NetworkEvent()
    data class LobbyCreated(val lobby: GameLobby) : NetworkEvent()
    data class LobbyUpdated(val lobby: GameLobby) : NetworkEvent()
    data class GameStarted(val lobbyId: String) : NetworkEvent()
    data class GameEnded(val lobbyId: String) : NetworkEvent()
    data class DeviceDiscovered(val deviceName: String, val ipAddress: String) : NetworkEvent()
    data class Error(val message: String, val throwable: Throwable? = null) : NetworkEvent()
}