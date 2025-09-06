package com.oceloti.lemc.games.impostor.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * Adaptive layout that responds to screen width
 */
@Composable
fun ResponsiveGameLayout(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    when {
        screenWidth < 600.dp -> {
            // Phone layout - single column with standard padding
            content(PaddingValues(16.dp))
        }
        screenWidth < 840.dp -> {
            // Large phone/small tablet - increased padding
            content(PaddingValues(24.dp))
        }
        else -> {
            // Tablet - max width with centering
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.widthIn(max = 800.dp)) {
                    content(PaddingValues(32.dp))
                }
            }
        }
    }
}

/**
 * Adaptive word grid that adjusts based on screen size and orientation
 */
@Composable
fun AdaptiveWordGrid(
    words: List<String>,
    onWordClick: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    
    when {
        // Large screens or landscape - use full 6x4 grid
        screenWidth >= 600.dp || isLandscape -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
            ) {
                items(words.take(24)) { word ->
                    WordCard(
                        word = word,
                        onClick = onWordClick?.let { { it(word) } }
                    )
                }
            }
        }
        
        // Medium screens - 4 columns
        screenWidth >= 400.dp -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
            ) {
                items(words.take(24)) { word ->
                    WordCard(
                        word = word,
                        onClick = onWordClick?.let { { it(word) } }
                    )
                }
            }
        }
        
        // Small screens - 3 columns
        else -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = modifier
            ) {
                items(words.take(24)) { word ->
                    WordCard(
                        word = word,
                        onClick = onWordClick?.let { { it(word) } }
                    )
                }
            }
        }
    }
}

/**
 * Player validation constants and utilities
 */
object GameConstants {
    const val MIN_PLAYERS = 3
    const val MAX_PLAYERS = 12
    const val DEFAULT_LIVES = 3
    const val MIN_LIVES = 1
    const val MAX_LIVES = 10
    
    fun validatePlayerCount(count: Int): Boolean = count in MIN_PLAYERS..MAX_PLAYERS
    fun getPlayerCountRequirement(): String = "Mínimo $MIN_PLAYERS jugadores (incluyendo anfitrión)"
    fun validateLives(lives: Int): Boolean = lives in MIN_LIVES..MAX_LIVES
}

/**
 * Word grid for the impostor game with forced landscape mode
 */
@Composable
fun ImpostorWordGrid(
    words: List<String>,
    category: String,
    isImpostor: Boolean,
    onGridViewed: () -> Unit,
    modifier: Modifier = Modifier
) {
    ResponsiveGameLayout(modifier = modifier) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category header
            GameInfoCard(
                title = if (isImpostor) "¡Eres el Impostor!" else "Categoría: $category"
            ) {
                if (isImpostor) {
                    androidx.compose.material3.Text(
                        text = "Tu objetivo es adivinar la palabra secreta observando las pistas de los demás jugadores.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    androidx.compose.material3.Text(
                        text = "Estas son las palabras posibles. Dibuja pistas sin ser obvio para que el impostor no adivine.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (!isImpostor) {
                AdaptiveWordGrid(
                    words = words,
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Impostor sees category hint only
                GameInfoCard(
                    title = "Pista de Categoría"
                ) {
                    androidx.compose.material3.Text(
                        text = "Las palabras están relacionadas con: $category",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            androidx.compose.material3.FilledTonalButton(
                onClick = onGridViewed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                androidx.compose.material3.Text("Continuar al Juego")
            }
        }
    }
}

/**
 * Orientation-aware layout helper
 */
@Composable
fun OrientationAwareLayout(
    content: @Composable (Boolean) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    content(isLandscape)
}