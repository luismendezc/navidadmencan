package com.oceloti.lemc.games.impostor.domain.models

import java.util.Date

/**
 * Represents a complete game session of the impostor game
 * 
 * @param id Unique identifier for this game session
 * @param hostPlayer The player hosting this game
 * @param players All players in the game
 * @param gameConfig Configuration settings for this game
 * @param currentState Current state of the game
 * @param currentRound The round currently being played
 * @param rounds List of all completed and current rounds
 * @param statistics Game statistics for tracking
 * @param createdAt When this game was created
 * @param updatedAt When this game was last updated
 */
data class GameSession(
    val id: GameId,
    val hostPlayer: Player,
    val players: List<Player>,
    val gameConfig: GameConfig,
    val currentState: GameState,
    val currentRound: GameRound?,
    val rounds: List<GameRound>,
    val statistics: GameStatistics,
    val createdAt: Long,
    val updatedAt: Long
) {
    /**
     * Check if the game can be started
     */
    fun canStartGame(): Boolean {
        return players.size >= gameConfig.minPlayers &&
                currentState is GameState.WaitingForPlayers
    }
    
    /**
     * Get all players except the host
     */
    fun getNonHostPlayers(): List<Player> {
        return players.filter { !it.isHost }
    }
    
    /**
     * Get players who are still alive (have lives > 0)
     */
    fun getAlivePlayers(): List<Player> {
        return players.filter { it.lives > 0 }
    }
    
    /**
     * Get the current impostor if there is one
     */
    fun getCurrentImpostor(): Player? {
        return players.find { it.isImpostor }
    }
    
    /**
     * Check if the game should end
     */
    fun shouldGameEnd(): Boolean {
        val alivePlayers = getAlivePlayers()
        val aliveAllies = alivePlayers.filter { !it.isImpostor }
        val aliveImpostors = alivePlayers.filter { it.isImpostor }
        
        // Game ends if impostor is eliminated or all allies are eliminated
        return aliveImpostors.isEmpty() || aliveAllies.size <= 1
    }
    
    /**
     * Determine the winner of the game
     */
    fun determineWinner(): GameWinner? {
        if (!shouldGameEnd()) return null
        
        val alivePlayers = getAlivePlayers()
        val aliveAllies = alivePlayers.filter { !it.isImpostor }
        val aliveImpostors = alivePlayers.filter { it.isImpostor }
        
        return when {
            aliveImpostors.isEmpty() -> GameWinner.AlliesWin(aliveAllies.map { it.id })
            aliveAllies.size <= 1 -> {
                val impostor = aliveImpostors.first()
                GameWinner.ImpostorWins(impostor.id)
            }
            else -> null
        }
    }
}

/**
 * Represents a single round in the impostor game
 * 
 * @param roundNumber Sequential number of this round (starts at 1)
 * @param word The secret word for this round
 * @param category The category this word belongs to
 * @param impostor The player who is the impostor this round
 * @param drawings Map of player drawings for this round
 * @param votes Map of votes cast by players
 * @param impostorGuess The impostor's guess if they were voted out
 * @param result The final result of this round
 * @param startTime When this round started
 * @param endTime When this round ended (null if still ongoing)
 */
data class GameRound(
    val roundNumber: Int,
    val word: String,
    val category: WordCategory,
    val impostor: PlayerId,
    val drawings: Map<PlayerId, Drawing> = emptyMap(),
    val votes: Map<PlayerId, Vote> = emptyMap(),
    val impostorGuess: ImpostorGuess? = null,
    val result: RoundResult? = null,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null
) {
    /**
     * Check if all living players have voted
     */
    fun hasAllPlayersVoted(livingPlayers: List<Player>): Boolean {
        return votes.keys.containsAll(livingPlayers.map { it.id })
    }
    
    /**
     * Get the player with the most votes
     */
    fun getMostVotedPlayer(): PlayerId? {
        return votes.values
            .groupingBy { it.votedPlayerId }
            .eachCount()
            .maxByOrNull { it.value }?.key
    }
    
    /**
     * Check if this round is completed
     */
    fun isCompleted(): Boolean {
        return result != null && endTime != null
    }
}

/**
 * Statistics for tracking game performance
 */
data class GameStatistics(
    val playerId: PlayerId,
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val timesImpostor: Int,
    val timesImpostorWon: Int,
    val averageRoundsPerGame: Double,
    val favoriteCategories: List<WordCategoryId>,
    val lastPlayed: Long
) {
    /**
     * Calculate win rate as a percentage
     */
    fun getWinRate(): Double {
        return if (gamesPlayed > 0) {
            (gamesWon.toDouble() / gamesPlayed.toDouble()) * 100.0
        } else 0.0
    }
    
    /**
     * Calculate impostor win rate as a percentage
     */
    fun getImpostorWinRate(): Double {
        return if (timesImpostor > 0) {
            (timesImpostorWon.toDouble() / timesImpostor.toDouble()) * 100.0
        } else 0.0
    }
}