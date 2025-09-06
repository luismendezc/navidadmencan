package com.oceloti.lemc.games.impostor.domain.models

/**
 * Represents a player in the impostor game
 * 
 * @param id Unique identifier for the player
 * @param name Display name of the player
 * @param isHost Whether this player is the game host
 * @param isImpostor Whether this player is the impostor for current round
 * @param lives Number of lives remaining
 * @param isConnected Connection status
 * @param connectionType How this player connects to the game
 * @param deviceInfo Information about the player's device
 */
data class Player(
    val id: PlayerId,
    val name: String,
    val isHost: Boolean = false,
    val isImpostor: Boolean = false,
    val lives: Int,
    val isConnected: Boolean = true,
    val connectionType: ConnectionType,
    val deviceInfo: DeviceInfo
)

/**
 * Device information for cross-platform compatibility
 */
data class DeviceInfo(
    val platform: Platform,
    val deviceName: String,
    val version: String
)

/**
 * Supported platforms for cross-platform play
 */
enum class Platform {
    ANDROID,
    IOS
}

/**
 * Connection types for players
 */
enum class ConnectionType {
    HOST,        // This player is hosting the game
    BLUETOOTH,   // Connected via Bluetooth
    LAN         // Connected via LAN/WiFi
}