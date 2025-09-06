package com.oceloti.lemc.games.impostor.domain.models

/**
 * Configuration settings for an impostor game
 * 
 * @param hostName Name of the player hosting this game
 * @param livesPerPlayer Number of lives each player starts with
 * @param selectedCategories List of word categories selected for this game
 * @param maxPlayers Maximum number of players allowed
 * @param discussionTimeSeconds Time in seconds for discussion phase
 * @param votingTimeSeconds Time in seconds for voting phase
 */
data class GameConfig(
    val hostName: String,
    val livesPerPlayer: Int,
    val selectedCategories: List<WordCategoryId>,
    val maxPlayers: Int = 10,
    val discussionTimeSeconds: Int = 180, // 3 minutes
    val votingTimeSeconds: Int = 60       // 1 minute
) {
    
    /**
     * Validates the configuration settings
     */
    fun isValid(): Boolean {
        return hostName.isNotBlank() &&
                livesPerPlayer > 0 &&
                discussionTimeSeconds > 0 &&
                votingTimeSeconds > 0 &&
                selectedCategories.isNotEmpty() &&
                maxPlayers in MIN_PLAYERS..MAX_PLAYERS
    }
    
    /**
     * Validates if the current player count allows starting the game
     * @param currentPlayerCount Total players including host
     */
    fun canStartGame(currentPlayerCount: Int): Boolean {
        return isValid() && currentPlayerCount >= MIN_PLAYERS
    }
    
    companion object {
        const val MIN_PLAYERS = 3
        const val MAX_PLAYERS = 12
        
        /**
         * Default configuration for casual games
         */
        fun default(hostName: String): GameConfig = GameConfig(
            hostName = hostName,
            livesPerPlayer = 3,
            selectedCategories = emptyList(),
            maxPlayers = 8,
            discussionTimeSeconds = 180, // 3 minutes
            votingTimeSeconds = 60       // 1 minute
        )
        
        /**
         * Quick configuration for faster games
         */
        fun quick(hostName: String): GameConfig = GameConfig(
            hostName = hostName,
            livesPerPlayer = 2,
            selectedCategories = emptyList(),
            maxPlayers = 6,
            discussionTimeSeconds = 120, // 2 minutes
            votingTimeSeconds = 45       // 45 seconds
        )
    }
}