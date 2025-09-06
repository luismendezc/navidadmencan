package com.oceloti.lemc.games.impostor.domain.models

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Manages game state transitions and validation for the impostor game
 * Ensures only valid state transitions occur and handles timing
 */
class GameStateManager(
    private val scope: CoroutineScope
) {
    
    private val _currentState = MutableStateFlow<GameState>(GameState.WaitingForPlayers)
    val currentState = _currentState.asStateFlow()
    
    private val _stateEvents = MutableSharedFlow<GameStateEvent>()
    val stateEvents = _stateEvents.asSharedFlow()
    
    private var timerJob: Job? = null
    
    /**
     * Attempt to transition to a new state
     */
    suspend fun transitionTo(newState: GameState): Result<Unit> {
        val currentState = _currentState.value
        
        return if (isValidTransition(currentState, newState)) {
            _currentState.value = newState
            onStateEntered(newState)
            _stateEvents.emit(GameStateEvent.StateChanged(currentState, newState))
            Result.success(Unit)
        } else {
            val error = "Invalid transition from ${currentState::class.simpleName} to ${newState::class.simpleName}"
            Result.failure(IllegalStateException(error))
        }
    }
    
    /**
     * Check if a state transition is valid
     */
    private fun isValidTransition(from: GameState, to: GameState): Boolean {
        return when (from) {
            is GameState.WaitingForPlayers -> {
                to is GameState.GameStarted || to is GameState.WordReveal
            }
            is GameState.GameStarted -> {
                to is GameState.WordReveal || to is GameState.WaitingForPlayers
            }
            is GameState.WordReveal -> {
                to is GameState.Discussion || to is GameState.Drawing || to is GameState.GameEnded
            }
            is GameState.Discussion -> {
                to is GameState.Drawing || to is GameState.Voting || to is GameState.GameEnded
            }
            is GameState.Drawing -> {
                to is GameState.Discussion || to is GameState.Voting || to is GameState.GameEnded
            }
            is GameState.Voting -> {
                to is GameState.ImpostorGuessing || to is GameState.RoundResult || to is GameState.GameEnded
            }
            is GameState.ImpostorGuessing -> {
                to is GameState.RoundResult || to is GameState.GameEnded
            }
            is GameState.RoundResult -> {
                to is GameState.WordReveal || to is GameState.GameEnded || to is GameState.WaitingForPlayers
            }
            is GameState.GameEnded -> {
                to is GameState.WaitingForPlayers
            }
        }
    }
    
    /**
     * Handle entering a new state (start timers, etc.)
     */
    private suspend fun onStateEntered(state: GameState) {
        // Cancel any existing timer
        timerJob?.cancel()
        
        // Start new timer if needed
        when (state) {
            is GameState.WordReveal -> {
                startTimer(WORD_REVEAL_DURATION, TimedPhase.WORD_REVEAL)
            }
            is GameState.Discussion -> {
                // Timer duration comes from game config
                // This should be passed in from the game session
            }
            is GameState.Drawing -> {
                startTimer(state.remainingTime, TimedPhase.DRAWING)
            }
            is GameState.Voting -> {
                startTimer(state.remainingTime, TimedPhase.VOTING)
            }
            is GameState.ImpostorGuessing -> {
                startTimer(state.remainingTime, TimedPhase.IMPOSTOR_GUESS)
            }
            else -> { /* No timer needed */ }
        }
    }
    
    /**
     * Start a countdown timer for timed phases
     */
    private fun startTimer(durationSeconds: Int, phase: TimedPhase) {
        timerJob = scope.launch {
            repeat(durationSeconds) { elapsed ->
                val remaining = durationSeconds - elapsed
                _stateEvents.emit(GameStateEvent.TimerUpdate(remaining, phase))
                delay(1000) // 1 second
            }
            
            // Timer expired
            _stateEvents.emit(GameStateEvent.TimerExpired(phase))
            handleTimerExpiry(phase)
        }
    }
    
    /**
     * Handle timer expiry for different phases
     */
    private suspend fun handleTimerExpiry(phase: TimedPhase) {
        when (phase) {
            TimedPhase.WORD_REVEAL -> {
                transitionTo(GameState.Discussion)
            }
            TimedPhase.DISCUSSION -> {
                // Move to first player's drawing phase
                // This needs to be coordinated with game logic
            }
            TimedPhase.DRAWING -> {
                // Move to next player or to voting
                // This needs to be coordinated with game logic
            }
            TimedPhase.VOTING -> {
                // Process votes and determine result
                // This needs to be coordinated with game logic
            }
            TimedPhase.IMPOSTOR_GUESS -> {
                // Impostor failed to guess in time
                // This needs to be coordinated with game logic
            }
        }
    }
    
    fun cleanup() {
        timerJob?.cancel()
    }
    
    companion object {
        private const val WORD_REVEAL_DURATION = 10 // seconds
    }
}

/**
 * Events emitted by the game state manager
 */
sealed class GameStateEvent {
    data class StateChanged(val from: GameState, val to: GameState) : GameStateEvent()
    data class TimerUpdate(val remainingTime: Int, val phase: TimedPhase) : GameStateEvent()
    data class TimerExpired(val phase: TimedPhase) : GameStateEvent()
}