package com.oceloti.lemc.navidadmencan.domain.usecase

import com.oceloti.lemc.navidadmencan.domain.model.Game
import com.oceloti.lemc.navidadmencan.domain.model.GameCategory
import com.oceloti.lemc.navidadmencan.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get available games with optional filtering
 */
class GetAvailableGamesUseCase(
    private val gameRepository: GameRepository
) {
    
    suspend operator fun invoke(
        category: GameCategory? = null,
        installedOnly: Boolean = false,
        playerCount: Int? = null
    ): Flow<List<Game>> {
        return when {
            category != null -> gameRepository.getGamesByCategory(category)
            installedOnly -> gameRepository.getInstalledGames()
            playerCount != null -> gameRepository.getRecommendedGames(playerCount)
            else -> gameRepository.getAllGames()
        }
    }
}