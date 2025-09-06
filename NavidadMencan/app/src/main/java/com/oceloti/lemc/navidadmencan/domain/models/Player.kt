package com.oceloti.lemc.navidadmencan.domain.models

data class Player(
    val id: String,
    val name: String,
    val deviceName: String,
    val ipAddress: String,
    val isHost: Boolean = false,
    val isConnected: Boolean = true,
    val avatarUrl: String? = null
)