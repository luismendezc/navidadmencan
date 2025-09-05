package com.oceloti.lemc.navidadmencan.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WelcomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(WelcomeState())
    val state: StateFlow<WelcomeState> = _state

    private val _events = Channel<WelcomeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: WelcomeAction) {
        when (action) {
            WelcomeAction.OnStartClicked -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    //_events.send(WelcomeEvent.NavigateToDashboard)
                }
            }

            is WelcomeAction.OnDisabledClicked -> {
                viewModelScope.launch {
                    _events.send(
                        WelcomeEvent.ShowMessage(
                            "Waiting operation in: ${action.status}"
                        )
                    )
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    // Ejemplo de mutación de estado si después la necesitas
    fun setLoading(loading: Boolean) {
        _state.update { it.copy(isLoading = loading) }
    }
}