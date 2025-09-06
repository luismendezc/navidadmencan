package com.oceloti.lemc.navidadmencan.di

import com.oceloti.lemc.navidadmencan.domain.usecases.CreateLobbyUseCase
import com.oceloti.lemc.navidadmencan.domain.usecases.DiscoverDevicesUseCase
import com.oceloti.lemc.navidadmencan.domain.usecases.GetAvailableGamesUseCase
import com.oceloti.lemc.navidadmencan.domain.usecases.JoinLobbyUseCase
import org.koin.dsl.module

val domainModule = module {
    
    // Use Cases
    factory { GetAvailableGamesUseCase(get()) }
    // Note: Lobby and Network use cases will be added when repositories are implemented
    // factory { CreateLobbyUseCase(get()) }
    // factory { JoinLobbyUseCase(get()) }
    // factory { DiscoverDevicesUseCase(get()) }
    
}