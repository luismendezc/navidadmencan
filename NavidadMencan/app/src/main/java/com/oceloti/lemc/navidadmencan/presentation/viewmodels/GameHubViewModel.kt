package com.oceloti.lemc.navidadmencan.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.domain.models.Game
import com.oceloti.lemc.navidadmencan.domain.models.GameCategory
import com.oceloti.lemc.navidadmencan.domain.repositories.GameRepository
import com.oceloti.lemc.navidadmencan.domain.usecases.GetAvailableGamesUseCase
import kotlinx.coroutines.launch

class GameHubViewModel(
    private val getAvailableGamesUseCase: GetAvailableGamesUseCase,
    private val gameRepository: GameRepository
) : BaseViewModel<GameHubState, GameHubEvent>() {

    override fun getInitialState() = GameHubState()

    init {
        loadGames()
    }

    fun loadGames() {
        logAction("loadGames")
        viewModelScope.launch {
            getAvailableGamesUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        updateState { copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        updateState { 
                            copy(
                                games = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        logAction("loadGames", "Loaded ${result.data.size} games")
                    }
                    is Result.Error -> {
                        val errorMessage = result.exception.message ?: "Error loading games"
                        updateState { 
                            copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                        sendEvent(GameHubEvent.ShowError(errorMessage))
                        logError("loadGames", result.exception)
                    }
                }
            }
        }
    }

    fun filterByCategory(category: GameCategory?) {
        logAction("filterByCategory", "Category: $category")
        updateState { copy(selectedCategory = category) }
        
        if (category == null) {
            loadGames()
            return
        }
        
        viewModelScope.launch {
            gameRepository.getGamesByCategory(category).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        updateState { copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        updateState { 
                            copy(
                                games = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        logAction("filterByCategory", "Found ${result.data.size} games in category $category")
                    }
                    is Result.Error -> {
                        val errorMessage = result.exception.message ?: "Error filtering games"
                        updateState { 
                            copy(
                                isLoading = false,
                                error = errorMessage
                            )
                        }
                        sendEvent(GameHubEvent.ShowError(errorMessage))
                        logError("filterByCategory", result.exception)
                    }
                }
            }
        }
    }

    fun installGame(game: Game) {
        logAction("installGame", "Game: ${game.name}")
        viewModelScope.launch {
            when (val result = gameRepository.installGame(game.id)) {
                is Result.Success -> {
                    sendEvent(GameHubEvent.ShowGameInstalled)
                    loadGames() // Reload to update the installation status
                }
                is Result.Error -> {
                    val errorMessage = "Failed to install ${game.name}"
                    sendEvent(GameHubEvent.ShowError(errorMessage))
                    logError("installGame", result.exception)
                }
                is Result.Loading -> { /* Handle if needed */ }
            }
        }
    }

    fun uninstallGame(game: Game) {
        logAction("uninstallGame", "Game: ${game.name}")
        viewModelScope.launch {
            when (val result = gameRepository.uninstallGame(game.id)) {
                is Result.Success -> {
                    sendEvent(GameHubEvent.ShowGameUninstalled)
                    loadGames() // Reload to update the installation status
                }
                is Result.Error -> {
                    val errorMessage = "Failed to uninstall ${game.name}"
                    sendEvent(GameHubEvent.ShowError(errorMessage))
                    logError("uninstallGame", result.exception)
                }
                is Result.Loading -> { /* Handle if needed */ }
            }
        }
    }

    fun onGameSelected(game: Game) {
        logAction("onGameSelected", "Game: ${game.name}")
        sendEvent(GameHubEvent.NavigateToGame(game))
    }

    fun clearError() {
        logAction("clearError")
        updateState { copy(error = null) }
    }
}