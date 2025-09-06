package com.oceloti.lemc.games.impostor.domain.models

import kotlinx.serialization.Serializable

/**
 * Events that occur over the network during gameplay
 */
@Serializable
sealed class NetworkEvent {
    /**
     * A new player has joined the game
     * 
     * @param player The player who joined
     */
    @Serializable
    data class PlayerJoined(val player: Player) : NetworkEvent()
    
    /**
     * A player has left the game
     * 
     * @param playerId The player who left
     */
    @Serializable
    data class PlayerLeft(val playerId: PlayerId) : NetworkEvent()
    
    /**
     * The game state has changed
     * 
     * @param newState The new state of the game
     */
    @Serializable
    data class GameStateChanged(val newState: GameState) : NetworkEvent()
    
    /**
     * A player has submitted a drawing
     * 
     * @param drawing The drawing that was submitted
     */
    @Serializable
    data class PlayerDrawing(val drawing: Drawing) : NetworkEvent()
    
    /**
     * A vote has been cast
     * 
     * @param vote The vote that was cast
     */
    @Serializable
    data class VoteCast(val vote: Vote) : NetworkEvent()
    
    /**
     * The impostor has submitted their guess
     * 
     * @param guess The guess that was submitted
     */
    @Serializable
    data class ImpostorGuessSubmitted(val guess: ImpostorGuess) : NetworkEvent()
    
    /**
     * A round has ended
     * 
     * @param result The result of the round
     */
    @Serializable
    data class RoundEnded(val result: RoundResult) : NetworkEvent()
    
    /**
     * The entire game has ended
     * 
     * @param winner Who won the game
     */
    @Serializable
    data class GameEnded(val winner: GameWinner) : NetworkEvent()
    
    /**
     * A player has disconnected unexpectedly
     * 
     * @param playerId The player who disconnected
     */
    @Serializable
    data class PlayerDisconnected(val playerId: PlayerId) : NetworkEvent()
    
    /**
     * A network or game error occurred
     * 
     * @param message Error message
     * @param cause Optional cause of the error
     */
    @Serializable
    data class Error(val message: String) : NetworkEvent() // Note: Throwable is not serializable
    
    /**
     * Connection status has changed
     * 
     * @param status New connection status
     */
    @Serializable
    data class ConnectionStatusChanged(val status: ConnectionStatus) : NetworkEvent()
    
    /**
     * A game lobby has been discovered during scanning
     * 
     * @param game The discoverable game
     */
    @Serializable
    data class GameDiscovered(val game: DiscoverableGame) : NetworkEvent()
    
    /**
     * Host has updated game configuration
     * 
     * @param config New game configuration
     */
    @Serializable
    data class GameConfigUpdated(val config: GameConfig) : NetworkEvent()
    
    /**
     * Timer update for timed phases
     * 
     * @param remainingTime Time remaining in seconds
     * @param phase Which phase the timer is for
     */
    @Serializable
    data class TimerUpdate(
        val remainingTime: Int,
        val phase: TimedPhase
    ) : NetworkEvent()
    
    /**
     * A player has been kicked from the game
     * 
     * @param playerId The player who was kicked
     */
    @Serializable
    data class PlayerKicked(val playerId: PlayerId) : NetworkEvent()
    
    /**
     * The game has started
     * 
     * @param gameSession The game session that was started
     */
    @Serializable
    data class GameStarted(val gameSession: GameSession) : NetworkEvent()
}

/**
 * Connection status for network communication
 */
enum class ConnectionStatus {
    DISCONNECTED,      // Not connected to any game
    CONNECTING,        // Attempting to connect
    CONNECTED,         // Connected and ready
    HOST_DISCONNECTED, // The host has disconnected
    ERROR             // Connection error
}

/**
 * Represents a game that can be discovered and joined
 * 
 * @param gameId Unique identifier of the discoverable game
 * @param hostName Name of the host player
 * @param playerCount Current number of players
 * @param maxPlayers Maximum number of players allowed
 * @param gameState Current state of the game
 * @param connectionInfo Information needed to connect
 */
data class DiscoverableGame(
    val gameId: GameId,
    val hostName: String,
    val playerCount: Int,
    val maxPlayers: Int,
    val gameState: GameState,
    val connectionInfo: ConnectionInfo
) {
    /**
     * Check if this game can be joined
     */
    fun canJoin(): Boolean {
        return playerCount < maxPlayers && 
               gameState is GameState.WaitingForPlayers
    }
}

/**
 * Information needed to connect to a discoverable game
 * 
 * @param connectionType How to connect (Bluetooth/LAN)
 * @param address Connection address (Bluetooth MAC or IP address)
 * @param port Port number for network connections (null for Bluetooth)
 */
data class ConnectionInfo(
    val connectionType: ConnectionType,
    val address: String,
    val port: Int? = null
)

/**
 * Phases of the game that have timers
 */
enum class TimedPhase {
    WORD_REVEAL,      // Showing the word to players
    DISCUSSION,       // General discussion time
    DRAWING,          // Individual drawing time
    VOTING,           // Voting phase
    IMPOSTOR_GUESS    // Impostor guessing the word
}