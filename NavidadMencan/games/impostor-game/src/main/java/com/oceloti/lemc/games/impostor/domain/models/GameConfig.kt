package com.oceloti.lemc.games.impostor.domain.models

/**
 * Configuration settings for an impostor game
 * 
 * @param maxLives Maximum number of lives each player starts with
 * @param discussionTimeSeconds Time in seconds for discussion phase
 * @param votingTimeSeconds Time in seconds for voting phase
 * @param wordCategories List of word categories to use in the game
 * @param minPlayers Minimum number of players required
 * @param maxPlayers Maximum number of players allowed
 */
data class GameConfig(
    val maxLives: Int,
    val discussionTimeSeconds: Int,
    val votingTimeSeconds: Int,
    val wordCategories: List<WordCategoryId>,
    val minPlayers: Int = 3,
    val maxPlayers: Int = 10
) {
    companion object {
        /**
         * Default configuration for casual games
         */
        fun default(): GameConfig = GameConfig(
            maxLives = 3,
            discussionTimeSeconds = 180, // 3 minutes
            votingTimeSeconds = 60,      // 1 minute
            wordCategories = emptyList(),
            minPlayers = 3,
            maxPlayers = 8
        )
        
        /**
         * Quick configuration for faster games
         */
        fun quick(): GameConfig = GameConfig(
            maxLives = 2,
            discussionTimeSeconds = 120, // 2 minutes
            votingTimeSeconds = 45,      // 45 seconds
            wordCategories = emptyList(),
            minPlayers = 3,
            maxPlayers = 6
        )
    }
    
    /**
     * Validates the configuration settings
     */
    fun isValid(): Boolean {
        return maxLives > 0 &&
                discussionTimeSeconds > 0 &&
                votingTimeSeconds > 0 &&
                wordCategories.isNotEmpty() &&
                minPlayers >= 3 &&
                maxPlayers >= minPlayers &&
                maxPlayers <= 12
    }
}