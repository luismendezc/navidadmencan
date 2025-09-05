package com.oceloti.lemc.navidadmencan.presentation.viewmodels

import androidx.compose.runtime.Immutable

@Immutable
data class WelcomeState(
    val title: String = "🎄 Bienvenido/a a Navidad Men Can",
    val isLoading: Boolean = false
)

sealed interface WelcomeAction {
    data object OnStartClicked : WelcomeAction
    data class OnDisabledClicked(val status: String) : WelcomeAction
}

sealed interface WelcomeEvent {
    data object NavigateToDashboard : WelcomeEvent
    data class ShowMessage(val message: String) : WelcomeEvent
}