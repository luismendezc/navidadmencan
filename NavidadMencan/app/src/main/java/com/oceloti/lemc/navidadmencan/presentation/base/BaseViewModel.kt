package com.oceloti.lemc.navidadmencan.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.navidadmencan.core.util.AppLog.logD
import com.oceloti.lemc.navidadmencan.core.util.AppLog.logE
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class that provides common functionality for state management
 * and event handling using StateFlow and Channel patterns
 */
abstract class BaseViewModel<State : Any, Action : Any, Event : Any>(
    initialState: State
) : ViewModel() {
    
    // State management with StateFlow
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<State> = _state.asStateFlow()
    
    // Event handling with Channel
    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()
    
    // Error handling
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        logE(this::class.simpleName ?: "BaseViewModel", "Coroutine exception caught", exception)
        handleError(exception)
    }
    
    /**
     * Current state value
     */
    protected val currentState: State
        get() = _state.value
    
    /**
     * Update the state
     */
    protected fun updateState(newState: State) {
        logD(this::class.simpleName ?: "BaseViewModel", "State updated")
        _state.value = newState
    }
    
    /**
     * Update state using a transform function
     */
    protected fun updateState(transform: (State) -> State) {
        updateState(transform(currentState))
    }
    
    /**
     * Send an event
     */
    protected fun sendEvent(event: Event) {
        viewModelScope.launch(exceptionHandler) {
            logD(this@BaseViewModel::class.simpleName ?: "BaseViewModel", "Event sent: $event")
            _events.send(event)
        }
    }
    
    /**
     * Handle actions from the UI
     */
    abstract fun onAction(action: Action)
    
    /**
     * Handle errors - override in subclasses for custom error handling
     */
    protected open fun handleError(exception: Throwable) {
        logE(this::class.simpleName ?: "BaseViewModel", "Error handled in base class", exception)
        // Default error handling - can be overridden in subclasses
    }
    
    /**
     * Launch a coroutine with error handling
     */
    protected fun launchWithErrorHandling(block: suspend () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            block()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        logD(this::class.simpleName ?: "BaseViewModel", "ViewModel cleared")
    }
}