package com.oceloti.lemc.games.impostor.domain.models

/**
 * Represents a category of words for the impostor game
 * 
 * @param id Unique identifier for the category
 * @param name Display name of the category
 * @param description Brief description of what words are in this category
 * @param words List of words in this category
 * @param difficulty Difficulty level of this category
 */
data class WordCategory(
    val id: WordCategoryId,
    val name: String,
    val description: String,
    val words: List<String>,
    val difficulty: Difficulty
) {
    /**
     * Get a random word from this category
     */
    fun getRandomWord(): String? {
        return words.randomOrNull()
    }
    
    /**
     * Check if this category has enough words for gameplay
     */
    fun hasEnoughWords(): Boolean {
        return words.size >= MIN_WORDS_PER_CATEGORY
    }
    
    companion object {
        private const val MIN_WORDS_PER_CATEGORY = 10
        
        /**
         * Sample categories for testing and initial setup
         */
        fun getSampleCategories(): List<WordCategory> = listOf(
            WordCategory(
                id = WordCategoryId("dog_breeds"),
                name = "Razas de Perros",
                description = "Diferentes razas de perros domésticos",
                words = listOf(
                    "Labrador", "Golden Retriever", "Pastor Alemán", "Bulldog", "Beagle",
                    "Poodle", "Rottweiler", "Yorkshire Terrier", "Chihuahua", "Dálmata",
                    "Boxer", "Husky Siberiano", "Border Collie", "Cocker Spaniel", "Doberman"
                ),
                difficulty = Difficulty.EASY
            ),
            
            WordCategory(
                id = WordCategoryId("mexican_food"),
                name = "Comida Mexicana",
                description = "Platillos típicos de la cocina mexicana",
                words = listOf(
                    "Tacos", "Quesadillas", "Enchiladas", "Tamales", "Pozole",
                    "Mole", "Chiles Rellenos", "Guacamole", "Elote", "Sopes",
                    "Tostadas", "Flautas", "Cochinita Pibil", "Carnitas", "Barbacoa"
                ),
                difficulty = Difficulty.EASY
            ),
            
            WordCategory(
                id = WordCategoryId("bathroom_objects"),
                name = "Objetos del Baño",
                description = "Cosas que encuentras en un baño",
                words = listOf(
                    "Cepillo de Dientes", "Pasta Dental", "Champú", "Jabón", "Toalla",
                    "Espejo", "Regadera", "Inodoro", "Lavabo", "Papel Higiénico",
                    "Secador de Pelo", "Maquinilla de Afeitar", "Peine", "Esponja", "Cortina"
                ),
                difficulty = Difficulty.MEDIUM
            ),
            
            WordCategory(
                id = WordCategoryId("car_brands"),
                name = "Marcas de Autos",
                description = "Marcas de automóviles conocidas",
                words = listOf(
                    "Toyota", "Ford", "Chevrolet", "Honda", "Nissan",
                    "BMW", "Mercedes-Benz", "Audi", "Volkswagen", "Hyundai",
                    "Kia", "Mazda", "Subaru", "Lexus", "Acura"
                ),
                difficulty = Difficulty.MEDIUM
            ),
            
            WordCategory(
                id = WordCategoryId("candies"),
                name = "Dulces",
                description = "Diferentes tipos de dulces y golosinas",
                words = listOf(
                    "Chocolate", "Caramelo", "Chicle", "Paleta", "Gomitas",
                    "Malvaviscos", "Regaliz", "Piruleta", "Trufas", "Bombones",
                    "Mazapán", "Nougat", "Fudge", "Tamarindo", "Cocadas"
                ),
                difficulty = Difficulty.EASY
            ),
            
            WordCategory(
                id = WordCategoryId("musical_instruments"),
                name = "Instrumentos Musicales",
                description = "Instrumentos para hacer música",
                words = listOf(
                    "Piano", "Guitarra", "Violín", "Batería", "Saxofón",
                    "Trompeta", "Flauta", "Clarinete", "Arpa", "Bajo",
                    "Acordeón", "Harmónica", "Maracas", "Xilófono", "Cello"
                ),
                difficulty = Difficulty.HARD
            )
        )
    }
}

/**
 * Difficulty levels for word categories
 */
enum class Difficulty {
    EASY,    // Common, everyday words
    MEDIUM,  // Somewhat specific or technical words
    HARD     // Specialized or uncommon words
}