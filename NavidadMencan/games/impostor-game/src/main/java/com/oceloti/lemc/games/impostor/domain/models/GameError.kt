package com.oceloti.lemc.games.impostor.domain.models

/**
 * Comprehensive error handling for the impostor game
 */
sealed class GameError(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {
    
    /**
     * Network-related errors
     */
    sealed class NetworkError(message: String, cause: Throwable? = null) : GameError(message, cause) {
        data class BluetoothNotAvailable(override val cause: Throwable? = null) : 
            NetworkError("Bluetooth no está disponible", cause)
        
        data class BluetoothPermissionDenied(override val cause: Throwable? = null) : 
            NetworkError("Permisos de Bluetooth denegados", cause)
        
        data class ConnectionLost(val playerId: PlayerId? = null, override val cause: Throwable? = null) : 
            NetworkError("Conexión perdida", cause)
        
        data class ConnectionTimeout(override val cause: Throwable? = null) : 
            NetworkError("Tiempo de conexión agotado", cause)
        
        data class HostDisconnected(override val cause: Throwable? = null) : 
            NetworkError("El anfitrión se desconectó", cause)
        
        data class MessageSendFailed(val recipient: PlayerId? = null, override val cause: Throwable? = null) : 
            NetworkError("Error al enviar mensaje", cause)
    }
    
    /**
     * Game logic errors
     */
    sealed class GameLogicError(message: String, cause: Throwable? = null) : GameError(message, cause) {
        data class InvalidPlayerCount(val current: Int, val required: Int) : 
            GameLogicError("Se requieren $required jugadores, tienes $current")
        
        data class GameNotFound(val gameId: GameId) : 
            GameLogicError("Juego ${gameId.value} no encontrado")
        
        data class PlayerNotFound(val playerId: PlayerId) : 
            GameLogicError("Jugador ${playerId.value} no encontrado")
        
        data class InvalidGameState(val currentState: GameState, val attemptedAction: String) : 
            GameLogicError("No se puede $attemptedAction en estado ${currentState::class.simpleName}")
        
        data class GameAlreadyStarted(val gameId: GameId) : 
            GameLogicError("El juego ${gameId.value} ya ha comenzado")
        
        data class GameIsFull(val gameId: GameId, val maxPlayers: Int) : 
            GameLogicError("El juego ${gameId.value} está lleno ($maxPlayers jugadores)")
        
        data class NoWordsAvailable(val categoryId: WordCategoryId) : 
            GameLogicError("No hay palabras disponibles en la categoría ${categoryId.value}")
        
        data class VoteAlreadyCast(val playerId: PlayerId) : 
            GameLogicError("El jugador ${playerId.value} ya ha votado")
        
        data class DrawingTimeExpired(val playerId: PlayerId) : 
            GameLogicError("Tiempo de dibujo agotado para ${playerId.value}")
    }
    
    /**
     * Configuration errors
     */
    sealed class ConfigurationError(message: String, cause: Throwable? = null) : GameError(message, cause) {
        data class InvalidGameConfig(val reason: String) : 
            ConfigurationError("Configuración inválida: $reason")
        
        data class NoCategoriesSelected(override val cause: Throwable? = null) : 
            ConfigurationError("Debes seleccionar al menos una categoría")
        
        data class InvalidTimeSettings(val setting: String, val value: Int) : 
            ConfigurationError("Configuración de tiempo inválida: $setting = $value")
    }
    
    /**
     * System errors
     */
    sealed class SystemError(message: String, cause: Throwable? = null) : GameError(message, cause) {
        data class UnexpectedError(override val cause: Throwable) : 
            SystemError("Error inesperado: ${cause.message}", cause)
        
        data class SerializationError(val data: String, override val cause: Throwable? = null) : 
            SystemError("Error de serialización", cause)
        
        data class StateCorruption(val description: String) : 
            SystemError("Estado del juego corrompido: $description")
        
        data class ResourceNotFound(val resource: String) : 
            SystemError("Recurso no encontrado: $resource")
    }
}

/**
 * Extension functions for error handling
 */
fun Result<*>.toGameError(): GameError? {
    return exceptionOrNull()?.let { exception ->
        when (exception) {
            is GameError -> exception
            is SecurityException -> GameError.NetworkError.BluetoothPermissionDenied(exception)
            is IllegalStateException -> GameError.SystemError.StateCorruption(exception.message ?: "Unknown")
            is IllegalArgumentException -> GameError.ConfigurationError.InvalidGameConfig(exception.message ?: "Unknown")
            else -> GameError.SystemError.UnexpectedError(exception)
        }
    }
}

/**
 * User-friendly error messages in Spanish
 */
fun GameError.getUserMessage(): String = when (this) {
    is GameError.NetworkError.BluetoothNotAvailable -> "Bluetooth no está disponible. Actívalo e inténtalo de nuevo."
    is GameError.NetworkError.BluetoothPermissionDenied -> "Necesitas conceder permisos de Bluetooth para jugar."
    is GameError.NetworkError.ConnectionLost -> "Se perdió la conexión. Intentando reconectar..."
    is GameError.NetworkError.ConnectionTimeout -> "La conexión tardó demasiado. Inténtalo de nuevo."
    is GameError.NetworkError.HostDisconnected -> "El anfitrión se desconectó. El juego ha terminado."
    is GameError.NetworkError.MessageSendFailed -> "Error al comunicarse con otros jugadores."
    
    is GameError.GameLogicError.InvalidPlayerCount -> message
    is GameError.GameLogicError.GameNotFound -> "No se encontró el juego. Puede haber terminado."
    is GameError.GameLogicError.PlayerNotFound -> "Jugador no encontrado."
    is GameError.GameLogicError.InvalidGameState -> "No se puede realizar esta acción ahora."
    is GameError.GameLogicError.GameAlreadyStarted -> "El juego ya comenzó."
    is GameError.GameLogicError.GameIsFull -> "El juego está lleno."
    is GameError.GameLogicError.NoWordsAvailable -> "No hay palabras disponibles para esta categoría."
    is GameError.GameLogicError.VoteAlreadyCast -> "Ya has votado en esta ronda."
    is GameError.GameLogicError.DrawingTimeExpired -> "Se acabó el tiempo para dibujar."
    
    is GameError.ConfigurationError.InvalidGameConfig -> "Configuración del juego inválida: $reason"
    is GameError.ConfigurationError.NoCategoriesSelected -> "Debes seleccionar al menos una categoría de palabras."
    is GameError.ConfigurationError.InvalidTimeSettings -> "Configuración de tiempo inválida."
    
    is GameError.SystemError.UnexpectedError -> "Ocurrió un error inesperado. Inténtalo de nuevo."
    is GameError.SystemError.SerializationError -> "Error al procesar datos del juego."
    is GameError.SystemError.StateCorruption -> "Estado del juego corrupto. Reinicia el juego."
    is GameError.SystemError.ResourceNotFound -> "Recurso no encontrado."
}

/**
 * Determine if an error is recoverable
 */
fun GameError.isRecoverable(): Boolean = when (this) {
    is GameError.NetworkError.ConnectionLost -> true
    is GameError.NetworkError.ConnectionTimeout -> true
    is GameError.NetworkError.MessageSendFailed -> true
    is GameError.GameLogicError.DrawingTimeExpired -> true
    is GameError.SystemError.SerializationError -> true
    else -> false
}

/**
 * Recovery strategies for different errors
 */
enum class ErrorRecoveryStrategy {
    RETRY,           // Try the operation again
    RECONNECT,       // Attempt to reconnect to network
    NAVIGATE_BACK,   // Go back to previous screen
    RESTART_GAME,    // Start a new game
    SHOW_ERROR,      // Just show error message
    IGNORE           // Ignore and continue
}

/**
 * Get recovery strategy for an error
 */
fun GameError.getRecoveryStrategy(): ErrorRecoveryStrategy = when (this) {
    is GameError.NetworkError.ConnectionLost -> ErrorRecoveryStrategy.RECONNECT
    is GameError.NetworkError.ConnectionTimeout -> ErrorRecoveryStrategy.RETRY
    is GameError.NetworkError.HostDisconnected -> ErrorRecoveryStrategy.NAVIGATE_BACK
    is GameError.NetworkError.BluetoothNotAvailable -> ErrorRecoveryStrategy.SHOW_ERROR
    is GameError.NetworkError.BluetoothPermissionDenied -> ErrorRecoveryStrategy.SHOW_ERROR
    
    is GameError.GameLogicError.GameNotFound -> ErrorRecoveryStrategy.NAVIGATE_BACK
    is GameError.GameLogicError.GameAlreadyStarted -> ErrorRecoveryStrategy.NAVIGATE_BACK
    is GameError.GameLogicError.GameIsFull -> ErrorRecoveryStrategy.NAVIGATE_BACK
    is GameError.GameLogicError.InvalidPlayerCount -> ErrorRecoveryStrategy.SHOW_ERROR
    is GameError.GameLogicError.DrawingTimeExpired -> ErrorRecoveryStrategy.IGNORE
    
    is GameError.ConfigurationError -> ErrorRecoveryStrategy.SHOW_ERROR
    
    is GameError.SystemError.StateCorruption -> ErrorRecoveryStrategy.RESTART_GAME
    is GameError.SystemError.UnexpectedError -> ErrorRecoveryStrategy.SHOW_ERROR
    else -> ErrorRecoveryStrategy.SHOW_ERROR
}