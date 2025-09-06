package com.oceloti.lemc.navidadmencan.presentation.designsystem.badges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.navidadmencan.domain.models.GameCategory
import com.oceloti.lemc.navidadmencan.ui.theme.AppTheme

/**
 * CategoryBadge - A small badge component to display game categories.
 * 
 * Features:
 * - Color-coded background based on category type
 * - Compact design suitable for cards and lists
 * - Consistent typography and styling
 * 
 * Usage:
 * CategoryBadge(category = GameCategory.TRIVIA)
 */
@Composable
fun CategoryBadge(
    category: GameCategory,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = getCategoryColors(category)
    
    Text(
        text = category.displayName,
        style = MaterialTheme.typography.labelSmall,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

/**
 * Extension property to get display names for categories
 */
private val GameCategory.displayName: String
    get() = when (this) {
        GameCategory.CARD -> "Card"
        GameCategory.BOARD -> "Board"
        GameCategory.TRIVIA -> "Trivia"
        GameCategory.PARTY -> "Party"
        GameCategory.STRATEGY -> "Strategy"
        GameCategory.PUZZLE -> "Puzzle"
    }

/**
 * Get appropriate colors for each category
 */
@Composable
private fun getCategoryColors(category: GameCategory): Pair<Color, Color> {
    return when (category) {
        GameCategory.CARD -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        GameCategory.BOARD -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        GameCategory.TRIVIA -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        GameCategory.PARTY -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        GameCategory.STRATEGY -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
        GameCategory.PUZZLE -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f) to MaterialTheme.colorScheme.onPrimaryContainer
    }
}

@PreviewLightDark
@Composable
fun PreviewCategoryBadge() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            GameCategory.values().forEach { category ->
                CategoryBadge(category = category)
            }
        }
    }
}