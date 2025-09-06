package com.oceloti.lemc.navidadmencan.di

import com.oceloti.lemc.navidadmencan.data.datasources.GameDataSource
import com.oceloti.lemc.navidadmencan.data.datasources.LocalGameDataSource
import com.oceloti.lemc.navidadmencan.data.repositories.GameRepositoryImpl
import com.oceloti.lemc.navidadmencan.domain.repositories.GameRepository
import org.koin.dsl.module

val dataModule = module {
    
    // Data Sources
    single<GameDataSource> { LocalGameDataSource() }
    
    // Repositories
    single<GameRepository> { GameRepositoryImpl(get()) }
    
}