package com.oceloti.lemc.games.impostor.presentation.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.games.impostor.domain.models.*
import com.oceloti.lemc.games.impostor.presentation.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGameScreen(
    onNavigateBack: () -> Unit = {},
    onStartHosting: (GameConfig) -> Unit = {},
    availableCategories: List<WordCategory> = emptyList()
) {
    var hostName by remember { mutableStateOf("") }
    var livesPerPlayer by remember { mutableIntStateOf(3) }
    var selectedCategories by remember { mutableStateOf(setOf<WordCategoryId>()) }
    var showCategorySelector by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { 
                Text("Crear Partida")
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
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Host Name Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Nombre del Anfitrión",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = hostName,
                        onValueChange = { hostName = it },
                        label = { Text("Tu nombre") },
                        placeholder = { Text("Ej: María") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
            
            // Lives Configuration Section
            GameInfoCard(
                title = "Vidas por Jugador"
            ) {
                Text(
                    text = "Los jugadores serán eliminados al perder todas sus vidas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                GameCounter(
                    label = "",
                    count = livesPerPlayer,
                    onIncrement = { if (livesPerPlayer < GameConstants.MAX_LIVES) livesPerPlayer++ },
                    onDecrement = { if (livesPerPlayer > GameConstants.MIN_LIVES) livesPerPlayer-- },
                    minValue = GameConstants.MIN_LIVES,
                    maxValue = GameConstants.MAX_LIVES,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            
            // Word Categories Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Categorías de Palabras",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "${selectedCategories.size} seleccionadas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        TextButton(
                            onClick = { showCategorySelector = true }
                        ) {
                            Text("Seleccionar")
                        }
                    }
                    
                    if (selectedCategories.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = availableCategories
                                .filter { it.id in selectedCategories }
                                .take(3)
                                .joinToString(", ") { it.name } +
                                if (selectedCategories.size > 3) " y ${selectedCategories.size - 3} más..." else "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Create Game Button
            FilledTonalButton(
                onClick = {
                    if (hostName.isNotBlank() && selectedCategories.isNotEmpty()) {
                        val config = GameConfig(
                            hostName = hostName.trim(),
                            livesPerPlayer = livesPerPlayer,
                            selectedCategories = selectedCategories.toList(),
                            maxPlayers = 12
                        )
                        onStartHosting(config)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = hostName.isNotBlank() && selectedCategories.isNotEmpty()
            ) {
                Text(
                    text = "Crear y Alojar Partida",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // Requirements validation
            val missingRequirements = mutableListOf<String>()
            if (hostName.isBlank()) missingRequirements.add("Ingresa tu nombre")
            if (selectedCategories.isEmpty()) missingRequirements.add("Selecciona al menos una categoría")
            
            RequirementsCard(
                requirements = missingRequirements,
                isError = true
            )
        }
    }
    
    // Category Selector Dialog
    if (showCategorySelector) {
        CategorySelectorDialog(
            categories = availableCategories,
            selectedCategories = selectedCategories,
            onCategoriesSelected = { newSelection ->
                selectedCategories = newSelection
                showCategorySelector = false
            },
            onDismiss = { showCategorySelector = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategorySelectorDialog(
    categories: List<WordCategory>,
    selectedCategories: Set<WordCategoryId>,
    onCategoriesSelected: (Set<WordCategoryId>) -> Unit,
    onDismiss: () -> Unit
) {
    var tempSelection by remember { mutableStateOf(selectedCategories) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text("Seleccionar Categorías")
        },
        text = {
            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = category.id in tempSelection,
                                onClick = {
                                    tempSelection = if (category.id in tempSelection) {
                                        tempSelection - category.id
                                    } else {
                                        tempSelection + category.id
                                    }
                                }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = category.id in tempSelection,
                            onCheckedChange = null
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Column {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = getDifficultyText(category.difficulty),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onCategoriesSelected(tempSelection) }
            ) {
                Text("Confirmar (${tempSelection.size})")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun getDifficultyText(difficulty: Difficulty): String {
    return when (difficulty) {
        Difficulty.EASY -> "Fácil"
        Difficulty.MEDIUM -> "Medio"
        Difficulty.HARD -> "Difícil"
    }
}

@Preview(showBackground = true)
@Composable
fun CreateGameScreenPreview() {
    MaterialTheme {
        CreateGameScreen(
            availableCategories = listOf(
                WordCategory(
                    id = WordCategoryId("animals"),
                    name = "Animales",
                    description = "Diferentes tipos de animales",
                    words = listOf(),
                    difficulty = Difficulty.EASY
                ),
                WordCategory(
                    id = WordCategoryId("food"),
                    name = "Comida",
                    description = "Diferentes tipos de alimentos",
                    words = listOf(),
                    difficulty = Difficulty.MEDIUM
                )
            )
        )
    }
}