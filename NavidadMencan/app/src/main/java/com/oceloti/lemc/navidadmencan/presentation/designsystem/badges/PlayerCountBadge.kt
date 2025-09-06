package com.oceloti.lemc.navidadmencan.presentation.designsystem.badges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.navidadmencan.ui.theme.AppTheme

/**
 * PlayerCountBadge - A compact component to display player count range.
 * 
 * Features:
 * - Shows player count range (e.g., "2-4 players")
 * - Handles single player count (e.g., "4 players")
 * - Includes player icon for visual clarity
 * - Consistent styling with Material Design
 * 
 * Usage:
 * PlayerCountBadge(minPlayers = 2, maxPlayers = 4)
 * PlayerCountBadge(minPlayers = 1, maxPlayers = 1) // Shows "1 player"
 */
@Composable
fun PlayerCountBadge(
    minPlayers: Int,
    maxPlayers: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Group,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = formatPlayerCount(minPlayers, maxPlayers),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Formats the player count text based on min and max values
 */
private fun formatPlayerCount(minPlayers: Int, maxPlayers: Int): String {
    return when {
        minPlayers == maxPlayers -> {
            if (minPlayers == 1) "$minPlayers player" else "$minPlayers players"
        }
        else -> {
            "$minPlayers-$maxPlayers players"
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewPlayerCountBadge() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Various player count scenarios
            PlayerCountBadge(minPlayers = 1, maxPlayers = 1)
            PlayerCountBadge(minPlayers = 2, maxPlayers = 4)
            PlayerCountBadge(minPlayers = 3, maxPlayers = 6)
            PlayerCountBadge(minPlayers = 4, maxPlayers = 8)
            PlayerCountBadge(minPlayers = 6, maxPlayers = 6)
        }
    }
}