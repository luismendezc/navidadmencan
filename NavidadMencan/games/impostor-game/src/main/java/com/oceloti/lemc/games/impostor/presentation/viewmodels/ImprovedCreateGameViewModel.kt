package com.oceloti.lemc.games.impostor.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.games.impostor.data.network.BluetoothService
import com.oceloti.lemc.games.impostor.domain.models.*
import com.oceloti.lemc.games.impostor.domain.repository.GameRepository
import com.oceloti.lemc.games.impostor.domain.repository.WordRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Improved CreateGameViewModel with proper DI, state preservation, and error handling
 * Note: In production, use Hilt for proper dependency injection
 */
class ImprovedCreateGameViewModel constructor(
    private val bluetoothService: BluetoothService,
    private val wordRepository: WordRepository,
    private val gameRepository: GameRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // State keys for SavedStateHandle
    companion object {
        private const val KEY_GAME_CONFIG = "game_config"
        private const val KEY_GAME_ID = "game_id"
        private const val KEY_IS_HOSTING = "is_hosting"
        private const val KEY_PLAYERS_IN_LOBBY = "players_in_lobby"
    }
    
    private val _uiState = MutableStateFlow(
        com.oceloti.lemc.games.impostor.presentation.viewmodels.CreateGameUiState(
            gameConfig = savedStateHandle.get<GameConfig>(KEY_GAME_CONFIG),
            gameId = savedStateHandle.get<String>(KEY_GAME_ID)?.let(::GameId),
            isHosting = savedStateHandle.get<Boolean>(KEY_IS_HOSTING) ?: false,
            playersInLobby = savedStateHandle.get<List<Player>>(KEY_PLAYERS_IN_LOBBY) ?: emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()
    
    private val _uiEvent = Channel<CreateGameUiEvent>(Channel.UNLIMITED)
    val uiEvent = _uiEvent.receiveAsFlow()
    
    private val _errorState = MutableStateFlow<GameError?>(null)
    val errorState = _errorState.asStateFlow()
    
    init {
        loadAvailableCategories()
        observeNetworkEvents()
        
        // Restore hosting state if needed
        if (_uiState.value.isHosting) {
            restoreHostingState()
        }
    }
    
    private fun loadAvailableCategories() {
        viewModelScope.launch {
            try {
                val categories = wordRepository.getAllCategories()
                _uiState.update { it.copy(availableCategories = categories) }
            } catch (exception: Exception) {
                handleError(GameError.SystemError.UnexpectedError(exception))
            }
        }
    }
    
    private fun observeNetworkEvents() {
        viewModelScope.launch {
            bluetoothService.observeNetworkEvents().collect { event ->
                handleNetworkEvent(event)
            }
        }
    }
    
    private suspend fun handleNetworkEvent(event: NetworkEvent) {
        when (event) {
            is NetworkEvent.ConnectionStatusChanged -> {
                updateConnectionStatus(event.status)
                
                if (event.status == ConnectionStatus.CONNECTED) {
                    _uiEvent.send(CreateGameUiEvent.NavigateToLobby)
                }
            }
            
            is NetworkEvent.PlayerJoined -> {
                addPlayerToLobby(event.player)
            }
            
            is NetworkEvent.PlayerDisconnected -> {
                removePlayerFromLobby(event.playerId)
            }
            
            is NetworkEvent.Error -> {
                handleError(GameError.NetworkError.MessageSendFailed(cause = RuntimeException(event.message)))
            }
            
            else -> { /* Handle other events */ }
        }
    }
    
    private fun updateConnectionStatus(status: ConnectionStatus) {
        _uiState.update { it.copy(connectionStatus = status) }
        saveState()
    }
    
    private fun addPlayerToLobby(player: Player) {
        _uiState.update { currentState ->
            val updatedPlayers = (currentState.playersInLobby + player).distinctBy { it.id }
            currentState.copy(playersInLobby = updatedPlayers)
        }
        saveState()
    }
    
    private fun removePlayerFromLobby(playerId: PlayerId) {
        _uiState.update { currentState ->
            val updatedPlayers = currentState.playersInLobby.filterNot { it.id == playerId }
            currentState.copy(playersInLobby = updatedPlayers)
        }
        saveState()
    }
    
    fun startHosting(gameConfig: GameConfig) {
        viewModelScope.launch {
            try {
                // Validate Bluetooth availability
                if (!bluetoothService.isBluetoothAvailable()) {
                    throw GameError.NetworkError.BluetoothNotAvailable()
                }
                
                if (!bluetoothService.hasRequiredPermissions()) {
                    throw GameError.NetworkError.BluetoothPermissionDenied()
                }
                
                // Validate game configuration
                if (!gameConfig.isValid()) {
                    throw GameError.ConfigurationError.InvalidGameConfig("Invalid configuration")
                }
                
                val gameId = GameId(generateGameId())
                
                // Update UI state
                _uiState.update { currentState ->
                    currentState.copy(
                        gameConfig = gameConfig,
                        gameId = gameId,
                        isHosting = true,
                        connectionStatus = ConnectionStatus.CONNECTING
                    )
                }
                saveState()
                
                // Start hosting
                val result = bluetoothService.startHosting(gameId, gameConfig.hostName)
                result.fold(
                    onSuccess = {
                        // Success handled by network events observer
                    },
                    onFailure = { error ->
                        handleHostingError(error)
                    }
                )
                
            } catch (error: GameError) {
                handleError(error)
            } catch (exception: Exception) {
                handleError(GameError.SystemError.UnexpectedError(exception))
            }
        }
    }
    
    private fun handleHostingError(error: Throwable) {
        _uiState.update { currentState ->
            currentState.copy(
                isHosting = false,
                connectionStatus = ConnectionStatus.DISCONNECTED
            )
        }
        saveState()
        
        val gameError = when (error) {
            is SecurityException -> GameError.NetworkError.BluetoothPermissionDenied(error)
            is IllegalStateException -> GameError.NetworkError.BluetoothNotAvailable(error)
            else -> GameError.SystemError.UnexpectedError(error)
        }
        
        handleError(gameError)
    }
    
    fun startGame() {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                val gameConfig = currentState.gameConfig 
                    ?: throw GameError.ConfigurationError.InvalidGameConfig("No game config")
                
                val totalPlayers = currentState.playersInLobby.size + 1 // +1 for host
                
                if (!gameConfig.canStartGame(totalPlayers)) {
                    throw GameError.GameLogicError.InvalidPlayerCount(
                        current = totalPlayers,
                        required = GameConfig.MIN_PLAYERS
                    )
                }
                
                // Select random category
                val selectedCategoryId = gameConfig.selectedCategories.randomOrNull()
                    ?: throw GameError.ConfigurationError.NoCategoriesSelected()
                
                val selectedCategory = wordRepository.getCategoryById(selectedCategoryId)
                    ?: throw GameError.GameLogicError.NoWordsAvailable(selectedCategoryId)
                
                // Create host player
                val hostPlayer = Player(
                    id = PlayerId("host"),
                    name = gameConfig.hostName,
                    isHost = true,
                    lives = gameConfig.livesPerPlayer,
                    connectionType = ConnectionType.HOST,
                    deviceInfo = DeviceInfo(
                        platform = Platform.ANDROID,
                        deviceName = "Host Device",
                        version = "1.0"
                    )
                )
                
                // Create game session using repository
                val result = gameRepository.createGameSession(gameConfig, hostPlayer)
                result.fold(
                    onSuccess = { gameSession ->
                        // Update the session with all players and random impostor
                        val allPlayers = listOf(hostPlayer) + currentState.playersInLobby
                        val impostorIndex = allPlayers.indices.random()
                        val gameWords = wordRepository.getGameWords(selectedCategoryId, 24)
                        
                        val finalSession = gameSession.copy(
                            players = allPlayers,
                            currentState = GameState.WordReveal(
                                word = gameWords.first(),
                                category = selectedCategory
                            ),
                            impostorId = allPlayers[impostorIndex].id,
                            secretWords = gameWords,
                            selectedCategory = selectedCategory
                        )
                        
                        // Update repository
                        gameRepository.updateGameSession(finalSession)
                        
                        // Update UI state
                        _uiState.update { it.copy(gameSession = finalSession) }
                        saveState()
                        
                        // Notify all players
                        val startEvent = NetworkEvent.GameStarted(finalSession)
                        bluetoothService.sendNetworkEvent(startEvent)
                        
                        _uiEvent.send(CreateGameUiEvent.NavigateToGame(finalSession))
                    },
                    onFailure = { error ->
                        handleError(GameError.SystemError.UnexpectedError(error))
                    }
                )
                
            } catch (error: GameError) {
                handleError(error)
            } catch (exception: Exception) {
                handleError(GameError.SystemError.UnexpectedError(exception))
            }
        }
    }
    
    fun kickPlayer(playerId: PlayerId) {
        viewModelScope.launch {
            try {
                val event = NetworkEvent.PlayerKicked(playerId)
                bluetoothService.sendNetworkEvent(event)
                removePlayerFromLobby(playerId)
            } catch (exception: Exception) {
                handleError(GameError.NetworkError.MessageSendFailed(playerId, exception))
            }
        }
    }
    
    fun stopHosting() {
        viewModelScope.launch {
            try {
                bluetoothService.stopService()
                _uiState.update { currentState ->
                    currentState.copy(
                        isHosting = false,
                        connectionStatus = ConnectionStatus.DISCONNECTED,
                        playersInLobby = emptyList()
                    )
                }
                saveState()
            } catch (exception: Exception) {
                handleError(GameError.SystemError.UnexpectedError(exception))
            }
        }
    }
    
    fun clearError() {
        _errorState.value = null
    }
    
    private fun handleError(error: GameError) {
        _errorState.value = error
        // Error state handled by _errorState flow
        
        viewModelScope.launch {
            _uiEvent.send(CreateGameUiEvent.ShowError(error.getUserMessage()))
        }
    }
    
    private fun restoreHostingState() {
        // If we were hosting before process death, attempt to restore
        viewModelScope.launch {
            // Implementation would depend on whether Bluetooth connection can be restored
            // For now, just reset hosting state
            _uiState.update { it.copy(isHosting = false) }
            saveState()
        }
    }
    
    private fun saveState() {
        val state = _uiState.value
        savedStateHandle[KEY_GAME_CONFIG] = state.gameConfig
        savedStateHandle[KEY_GAME_ID] = state.gameId?.value
        savedStateHandle[KEY_IS_HOSTING] = state.isHosting
        savedStateHandle[KEY_PLAYERS_IN_LOBBY] = state.playersInLobby
    }
    
    private fun generateGameId(): String {
        return (1..8).map { ('A'..'Z').random() }.joinToString("")
    }
    
    override fun onCleared() {
        super.onCleared()
        if (_uiState.value.isHosting) {
            viewModelScope.launch {
                bluetoothService.stopService()
            }
        }
    }
}

// UI State and Events are defined in the original CreateGameViewModel