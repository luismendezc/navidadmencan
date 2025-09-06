package com.oceloti.lemc.navidadmencan.domain.model

/**
 * Domain entity representing a player in the game hub
 */
data class Player(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val isHost: Boolean = false,
    val isReady: Boolean = false,
    val deviceInfo: DeviceInfo? = null,
    val joinedAt: Long = System.currentTimeMillis()
)

data class DeviceInfo(
    val deviceId: String,
    val deviceName: String,
    val ipAddress: String,
    val port: Int = 8080
)