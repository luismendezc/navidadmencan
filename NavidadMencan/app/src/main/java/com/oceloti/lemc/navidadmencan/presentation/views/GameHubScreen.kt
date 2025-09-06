package com.oceloti.lemc.navidadmencan.presentation.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oceloti.lemc.navidadmencan.domain.models.Game
import com.oceloti.lemc.navidadmencan.domain.models.GameCategory
import com.oceloti.lemc.navidadmencan.domain.models.GameDifficulty
import com.oceloti.lemc.navidadmencan.presentation.designsystem.cards.GameCard
import com.oceloti.lemc.navidadmencan.presentation.designsystem.chips.CategoryFilter
import com.oceloti.lemc.navidadmencan.presentation.designsystem.states.EmptyState
import com.oceloti.lemc.navidadmencan.presentation.designsystem.states.ErrorState
import com.oceloti.lemc.navidadmencan.presentation.designsystem.states.LoadingState
import com.oceloti.lemc.navidadmencan.presentation.util.ObserveAsEvents
import com.oceloti.lemc.navidadmencan.presentation.viewmodels.GameHubEvent
import com.oceloti.lemc.navidadmencan.presentation.viewmodels.GameHubState
import com.oceloti.lemc.navidadmencan.presentation.viewmodels.GameHubViewModel
import com.oceloti.lemc.navidadmencan.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

/**
 * ROOT: connect VM, listen events and decide navigation/side-effects.
 */
@Composable
fun GameHubScreenRoot(
    onNavigateToGame: (Game) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: GameHubViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Manage one-shot events
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current
    
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is GameHubEvent.NavigateToGame -> onNavigateToGame(event.game)
            is GameHubEvent.ShowError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            GameHubEvent.ShowGameInstalled -> {
                Toast.makeText(context, "Game installed!", Toast.LENGTH_SHORT).show()
            }
            GameHubEvent.ShowGameUninstalled -> {
                Toast.makeText(context, "Game uninstalled!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    GameHubScreen(
        state = state,
        onCategorySelected = viewModel::filterByCategory,
        onGameInstall = viewModel::installGame,
        onGameUninstall = viewModel::uninstallGame,
        onGamePlay = viewModel::onGameSelected,
        onGameInfo = viewModel::onGameSelected,
        onRetry = viewModel::loadGames,
        onErrorDismiss = viewModel::clearError,
        modifier = modifier
    )
}

@Composable
fun GameHubScreen(
    state: GameHubState,
    onCategorySelected: (GameCategory?) -> Unit,
    onGameInstall: (Game) -> Unit,
    onGameUninstall: (Game) -> Unit,
    onGamePlay: (Game) -> Unit,
    onGameInfo: (Game) -> Unit,
    onRetry: () -> Unit,
    onErrorDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "🎮 Game Hub",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Choose a game to play with family",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Category Filter
        CategoryFilter(
            selectedCategory = state.selectedCategory,
            onCategorySelected = onCategorySelected,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on state
        when {
            state.isLoading -> {
                LoadingState(
                    message = "Loading games...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            
            state.error != null -> {
                ErrorState(
                    message = state.error,
                    onRetry = {
                        onErrorDismiss()
                        onRetry()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            
            state.games.isEmpty() -> {
                EmptyState(
                    message = if (state.selectedCategory != null) {
                        "No games found in this category"
                    } else {
                        "No games available"
                    },
                    description = "Try selecting a different category or check back later",
                    actionText = "Show All",
                    onAction = { onCategorySelected(null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(state.games) { game ->
                        GameCard(
                            game = game,
                            onInstallClick = { 
                                if (game.isInstalled) {
                                    onGameUninstall(game)
                                } else {
                                    onGameInstall(game)
                                }
                            },
                            onPlayClick = { onGamePlay(game) },
                            onInfoClick = { onGameInfo(game) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun GameHubScreenPreview() {
    val sampleGames = listOf(
        Game(
            id = "uno",
            name = "UNO Navideño",
            description = "El clásico juego de cartas UNO con temática navideña",
            category = GameCategory.CARD,
            minPlayers = 2,
            maxPlayers = 10,
            estimatedDuration = 30,
            isInstalled = true,
            difficulty = GameDifficulty.EASY
        ),
        Game(
            id = "trivia",
            name = "Trivia Navideña",
            description = "Preguntas y respuestas sobre la Navidad",
            category = GameCategory.TRIVIA,
            minPlayers = 2,
            maxPlayers = 8,
            estimatedDuration = 45,
            isInstalled = false,
            difficulty = GameDifficulty.MEDIUM
        )
    )

    AppTheme {
        GameHubScreen(
            state = GameHubState(
                games = sampleGames,
                isLoading = false,
                selectedCategory = null,
                error = null
            ),
            onCategorySelected = {},
            onGameInstall = {},
            onGameUninstall = {},
            onGamePlay = {},
            onGameInfo = {},
            onRetry = {},
            onErrorDismiss = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@PreviewLightDark
@Composable
fun GameHubScreenLoadingPreview() {
    AppTheme {
        GameHubScreen(
            state = GameHubState(isLoading = true),
            onCategorySelected = {},
            onGameInstall = {},
            onGameUninstall = {},
            onGamePlay = {},
            onGameInfo = {},
            onRetry = {},
            onErrorDismiss = {}
        )
    }
}