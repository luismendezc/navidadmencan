package com.oceloti.lemc.games.impostor.domain.models

/**
 * Represents a vote cast by a player during the voting phase
 * 
 * @param voterId The player who cast this vote
 * @param votedPlayerId The player being voted for as the impostor
 * @param timestamp When this vote was cast
 */
data class Vote(
    val voterId: PlayerId,
    val votedPlayerId: PlayerId,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Check if this is a self-vote (voting for yourself)
     */
    fun isSelfVote(): Boolean = voterId == votedPlayerId
}

/**
 * Represents the impostor's guess when they have been voted out
 * 
 * @param playerId The impostor making the guess
 * @param guessedWord The word they think is the secret word
 * @param isCorrect Whether their guess was correct
 * @param timestamp When this guess was made
 */
data class ImpostorGuess(
    val playerId: PlayerId,
    val guessedWord: String,
    val isCorrect: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Normalize the guessed word for comparison (lowercase, trimmed)
     */
    fun getNormalizedGuess(): String = guessedWord.lowercase().trim()
}

/**
 * Utility class for vote counting and analysis
 */
object VoteAnalyzer {
    /**
     * Count votes for each player
     * 
     * @param votes Map of votes cast
     * @return Map of player ID to vote count
     */
    fun countVotes(votes: Map<PlayerId, Vote>): Map<PlayerId, Int> {
        return votes.values
            .groupingBy { it.votedPlayerId }
            .eachCount()
    }
    
    /**
     * Find the player with the most votes
     * 
     * @param votes Map of votes cast
     * @return Player ID with most votes, or null if no votes or tie
     */
    fun getMostVotedPlayer(votes: Map<PlayerId, Vote>): PlayerId? {
        val voteCounts = countVotes(votes)
        val maxVotes = voteCounts.values.maxOrNull() ?: return null
        
        // Check for tie
        val playersWithMaxVotes = voteCounts.filter { it.value == maxVotes }
        return if (playersWithMaxVotes.size == 1) {
            playersWithMaxVotes.keys.first()
        } else {
            null // Tie situation
        }
    }
    
    /**
     * Check if there's a tie in voting
     * 
     * @param votes Map of votes cast
     * @return True if there's a tie for most votes
     */
    fun hasTie(votes: Map<PlayerId, Vote>): Boolean {
        val voteCounts = countVotes(votes)
        val maxVotes = voteCounts.values.maxOrNull() ?: return false
        val playersWithMaxVotes = voteCounts.count { it.value == maxVotes }
        return playersWithMaxVotes > 1
    }
    
    /**
     * Get voting summary for display
     * 
     * @param votes Map of votes cast
     * @param players List of all players
     * @return List of vote results sorted by vote count (descending)
     */
    fun getVotingSummary(
        votes: Map<PlayerId, Vote>,
        players: List<Player>
    ): List<VoteResult> {
        val voteCounts = countVotes(votes)
        val playerMap = players.associateBy { it.id }
        
        return voteCounts.map { (playerId, count) ->
            VoteResult(
                player = playerMap[playerId]!!,
                voteCount = count,
                percentage = (count.toDouble() / votes.size.toDouble()) * 100.0
            )
        }.sortedByDescending { it.voteCount }
    }
}

/**
 * Result of voting for a single player
 * 
 * @param player The player who received votes
 * @param voteCount Number of votes received
 * @param percentage Percentage of total votes
 */
data class VoteResult(
    val player: Player,
    val voteCount: Int,
    val percentage: Double
)