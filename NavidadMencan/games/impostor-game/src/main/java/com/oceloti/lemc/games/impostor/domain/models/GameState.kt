package com.oceloti.lemc.games.impostor.domain.models

/**
 * Represents the current state of the impostor game
 */
sealed class GameState {
    /**
     * Game is waiting for more players to join
     */
    data object WaitingForPlayers : GameState()
    
    /**
     * Game has started, preparing first round
     */
    data object GameStarted : GameState()
    
    /**
     * Word is being revealed to non-impostor players
     * 
     * @param word The secret word for this round
     * @param category The category this word belongs to
     */
    data class WordReveal(
        val word: String,
        val category: WordCategory
    ) : GameState()
    
    /**
     * Players are discussing and drawing their clues
     */
    data object Discussion : GameState()
    
    /**
     * Individual drawing phase for one player
     * 
     * @param currentDrawer Player who is currently drawing
     * @param remainingTime Time left in seconds
     */
    data class Drawing(
        val currentDrawer: PlayerId,
        val remainingTime: Int
    ) : GameState()
    
    /**
     * Voting phase where players vote for the impostor
     * 
     * @param remainingTime Time left for voting in seconds
     */
    data class Voting(val remainingTime: Int) : GameState()
    
    /**
     * Impostor is trying to guess the secret word
     * 
     * @param impostor The impostor player
     * @param remainingTime Time left for guessing in seconds
     */
    data class ImpostorGuessing(
        val impostor: PlayerId,
        val remainingTime: Int
    ) : GameState()
    
    /**
     * Round has ended, showing results
     * 
     * @param result The result of the round
     */
    data class RoundResult(val result: RoundResult) : GameState()
    
    /**
     * Game has ended completely
     * 
     * @param winner Who won the game
     */
    data class GameEnded(val winner: GameWinner) : GameState()
}

/**
 * Result of a completed round
 */
sealed class RoundResult {
    /**
     * The impostor was correctly identified
     * 
     * @param impostor The impostor player
     * @param savedByGuess Whether the impostor saved themselves by guessing correctly
     */
    data class ImpostorCaught(
        val impostor: PlayerId,
        val savedByGuess: Boolean
    ) : RoundResult()
    
    /**
     * An ally was wrongly accused
     * 
     * @param impostor The actual impostor
     * @param eliminatedPlayer The wrongly accused player
     */
    data class ImpostorNotCaught(
        val impostor: PlayerId,
        val eliminatedPlayer: PlayerId
    ) : RoundResult()
    
    /**
     * Impostor won by surviving or guessing correctly
     * 
     * @param impostor The winning impostor
     */
    data class ImpostorWon(val impostor: PlayerId) : RoundResult()
}

/**
 * Final winner of the entire game
 */
sealed class GameWinner {
    /**
     * The impostor won
     * 
     * @param impostor The winning impostor
     */
    data class ImpostorWins(val impostor: PlayerId) : GameWinner()
    
    /**
     * The allies won
     * 
     * @param survivors List of surviving ally players
     */
    data class AlliesWin(val survivors: List<PlayerId>) : GameWinner()
}