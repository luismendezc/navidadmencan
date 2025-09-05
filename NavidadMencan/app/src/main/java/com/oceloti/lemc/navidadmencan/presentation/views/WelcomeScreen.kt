package com.oceloti.lemc.navidadmencan.presentation.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oceloti.lemc.navidadmencan.presentation.designsystem.buttons.PrimaryButton
import com.oceloti.lemc.navidadmencan.presentation.util.ObserveAsEvents
import com.oceloti.lemc.navidadmencan.presentation.viewmodels.WelcomeAction
import com.oceloti.lemc.navidadmencan.presentation.viewmodels.WelcomeEvent
import com.oceloti.lemc.navidadmencan.presentation.viewmodels.WelcomeState
import com.oceloti.lemc.navidadmencan.presentation.viewmodels.WelcomeViewModel
import com.oceloti.lemc.navidadmencan.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel


/**
 * ROOT: connect VM, listen events and decide navigation/side-effects.
 */
@Composable
fun WelcomeScreenRoot(
    onNavigateDashboard: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Manage one-shot events
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is WelcomeEvent.NavigateToDashboard -> onNavigateDashboard()
            is WelcomeEvent.ShowMessage -> Toast.makeText(
                context,
                event.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    WelcomeScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    )
}

@Composable
fun WelcomeScreen(
    state: WelcomeState,
    onAction: (WelcomeAction) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state.title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(24.dp))
        // Tu botón de DS (iconos opcionales, disabled click soportado)
        PrimaryButton(
            text = "Start",
            enabled = !state.isLoading,
            leadingIcon = null,
            trailingIcon = null,
            onClick = { onAction(WelcomeAction.OnStartClicked) },
            onDisabledClick = { onAction(WelcomeAction.OnDisabledClicked("Welcome Screen")) }
        )

        if (state.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }

    }
}

@PreviewLightDark
@Composable
fun WelcomeScreenTest() {
    AppTheme {
        WelcomeScreen(WelcomeState("Testing"), onAction = {}, Modifier)
    }
}

