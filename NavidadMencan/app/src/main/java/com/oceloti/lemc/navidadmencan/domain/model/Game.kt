package com.oceloti.lemc.navidadmencan.domain.model

/**
 * Domain entity representing a game in the hub
 */
data class Game(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String? = null,
    val minPlayers: Int,
    val maxPlayers: Int,
    val estimatedDurationMinutes: Int,
    val category: GameCategory,
    val isInstalled: Boolean = false,
    val version: String = "1.0.0",
    val isEnabled: Boolean = true,
    val difficulty: GameDifficulty = GameDifficulty.MEDIUM
)

enum class GameCategory {
    CARD_GAME,
    BOARD_GAME,
    TRIVIA,
    PARTY_GAME,
    STRATEGY,
    ACTION,
    PUZZLE
}

enum class GameDifficulty {
    EASY,
    MEDIUM,
    HARD
}