package com.oceloti.lemc.games.impostor.domain.models

/**
 * Value class representing a unique player identifier
 */
@JvmInline
value class PlayerId(val value: String) {
    companion object {
        fun generate(): PlayerId = PlayerId(java.util.UUID.randomUUID().toString())
    }
}

/**
 * Value class representing a unique game identifier
 */
@JvmInline
value class GameId(val value: String) {
    companion object {
        fun generate(): GameId = GameId(java.util.UUID.randomUUID().toString())
    }
}

/**
 * Value class representing a unique word category identifier
 */
@JvmInline
value class WordCategoryId(val value: String)