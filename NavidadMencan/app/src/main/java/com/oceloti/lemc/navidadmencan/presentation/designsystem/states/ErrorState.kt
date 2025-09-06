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
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.WifiOff
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
 * ErrorState - A reusable error display component with retry functionality.
 * 
 * Features:
 * - Displays error icon, message, and optional retry button
 * - Supports different error types with appropriate icons
 * - Consistent styling and layout
 * - Proper accessibility support
 * - Customizable actions and messages
 * 
 * Usage:
 * ErrorState(
 *     message = "Failed to load games",
 *     onRetry = { /* handle retry */ }
 * )
 * 
 * ErrorState(
 *     message = "No internet connection",
 *     errorType = ErrorType.Network,
 *     onRetry = { /* handle retry */ }
 * )
 */
@Composable
fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    errorType: ErrorType = ErrorType.Generic,
    onRetry: (() -> Unit)? = null,
    retryButtonText: String = "Try Again"
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
            // Error icon
            Icon(
                imageVector = errorType.icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Error message
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            
            // Optional retry button
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(24.dp))
                PrimaryButton(
                    text = retryButtonText,
                    onClick = onRetry,
                    leadingIcon = Icons.Outlined.Refresh
                )
            }
        }
    }
}

/**
 * Compact variant of ErrorState for smaller spaces
 */
@Composable
fun ErrorStateCompact(
    message: String,
    modifier: Modifier = Modifier,
    errorType: ErrorType = ErrorType.Generic,
    onRetry: (() -> Unit)? = null,
    retryButtonText: String = "Retry"
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = errorType.icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = retryButtonText,
                onClick = onRetry
            )
        }
    }
}

/**
 * Different types of errors with appropriate icons
 */
enum class ErrorType(val icon: ImageVector) {
    Generic(Icons.Outlined.ErrorOutline),
    Network(Icons.Outlined.WifiOff),
    Server(Icons.Outlined.ErrorOutline)
}

@PreviewLightDark
@Composable
fun PreviewErrorState() {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Generic error with retry
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxSize()
            ) {
                ErrorState(
                    message = "Something went wrong while loading the games. Please try again.",
                    onRetry = { /* Handle retry */ }
                )
            }
            
            // Network error
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxSize()
            ) {
                ErrorState(
                    message = "No internet connection available. Check your network settings.",
                    errorType = ErrorType.Network,
                    onRetry = { /* Handle retry */ },
                    retryButtonText = "Check Connection"
                )
            }
            
            // Compact variant
            Text(
                text = "Compact variant:",
                style = MaterialTheme.typography.titleMedium
            )
            
            ErrorStateCompact(
                message = "Failed to install game",
                onRetry = { /* Handle retry */ }
            )
        }
    }
}