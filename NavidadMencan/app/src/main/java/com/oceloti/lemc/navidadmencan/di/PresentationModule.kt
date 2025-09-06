package com.oceloti.lemc.navidadmencan.di

import com.oceloti.lemc.navidadmencan.presentation.viewmodels.GameHubViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    
    // ViewModels
    viewModel { GameHubViewModel(get(), get()) }
    
}