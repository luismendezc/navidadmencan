package com.oceloti.lemc.navidadmencan.presentation.designsystem.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.navidadmencan.presentation.designsystem.buttons.PrimaryButton
import com.oceloti.lemc.navidadmencan.ui.theme.AppTheme

/**
 * EmptyState - A reusable empty state component for when no content is available.
 * 
 * Features:
 * - Displays appropriate icon, message, and optional action button
 * - Supports different empty state types with contextual icons
 * - Consistent styling and layout
 * - Proper accessibility support
 * - Customizable actions and messages
 * 
 * Usage:
 * EmptyState(
 *     message = "No games found",
 *     emptyType = EmptyType.NoGames,
 *     actionText = "Browse Store",
 *     onAction = { /* handle action */ }
 * )
 */
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    emptyType: EmptyType = EmptyType.NoResults,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
    description: String? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Empty state icon
            Icon(
                imageVector = emptyType.icon,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Main message
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            
            // Optional description
            if (!description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            
            // Optional action button
            if (actionText != null && onAction != null) {
                Spacer(modifier = Modifier.height(24.dp))
                PrimaryButton(
                    text = actionText,
                    onClick = onAction
                )
            }
        }
    }
}

/**
 * Compact variant of EmptyState for smaller spaces
 */
@Composable
fun EmptyStateCompact(
    message: String,
    modifier: Modifier = Modifier,
    emptyType: EmptyType = EmptyType.NoResults,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = emptyType.icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        if (actionText != null && onAction != null) {
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = actionText,
                onClick = onAction
            )
        }
    }
}

/**
 * Different types of empty states with appropriate icons
 */
enum class EmptyType(val icon: ImageVector) {
    NoGames(Icons.Outlined.SportsEsports),
    NoResults(Icons.Outlined.SearchOff),
    NoSearch(Icons.Outlined.Search),
    NoFavorites(Icons.Outlined.Casino)
}

@PreviewLightDark
@Composable
fun PreviewEmptyState() {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // No games found
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxSize()
            ) {
                EmptyState(
                    message = "No games found",
                    description = "Try adjusting your filters or check back later for new games.",
                    emptyType = EmptyType.NoGames,
                    actionText = "Clear Filters",
                    onAction = { /* Handle action */ }
                )
            }
            
            // No search results
            Box(
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxSize()
            ) {
                EmptyState(
                    message = "No results for \"Christmas Poker\"",
                    description = "Try a different search term or browse our game categories.",
                    emptyType = EmptyType.NoResults,
                    actionText = "Browse Categories",
                    onAction = { /* Handle action */ }
                )
            }
            
            // Compact variant
            Text(
                text = "Compact variant:",
                style = MaterialTheme.typography.titleMedium
            )
            
            EmptyStateCompact(
                message = "No favorite games",
                emptyType = EmptyType.NoFavorites,
                actionText = "Browse Games",
                onAction = { /* Handle action */ }
            )
        }
    }
}