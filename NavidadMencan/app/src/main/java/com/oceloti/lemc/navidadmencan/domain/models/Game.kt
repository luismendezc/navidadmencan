package com.oceloti.lemc.navidadmencan.domain.models

data class Game(
    val id: String,
    val name: String,
    val description: String,
    val category: GameCategory,
    val minPlayers: Int,
    val maxPlayers: Int,
    val estimatedDuration: Int, // in minutes
    val isInstalled: Boolean = false,
    val iconUrl: String? = null,
    val difficulty: GameDifficulty = GameDifficulty.EASY
)

enum class GameCategory {
    CARD,
    BOARD,
    TRIVIA,
    PARTY,
    STRATEGY,
    PUZZLE
}

enum class GameDifficulty {
    EASY,
    MEDIUM,
    HARD
}