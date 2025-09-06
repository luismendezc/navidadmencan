package com.oceloti.lemc.navidadmencan.domain.usecases

import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.domain.models.Game
import com.oceloti.lemc.navidadmencan.domain.repositories.GameRepository
import kotlinx.coroutines.flow.Flow

class GetAvailableGamesUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke(): Flow<Result<List<Game>>> {
        return gameRepository.getAvailableGames()
    }
}