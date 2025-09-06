package com.oceloti.lemc.games.impostor.presentation.views

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.games.impostor.domain.models.*
import com.oceloti.lemc.games.impostor.presentation.components.*
import kotlinx.coroutines.delay

/**
 * Word revelation screen that shows the 6x4 grid to non-impostor players
 * and category hints to the impostor
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordRevealScreen(
    words: List<String>,
    category: WordCategory,
    isImpostor: Boolean,
    playerName: String,
    remainingTime: Int = 30,
    onNavigateBack: () -> Unit = {},
    onTimeUp: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Force landscape orientation for optimal word grid display
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    
    // Animation states
    var isVisible by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }
    
    // Timer effect
    LaunchedEffect(remainingTime) {
        if (remainingTime <= 0) {
            onTimeUp()
        }
    }
    
    // Entrance animation
    LaunchedEffect(Unit) {
        delay(300) // Brief suspense
        isVisible = true
        delay(500) // Wait for fade in
        showContent = true
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar with timer
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Revelación de Palabras")
                    
                    // Timer badge
                    if (remainingTime > 0) {
                        Badge(
                            containerColor = if (remainingTime <= 10) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        ) {
                            Text("${remainingTime}s")
                        }
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            }
        )
        
        ResponsiveGameLayout {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(800)) + scaleIn(animationSpec = tween(800)),
                exit = fadeOut() + scaleOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Player role indicator
                    RoleIndicatorCard(
                        isImpostor = isImpostor,
                        playerName = playerName,
                        category = category
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Content based on role
                    AnimatedVisibility(
                        visible = showContent,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(600)
                        ) + fadeIn(animationSpec = tween(600))
                    ) {
                        if (isImpostor) {
                            ImpostorView(category = category)
                        } else {
                            WordGridView(
                                words = words,
                                category = category,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Orientation recommendation for small screens
                    if (!isLandscape && configuration.screenWidthDp < 600) {
                        OrientationRecommendationCard()
                    }
                }
            }
        }
    }
}

@Composable
private fun RoleIndicatorCard(
    isImpostor: Boolean,
    playerName: String,
    category: WordCategory
) {
    GameInfoCard(
        title = if (isImpostor) "🕵️ ¡Eres el Impostor, $playerName!" else "👥 Eres un Aliado, $playerName"
    ) {
        Text(
            text = if (isImpostor) {
                "Tu misión: Descubre la palabra secreta observando las pistas de los demás. " +
                "Si te votan, tendrás una oportunidad de adivinar para ganar."
            } else {
                "Tu misión: Da pistas sobre la palabra sin ser demasiado obvio. " +
                "Identifica al impostor y vótalo para ganar."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = if (isImpostor) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
        
        if (isImpostor) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "💡 Pista: La categoría es \"${category.name}\"",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun WordGridView(
    words: List<String>,
    category: WordCategory,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Category info
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = "📚 ${category.name}",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Word grid
        AdaptiveWordGrid(
            words = words,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ImpostorView(
    category: WordCategory
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Large impostor indicator
        Text(
            text = "🕵️‍♂️",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Category hint card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Tu Única Pista",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Strategy tip
        GameInfoCard(title = "💡 Estrategia") {
            Text(
                text = "• Observa las pistas de los demás\n" +
                        "• Actúa como si supieras la palabra\n" +
                        "• Da pistas vagas pero creíbles\n" +
                        "• Si te votan, ¡tendrás tu oportunidad!",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun OrientationRecommendationCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📱",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Para una mejor experiencia, rota tu dispositivo horizontalmente",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

// Preview composables
@Preview(showBackground = true)
@Composable
fun WordRevealScreenPreview() {
    MaterialTheme {
        WordRevealScreen(
            words = listOf(
                "Perro", "Gato", "León", "Tigre", "Elefante", "Jirafa",
                "Mono", "Oso", "Lobo", "Zorro", "Conejo", "Caballo",
                "Vaca", "Cerdo", "Oveja", "Pollo", "Pato", "Ganso",
                "Águila", "Halcón", "Colibrí", "Loro", "Canario", "Pavo"
            ),
            category = WordCategory(
                id = WordCategoryId("animals"),
                name = "Animales",
                description = "Diferentes tipos de animales domésticos y salvajes",
                words = emptyList(),
                difficulty = Difficulty.EASY
            ),
            isImpostor = false,
            playerName = "María",
            remainingTime = 25
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WordRevealScreenImpostorPreview() {
    MaterialTheme {
        WordRevealScreen(
            words = emptyList(),
            category = WordCategory(
                id = WordCategoryId("animals"),
                name = "Animales",
                description = "Diferentes tipos de animales domésticos y salvajes",
                words = emptyList(),
                difficulty = Difficulty.EASY
            ),
            isImpostor = true,
            playerName = "Carlos",
            remainingTime = 15
        )
    }
}