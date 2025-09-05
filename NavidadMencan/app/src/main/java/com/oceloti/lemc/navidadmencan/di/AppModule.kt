package com.oceloti.lemc.navidadmencan.di

import com.oceloti.lemc.navidadmencan.presentation.viewmodels.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { WelcomeViewModel() }
}