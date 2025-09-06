package com.oceloti.lemc.navidadmencan.presentation.designsystem.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.navidadmencan.ui.theme.AppTheme

/**
 * LoadingState - A reusable loading indicator component.
 * 
 * Features:
 * - Centered circular progress indicator
 * - Optional customizable loading message
 * - Proper accessibility semantics
 * - Consistent styling across the app
 * - Can be used as overlay or standalone state
 * 
 * Usage:
 * LoadingState() // Default "Loading..." message
 * LoadingState(message = "Installing game...") // Custom message
 * LoadingState(message = null) // No message, just indicator
 */
@Composable
fun LoadingState(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .semantics {
                        contentDescription = message
                    },
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
            
            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Compact variant of LoadingState for smaller spaces
 * Shows a smaller progress indicator with optional text
 */
@Composable
fun LoadingStateCompact(
    message: String = "",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(24.dp)
                .semantics {
                    contentDescription = if (message.isNotEmpty()) message else "Loading"
                },
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
        
        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewLoadingState() {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Default loading state
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxSize()
            ) {
                LoadingState()
            }
            
            // Custom message
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxSize()
            ) {
                LoadingState(message = "Installing Christmas Trivia...")
            }
            
            // Compact variants
            Text(
                text = "Compact variants:",
                style = MaterialTheme.typography.titleMedium
            )
            
            LoadingStateCompact()
            LoadingStateCompact(message = "Loading games...")
        }
    }
}