package com.oceloti.lemc.navidadmencan.data.models

data class GameDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val estimatedDuration: Int,
    val isInstalled: Boolean = false,
    val iconUrl: String? = null,
    val difficulty: String = "EASY"
)