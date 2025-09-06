package com.oceloti.lemc.navidadmencan.data.mappers

import com.oceloti.lemc.navidadmencan.data.models.GameDto
import com.oceloti.lemc.navidadmencan.domain.models.Game
import com.oceloti.lemc.navidadmencan.domain.models.GameCategory
import com.oceloti.lemc.navidadmencan.domain.models.GameDifficulty

object GameMapper {
    fun toDomain(dto: GameDto): Game {
        return Game(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            category = GameCategory.valueOf(dto.category),
            minPlayers = dto.minPlayers,
            maxPlayers = dto.maxPlayers,
            estimatedDuration = dto.estimatedDuration,
            isInstalled = dto.isInstalled,
            iconUrl = dto.iconUrl,
            difficulty = GameDifficulty.valueOf(dto.difficulty)
        )
    }
    
    fun toDto(domain: Game): GameDto {
        return GameDto(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            category = domain.category.name,
            minPlayers = domain.minPlayers,
            maxPlayers = domain.maxPlayers,
            estimatedDuration = domain.estimatedDuration,
            isInstalled = domain.isInstalled,
            iconUrl = domain.iconUrl,
            difficulty = domain.difficulty.name
        )
    }
    
    fun toDomainList(dtoList: List<GameDto>): List<Game> {
        return dtoList.map { toDomain(it) }
    }
}