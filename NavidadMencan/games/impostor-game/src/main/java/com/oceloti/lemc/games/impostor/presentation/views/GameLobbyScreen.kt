package com.oceloti.lemc.games.impostor.presentation.views

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.games.impostor.domain.models.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameLobbyScreen(
    gameConfig: GameConfig,
    players: List<Player> = emptyList(),
    gameId: GameId = GameId(""),
    connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    onNavigateBack: () -> Unit = {},
    onStartGame: () -> Unit = {},
    onKickPlayer: (PlayerId) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { 
                Text("Sala de Espera")
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Cancelar partida"
                    )
                }
            }
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Connection Status
            ConnectionStatusCard(
                status = connectionStatus,
                gameId = gameId
            )
            
            // Game Configuration Summary
            GameConfigCard(
                config = gameConfig,
                playerCount = players.size + 1 // +1 for host
            )
            
            // Players List
            PlayersCard(
                hostName = gameConfig.hostName,
                players = players,
                onKickPlayer = onKickPlayer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Start Game Button
            FilledTonalButton(
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = connectionStatus == ConnectionStatus.CONNECTED && 
                         gameConfig.canStartGame(players.size + 1) // +1 for host
            ) {
                Text(
                    text = "Iniciar Partida",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // Requirements info
            val totalPlayers = players.size + 1 // +1 for host
            if (connectionStatus != ConnectionStatus.CONNECTED || !gameConfig.canStartGame(totalPlayers)) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Requisitos para iniciar:",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        if (connectionStatus != ConnectionStatus.CONNECTED) {
                            Text(
                                text = "• Esperando conexión Bluetooth...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        
                        if (!gameConfig.canStartGame(totalPlayers)) {
                            Text(
                                text = "• Mínimo ${GameConfig.MIN_PLAYERS} jugadores (incluyendo anfitrión)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConnectionStatusCard(
    status: ConnectionStatus,
    gameId: GameId
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (status) {
                ConnectionStatus.CONNECTED -> MaterialTheme.colorScheme.primaryContainer
                ConnectionStatus.DISCONNECTED -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (status) {
                    ConnectionStatus.CONNECTED -> Icons.Default.Bluetooth
                    ConnectionStatus.DISCONNECTED -> Icons.Default.BluetoothDisabled
                    else -> Icons.Default.BluetoothSearching
                },
                contentDescription = null,
                tint = when (status) {
                    ConnectionStatus.CONNECTED -> MaterialTheme.colorScheme.onPrimaryContainer
                    ConnectionStatus.DISCONNECTED -> MaterialTheme.colorScheme.onErrorContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = when (status) {
                        ConnectionStatus.CONNECTED -> "Bluetooth Conectado"
                        ConnectionStatus.DISCONNECTED -> "Bluetooth Desconectado"
                        ConnectionStatus.HOST_DISCONNECTED -> "Alojando partida..."
                        else -> "Conectando..."
                    },
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = when (status) {
                        ConnectionStatus.CONNECTED -> MaterialTheme.colorScheme.onPrimaryContainer
                        ConnectionStatus.DISCONNECTED -> MaterialTheme.colorScheme.onErrorContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                
                if (gameId.value.isNotEmpty()) {
                    Text(
                        text = "ID: ${gameId.value.take(8)}...",
                        style = MaterialTheme.typography.bodySmall,
                        color = when (status) {
                            ConnectionStatus.CONNECTED -> MaterialTheme.colorScheme.onPrimaryContainer
                            ConnectionStatus.DISCONNECTED -> MaterialTheme.colorScheme.onErrorContainer
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GameConfigCard(
    config: GameConfig,
    playerCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Configuración de la Partida",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Vidas por jugador",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${config.livesPerPlayer}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                Column {
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${config.selectedCategories.size}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                Column {
                    Text(
                        text = "Jugadores",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$playerCount/${config.maxPlayers}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayersCard(
    hostName: String,
    players: List<Player>,
    onKickPlayer: (PlayerId) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Jugadores en la Sala",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Host (always first)
            PlayerItem(
                name = "$hostName (Anfitrión)",
                isHost = true,
                onKick = null
            )
            
            // Other players
            players.forEach { player ->
                Spacer(modifier = Modifier.height(8.dp))
                PlayerItem(
                    name = player.name,
                    isHost = false,
                    onKick = { onKickPlayer(player.id) }
                )
            }
            
            if (players.isEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Esperando que otros jugadores se unan...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun PlayerItem(
    name: String,
    isHost: Boolean,
    onKick: (() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isHost) Icons.Default.Star else Icons.Default.Person,
                contentDescription = null,
                tint = if (isHost) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isHost) FontWeight.Bold else FontWeight.Normal
                ),
                color = if (isHost) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
        
        if (onKick != null) {
            IconButton(
                onClick = onKick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Expulsar jugador",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameLobbyScreenPreview() {
    MaterialTheme {
        GameLobbyScreen(
            gameConfig = GameConfig(
                hostName = "María",
                livesPerPlayer = 3,
                selectedCategories = listOf(
                    WordCategoryId("animals"),
                    WordCategoryId("food")
                ),
                maxPlayers = 8
            ),
            players = listOf(
                Player(
                    id = PlayerId("1"),
                    name = "Juan",
                    isHost = false,
                    lives = 3,
                    connectionType = ConnectionType.BLUETOOTH,
                    deviceInfo = DeviceInfo(
                        platform = Platform.ANDROID,
                        deviceName = "Juan's Phone",
                        version = "1.0"
                    )
                ),
                Player(
                    id = PlayerId("2"),
                    name = "Ana",
                    isHost = false,
                    lives = 3,
                    connectionType = ConnectionType.LAN,
                    deviceInfo = DeviceInfo(
                        platform = Platform.IOS,
                        deviceName = "Ana's iPhone",
                        version = "1.0"
                    )
                )
            ),
            gameId = GameId("abc123def456"),
            connectionStatus = ConnectionStatus.CONNECTED
        )
    }
}