package com.oceloti.lemc.navidadmencan.presentation.designsystem.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.oceloti.lemc.navidadmencan.domain.models.Game
import com.oceloti.lemc.navidadmencan.domain.models.GameCategory
import com.oceloti.lemc.navidadmencan.domain.models.GameDifficulty
import com.oceloti.lemc.navidadmencan.presentation.designsystem.badges.CategoryBadge
import com.oceloti.lemc.navidadmencan.presentation.designsystem.badges.PlayerCountBadge
import com.oceloti.lemc.navidadmencan.ui.theme.AppTheme

/**
 * GameCard - A reusable card component to display game information.
 * 
 * Features:
 * - Displays game icon, name, description, and metadata
 * - Shows category badge and player count
 * - Adapts UI based on installation status
 * - Provides actions: install/play, info
 * - Fully accessible with proper semantics
 * 
 * Usage:
 * GameCard(
 *     game = myGame,
 *     onInstallClick = { gameId -> /* handle install */ },
 *     onPlayClick = { gameId -> /* handle play */ },
 *     onInfoClick = { gameId -> /* show game details */ }
 * )
 */
@Composable
fun GameCard(
    game: Game,
    modifier: Modifier = Modifier,
    onInstallClick: (String) -> Unit = {},
    onPlayClick: (String) -> Unit = {},
    onInfoClick: (String) -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with game icon and info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Game icon
                Box(
                    modifier = Modifier.size(60.dp)
                ) {
                    if (!game.iconUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(game.iconUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "${game.name} icon",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Fallback placeholder
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                // Game details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Game name and category badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CategoryBadge(category = game.category)
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Game description
                    Text(
                        text = game.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Game metadata
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerCountBadge(
                    minPlayers = game.minPlayers,
                    maxPlayers = game.maxPlayers
                )
                
                // Duration
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${game.estimatedDuration}min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Primary action (Install or Play)
                if (game.isInstalled) {
                    FilledTonalButton(
                        onClick = { onPlayClick(game.id) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Play")
                    }
                } else {
                    FilledTonalButton(
                        onClick = { onInstallClick(game.id) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Download,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Install")
                    }
                }
                
                // Info button
                OutlinedButton(
                    onClick = { onInfoClick(game.id) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Game info",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewGameCard() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Installed game
            GameCard(
                game = Game(
                    id = "1",
                    name = "Christmas Trivia",
                    description = "Test your knowledge about Christmas traditions, movies, and songs in this fun family trivia game.",
                    category = GameCategory.TRIVIA,
                    minPlayers = 2,
                    maxPlayers = 8,
                    estimatedDuration = 30,
                    isInstalled = true,
                    iconUrl = null,
                    difficulty = GameDifficulty.EASY
                )
            )
            
            // Not installed game with long name
            GameCard(
                game = Game(
                    id = "2",
                    name = "Epic Christmas Strategy Adventure Game",
                    description = "A complex strategy game where players compete to become the ultimate Christmas party planner. Manage resources, plan events, and outsmart your opponents in this engaging holiday-themed strategy experience.",
                    category = GameCategory.STRATEGY,
                    minPlayers = 3,
                    maxPlayers = 6,
                    estimatedDuration = 90,
                    isInstalled = false,
                    iconUrl = null,
                    difficulty = GameDifficulty.HARD
                )
            )
        }
    }
}