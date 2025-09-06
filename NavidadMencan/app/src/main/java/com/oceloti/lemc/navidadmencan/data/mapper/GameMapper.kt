package com.oceloti.lemc.navidadmencan.data.mapper

import com.oceloti.lemc.navidadmencan.data.model.GameDto
import com.oceloti.lemc.navidadmencan.domain.model.Game
import com.oceloti.lemc.navidadmencan.domain.model.GameCategory
import com.oceloti.lemc.navidadmencan.domain.model.GameDifficulty

/**
 * Mapper to convert between Game domain model and GameDto data model
 */
object GameMapper {
    
    fun GameDto.toDomain(): Game {
        return Game(
            id = id,
            name = name,
            description = description,
            iconUrl = iconUrl,
            minPlayers = minPlayers,
            maxPlayers = maxPlayers,
            estimatedDurationMinutes = estimatedDurationMinutes,
            category = category.toGameCategory(),
            isInstalled = isInstalled,
            version = version,
            isEnabled = isEnabled,
            difficulty = difficulty.toGameDifficulty()
        )
    }
    
    fun Game.toDto(): GameDto {
        return GameDto(
            id = id,
            name = name,
            description = description,
            iconUrl = iconUrl,
            minPlayers = minPlayers,
            maxPlayers = maxPlayers,
            estimatedDurationMinutes = estimatedDurationMinutes,
            category = category.name,
            isInstalled = isInstalled,
            version = version,
            isEnabled = isEnabled,
            difficulty = difficulty.name
        )
    }
    
    private fun String.toGameCategory(): GameCategory {
        return try {
            GameCategory.valueOf(this)
        } catch (e: IllegalArgumentException) {
            GameCategory.PARTY_GAME // Default fallback
        }
    }
    
    private fun String.toGameDifficulty(): GameDifficulty {
        return try {
            GameDifficulty.valueOf(this)
        } catch (e: IllegalArgumentException) {
            GameDifficulty.MEDIUM // Default fallback
        }
    }
}