package com.oceloti.lemc.navidadmencan.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.navidadmencan.core.util.AppLog
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Event> : ViewModel() {
    
    protected abstract fun getInitialState(): State
    
    private val _uiState = MutableStateFlow(getInitialState())
    val uiState: StateFlow<State> = _uiState.asStateFlow()
    
    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()
    
    protected fun updateState(newState: State) {
        _uiState.value = newState
    }
    
    protected fun updateState(reducer: State.() -> State) {
        _uiState.value = _uiState.value.reducer()
    }
    
    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            _events.send(event)
        }
    }
    
    protected fun logAction(action: String, details: String = "") {
        val className = this::class.simpleName ?: "BaseViewModel"
        AppLog.d(className, "$action: $details")
    }
    
    protected fun logError(action: String, error: Throwable) {
        val className = this::class.simpleName ?: "BaseViewModel"
        AppLog.e(className, "Error in $action", error)
    }
}