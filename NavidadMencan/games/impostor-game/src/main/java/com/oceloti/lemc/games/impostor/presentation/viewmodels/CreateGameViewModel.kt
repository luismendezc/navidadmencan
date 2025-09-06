package com.oceloti.lemc.games.impostor.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.games.impostor.data.network.BluetoothService
import com.oceloti.lemc.games.impostor.domain.models.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreateGameViewModel(
    private val bluetoothService: BluetoothService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CreateGameUiState())
    val uiState = _uiState.asStateFlow()
    
    private val _uiEvent = Channel<CreateGameUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    
    private val spanishWordDatabase = SpanishWordDatabase
    
    init {
        loadAvailableCategories()
        observeNetworkEvents()
    }
    
    private fun loadAvailableCategories() {
        _uiState.update { currentState ->
            currentState.copy(
                availableCategories = spanishWordDatabase.getAllCategories()
            )
        }
    }
    
    private fun observeNetworkEvents() {
        viewModelScope.launch {
            bluetoothService.observeNetworkEvents().collect { event ->
                when (event) {
                    is NetworkEvent.ConnectionStatusChanged -> {
                        _uiState.update { currentState ->
                            currentState.copy(connectionStatus = event.status)
                        }
                        
                        if (event.status == ConnectionStatus.CONNECTED) {
                            _uiEvent.send(CreateGameUiEvent.NavigateToLobby)
                        }
                    }
                    
                    is NetworkEvent.PlayerJoined -> {
                        _uiState.update { currentState ->
                            val updatedPlayers = currentState.playersInLobby + event.player
                            currentState.copy(playersInLobby = updatedPlayers)
                        }
                    }
                    
                    is NetworkEvent.PlayerDisconnected -> {
                        _uiState.update { currentState ->
                            val updatedPlayers = currentState.playersInLobby.filterNot { 
                                it.id == event.playerId 
                            }
                            currentState.copy(playersInLobby = updatedPlayers)
                        }
                    }
                    
                    is NetworkEvent.Error -> {
                        _uiEvent.send(CreateGameUiEvent.ShowError(event.message))
                    }
                    
                    else -> { /* Handle other events */ }
                }
            }
        }
    }
    
    fun startHosting(gameConfig: GameConfig) {
        viewModelScope.launch {
            if (!bluetoothService.isBluetoothAvailable()) {
                _uiEvent.send(CreateGameUiEvent.ShowError("Bluetooth no está disponible"))
                return@launch
            }
            
            if (!bluetoothService.hasRequiredPermissions()) {
                _uiEvent.send(CreateGameUiEvent.ShowError("Faltan permisos de Bluetooth"))
                return@launch
            }
            
            val gameId = GameId(generateGameId())
            
            _uiState.update { currentState ->
                currentState.copy(
                    gameConfig = gameConfig,
                    gameId = gameId,
                    isHosting = true,
                    connectionStatus = ConnectionStatus.HOST_DISCONNECTED
                )
            }
            
            val result = bluetoothService.startHosting(gameId, gameConfig.hostName)
            result.fold(
                onSuccess = {
                    // Success handled by network events observer
                },
                onFailure = { error ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isHosting = false,
                            connectionStatus = ConnectionStatus.DISCONNECTED
                        )
                    }
                    _uiEvent.send(CreateGameUiEvent.ShowError(error.message ?: "Error al crear partida"))
                }
            )
        }
    }
    
    fun kickPlayer(playerId: PlayerId) {
        viewModelScope.launch {
            val event = NetworkEvent.PlayerKicked(playerId)
            bluetoothService.sendNetworkEvent(event)
            
            _uiState.update { currentState ->
                val updatedPlayers = currentState.playersInLobby.filterNot { 
                    it.id == playerId 
                }
                currentState.copy(playersInLobby = updatedPlayers)
            }
        }
    }
    
    fun startGame() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val gameConfig = currentState.gameConfig ?: return@launch
            
            val totalPlayers = currentState.playersInLobby.size + 1 // +1 for host
            if (!gameConfig.canStartGame(totalPlayers)) {
                _uiEvent.send(CreateGameUiEvent.ShowError("Necesitas al menos ${GameConfig.MIN_PLAYERS} jugadores para iniciar"))
                return@launch
            }
            
            // Select random word category and words  
            val selectedCategory = gameConfig.selectedCategories.randomOrNull()?.let { categoryId ->
                spanishWordDatabase.getCategoryById(categoryId)
            } ?: run {
                _uiEvent.send(CreateGameUiEvent.ShowError("No hay categorías seleccionadas"))
                return@launch
            }
            
            // Create game session
            val allPlayers = listOf(
                Player(
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
            ) + currentState.playersInLobby
            
            val impostorIndex = allPlayers.indices.random()
            val gameWords: List<String> = selectedCategory.words.shuffled().take(24) // 6x4 grid
            
            val gameSession = GameSession(
                id = currentState.gameId ?: GameId(generateGameId()),
                gameConfig = gameConfig,
                players = allPlayers,
                currentState = GameState.GameStarted,
                currentRound = 1,
                totalRounds = 1,
                impostorId = allPlayers[impostorIndex].id,
                secretWords = gameWords,
                selectedCategory = selectedCategory,
                votes = emptyList(),
                drawings = emptyList()
            )
            
            _uiState.update { currentState ->
                currentState.copy(gameSession = gameSession)
            }
            
            // Notify all players that the game is starting
            val startEvent = NetworkEvent.GameStarted(gameSession)
            bluetoothService.sendNetworkEvent(startEvent)
            
            _uiEvent.send(CreateGameUiEvent.NavigateToGame(gameSession))
        }
    }
    
    fun stopHosting() {
        viewModelScope.launch {
            bluetoothService.stopService()
            _uiState.update { currentState ->
                currentState.copy(
                    isHosting = false,
                    connectionStatus = ConnectionStatus.DISCONNECTED,
                    playersInLobby = emptyList()
                )
            }
        }
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

data class CreateGameUiState(
    val availableCategories: List<WordCategory> = emptyList(),
    val gameConfig: GameConfig? = null,
    val gameId: GameId? = null,
    val isHosting: Boolean = false,
    val connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val playersInLobby: List<Player> = emptyList(),
    val gameSession: GameSession? = null
)

sealed class CreateGameUiEvent {
    object NavigateToLobby : CreateGameUiEvent()
    data class NavigateToGame(val gameSession: GameSession) : CreateGameUiEvent()
    data class ShowError(val message: String) : CreateGameUiEvent()
}