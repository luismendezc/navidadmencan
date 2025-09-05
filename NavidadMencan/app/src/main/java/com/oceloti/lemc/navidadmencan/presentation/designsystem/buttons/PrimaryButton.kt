package com.oceloti.lemc.navidadmencan.presentation.designsystem.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.navidadmencan.ui.theme.AppTheme

/**
 * A single reusable button:
 * - Optional leading/trailing icons
 * - onClick when enabled
 * - onDisabledClick when disabled (optional)
 * - Disabled colors come from your M3 theme
 */
@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit,
    onDisabledClick: (() -> Unit)? = null,
) {
    // If disabled and we want a special click, capture it in a wrapper
    val wrapperModifier = if (!enabled && onDisabledClick != null) {
        modifier
            .fillMaxWidth()
            .clip(ButtonDefaults.shape)
            .clickable(
                enabled = true,
                role = Role.Button,
                onClick = onDisabledClick
            )
    } else {
        modifier
            .fillMaxWidth()
            .clip(ButtonDefaults.shape)
    }

    Box(wrapperModifier) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (leadingIcon != null) {
                    Icon(leadingIcon, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelLarge
                )

                if (trailingIcon != null) {
                    Spacer(Modifier.width(8.dp))
                    Icon(trailingIcon, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }
        }
        // 🔒 Overlay clickeable SOLO cuando está deshabilitado
        if (!enabled && onDisabledClick != null) {
            val interaction = remember { MutableInteractionSource() }
            Box(
                Modifier
                    .matchParentSize()
                    .clip(ButtonDefaults.shape)
                    .clickable(
                        interactionSource = interaction,
                        indication = null,        // sin ripple para que se vea “disabled”
                        enabled = true,
                        role = Role.Button,
                        onClick = onDisabledClick
                    )
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewPrimaryButton() {
    AppTheme {
        Column {
            PrimaryButton(
                text = "Testing",
                modifier = Modifier,
                enabled = true,
                onClick = {})
            PrimaryButton(
                text = "Testing",
                modifier = Modifier,
                leadingIcon = Icons.Filled.Settings,
                enabled = true,
                onClick = {})
            PrimaryButton(
                text = "Testing",
                modifier = Modifier,
                trailingIcon = Icons.Filled.Brush,
                enabled = false,
                onClick = {})
        }
    }
}
