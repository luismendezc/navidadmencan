package com.oceloti.lemc.navidadmencan.presentation.viewmodels

import com.oceloti.lemc.navidadmencan.domain.models.Game
import com.oceloti.lemc.navidadmencan.domain.models.GameCategory

data class GameHubState(
    val games: List<Game> = emptyList(),
    val isLoading: Boolean = false,
    val selectedCategory: GameCategory? = null,
    val error: String? = null
)

sealed class GameHubEvent {
    data class NavigateToGame(val game: Game) : GameHubEvent()
    data class ShowError(val message: String) : GameHubEvent()
    data object ShowGameInstalled : GameHubEvent()
    data object ShowGameUninstalled : GameHubEvent()
}