package com.oceloti.lemc.games.impostor.domain.models

import kotlinx.serialization.Serializable

/**
 * Represents a complete game session of the impostor game
 * 
 * @param id Unique identifier for this game session
 * @param gameConfig Configuration settings for this game
 * @param players All players in the game
 * @param currentState Current state of the game
 * @param currentRound Current round number
 * @param totalRounds Total number of rounds in this game
 * @param impostorId The ID of the player who is the impostor
 * @param secretWords All words available for this game session (6x4 grid)
 * @param selectedCategory The category of words being used
 * @param votes List of votes cast in the current round
 * @param drawings List of drawings made in the current round
 * @param createdAt When this game was created
 * @param updatedAt When this game was last updated
 */
@Serializable
data class GameSession(
    val id: GameId,
    val gameConfig: GameConfig,
    val players: List<Player>,
    val currentState: GameState,
    val currentRound: Int,
    val totalRounds: Int,
    val impostorId: PlayerId,
    val secretWords: List<String>,
    val selectedCategory: WordCategory,
    val votes: List<Vote> = emptyList(),
    val drawings: List<Drawing> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Check if the game can be started
     */
    fun canStartGame(): Boolean {
        return players.size >= 3 && // Minimum 3 players for impostor game
                currentState == GameState.WaitingForPlayers
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
     * Get the current impostor
     */
    fun getCurrentImpostor(): Player? {
        return players.find { it.id == impostorId }
    }
    
    /**
     * Check if the game should end
     */
    fun shouldGameEnd(): Boolean {
        val alivePlayers = getAlivePlayers()
        val aliveAllies = alivePlayers.filter { it.id != impostorId }
        val impostor = alivePlayers.find { it.id == impostorId }
        
        // Game ends if impostor is eliminated or all allies are eliminated
        return impostor == null || aliveAllies.size <= 1
    }
    
    /**
     * Determine the winner of the game
     */
    fun determineWinner(): GameWinner? {
        if (!shouldGameEnd()) return null
        
        val alivePlayers = getAlivePlayers()
        val aliveAllies = alivePlayers.filter { it.id != impostorId }
        val impostor = alivePlayers.find { it.id == impostorId }
        
        return when {
            impostor == null -> GameWinner.AlliesWin(aliveAllies.map { it.id })
            aliveAllies.size <= 1 -> GameWinner.ImpostorWins(impostorId)
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