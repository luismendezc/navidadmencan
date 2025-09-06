package com.oceloti.lemc.games.impostor.domain.repository

import com.oceloti.lemc.games.impostor.domain.models.*

/**
 * Repository interface for managing word categories and game words
 */
interface WordRepository {
    /**
     * Get all available word categories
     */
    suspend fun getAllCategories(): List<WordCategory>
    
    /**
     * Get a specific category by ID
     */
    suspend fun getCategoryById(id: WordCategoryId): WordCategory?
    
    /**
     * Get categories by difficulty level
     */
    suspend fun getCategoriesByDifficulty(difficulty: Difficulty): List<WordCategory>
    
    /**
     * Search categories by name or description
     */
    suspend fun searchCategories(query: String): List<WordCategory>
    
    /**
     * Get a random word from a specific category
     */
    suspend fun getRandomWordFromCategory(categoryId: WordCategoryId): String?
    
    /**
     * Get multiple random words for game setup (typically 24 for 6x4 grid)
     */
    suspend fun getGameWords(categoryId: WordCategoryId, count: Int = 24): List<String>
}

/**
 * Implementation using the Spanish word database
 */
class SpanishWordRepository : WordRepository {
    
    override suspend fun getAllCategories(): List<WordCategory> {
        return SpanishWordDatabase.getAllCategories()
    }
    
    override suspend fun getCategoryById(id: WordCategoryId): WordCategory? {
        return getAllCategories().find { it.id == id }
    }
    
    override suspend fun getCategoriesByDifficulty(difficulty: Difficulty): List<WordCategory> {
        return SpanishWordDatabase.getCategoriesByDifficulty(difficulty)
    }
    
    override suspend fun searchCategories(query: String): List<WordCategory> {
        return SpanishWordDatabase.searchCategories(query)
    }
    
    override suspend fun getRandomWordFromCategory(categoryId: WordCategoryId): String? {
        return getCategoryById(categoryId)?.getRandomWord()
    }
    
    override suspend fun getGameWords(categoryId: WordCategoryId, count: Int): List<String> {
        val category = getCategoryById(categoryId) ?: return emptyList()
        return category.words.shuffled().take(count)
    }
}