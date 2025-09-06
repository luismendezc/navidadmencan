package com.oceloti.lemc.navidadmencan.data.model

import com.oceloti.lemc.navidadmencan.domain.model.GameCategory
import com.oceloti.lemc.navidadmencan.domain.model.GameDifficulty

/**
 * Data Transfer Object for Game entity
 */
data class GameDto(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String? = null,
    val minPlayers: Int,
    val maxPlayers: Int,
    val estimatedDurationMinutes: Int,
    val category: String,
    val isInstalled: Boolean = false,
    val version: String = "1.0.0",
    val isEnabled: Boolean = true,
    val difficulty: String = "MEDIUM"
)