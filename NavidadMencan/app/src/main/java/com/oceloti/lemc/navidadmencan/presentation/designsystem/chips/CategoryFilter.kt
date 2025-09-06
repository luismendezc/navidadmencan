package com.oceloti.lemc.navidadmencan.presentation.designsystem.chips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.navidadmencan.domain.models.GameCategory
import com.oceloti.lemc.navidadmencan.ui.theme.AppTheme

/**
 * CategoryFilter - A horizontal scrollable filter component for game categories.
 * 
 * Features:
 * - Horizontal scrollable row of filter chips
 * - Includes "ALL" option to clear filters
 * - Highlights selected category with Material Design states
 * - Proper content padding for consistent spacing
 * - Accessible with proper semantics
 * 
 * Usage:
 * CategoryFilter(
 *     selectedCategory = selectedCategory,
 *     onCategorySelected = { category -> 
 *         // Handle category selection
 *         // Pass null for "ALL" selection
 *     }
 * )
 */
@Composable
fun CategoryFilter(
    selectedCategory: GameCategory?,
    onCategorySelected: (GameCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        // ALL filter chip
        item {
            FilterChip(
                onClick = { onCategorySelected(null) },
                label = { 
                    Text(
                        text = "ALL",
                        style = MaterialTheme.typography.labelMedium
                    ) 
                },
                selected = selectedCategory == null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
        
        // Category filter chips
        items(GameCategory.values()) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = { 
                    Text(
                        text = category.displayName,
                        style = MaterialTheme.typography.labelMedium
                    ) 
                },
                selected = selectedCategory == category,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
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

@PreviewLightDark
@Composable
fun PreviewCategoryFilter() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            var selectedCategory1 by remember { mutableStateOf<GameCategory?>(null) }
            var selectedCategory2 by remember { mutableStateOf<GameCategory?>(GameCategory.TRIVIA) }
            
            // No selection (ALL selected)
            Text(
                text = "No selection:",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CategoryFilter(
                selectedCategory = selectedCategory1,
                onCategorySelected = { selectedCategory1 = it }
            )
            
            // With selection
            Text(
                text = "Trivia selected:",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CategoryFilter(
                selectedCategory = selectedCategory2,
                onCategorySelected = { selectedCategory2 = it }
            )
        }
    }
}