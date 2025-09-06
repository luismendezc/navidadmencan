package com.oceloti.lemc.games.impostor.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Reusable game menu button following Material Design patterns
 */
@Composable
fun GameMenuButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonType: GameButtonType = GameButtonType.Primary
) {
    when (buttonType) {
        GameButtonType.Primary -> {
            FilledTonalButton(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                ButtonContent(icon = icon, text = text)
            }
        }
        GameButtonType.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                ButtonContent(icon = icon, text = text)
            }
        }
    }
}

@Composable
private fun ButtonContent(icon: ImageVector, text: String) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium
    )
}

enum class GameButtonType {
    Primary,
    Secondary
}

/**
 * Reusable info card for game configuration and instructions
 */
@Composable
fun GameInfoCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            content()
        }
    }
}

/**
 * Counter component for lives, players, etc.
 */
@Composable
fun GameCounter(
    label: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    minValue: Int = 1,
    maxValue: Int = 10,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = onDecrement,
                enabled = enabled && count > minValue
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Reducir $label")
            }
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "$count",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            IconButton(
                onClick = onIncrement,
                enabled = enabled && count < maxValue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Aumentar $label")
            }
        }
    }
}

/**
 * Requirements validation card
 */
@Composable
fun RequirementsCard(
    requirements: List<String>,
    modifier: Modifier = Modifier,
    title: String = "Requisitos faltantes:",
    isError: Boolean = true
) {
    if (requirements.isNotEmpty()) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isError) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.secondaryContainer
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isError) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    }
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                requirements.forEach { requirement ->
                    Text(
                        text = "• $requirement",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isError) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        }
                    )
                }
            }
        }
    }
}

/**
 * Word card for the 6x4 grid display
 */
@Composable
fun WordCard(
    word: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.then(
            if (onClick != null) {
                Modifier.size(80.dp) // Fixed size for grid consistency
            } else {
                Modifier.aspectRatio(1f)
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        ),
        onClick = onClick ?: {}
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Text(
                text = word,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

/**
 * Connection status indicator
 */
@Composable
fun ConnectionStatusIndicator(
    status: com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus,
    gameId: String = "",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (status) {
                com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.CONNECTED -> 
                    MaterialTheme.colorScheme.primaryContainer
                com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.DISCONNECTED -> 
                    MaterialTheme.colorScheme.errorContainer
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
                    com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.CONNECTED -> 
                        Icons.Default.Add // TODO: Use Bluetooth icon
                    com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.DISCONNECTED -> 
                        Icons.Default.Add // TODO: Use BluetoothDisabled icon
                    else -> Icons.Default.Add // TODO: Use BluetoothSearching icon
                },
                contentDescription = null,
                tint = when (status) {
                    com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.CONNECTED -> 
                        MaterialTheme.colorScheme.onPrimaryContainer
                    com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.DISCONNECTED -> 
                        MaterialTheme.colorScheme.onErrorContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = when (status) {
                        com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.CONNECTED -> "Bluetooth Conectado"
                        com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.DISCONNECTED -> "Bluetooth Desconectado"
                        com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.HOST_DISCONNECTED -> "Alojando partida..."
                        else -> "Conectando..."
                    },
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = when (status) {
                        com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.CONNECTED -> 
                            MaterialTheme.colorScheme.onPrimaryContainer
                        com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.DISCONNECTED -> 
                            MaterialTheme.colorScheme.onErrorContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                
                if (gameId.isNotEmpty()) {
                    Text(
                        text = "ID: ${gameId.take(8)}...",
                        style = MaterialTheme.typography.bodySmall,
                        color = when (status) {
                            com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.CONNECTED -> 
                                MaterialTheme.colorScheme.onPrimaryContainer
                            com.oceloti.lemc.games.impostor.domain.models.ConnectionStatus.DISCONNECTED -> 
                                MaterialTheme.colorScheme.onErrorContainer
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}