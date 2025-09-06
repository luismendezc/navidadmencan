package com.oceloti.lemc.navidadmencan.di

/**
 * Main app module that includes all other modules for clean architecture
 */
val appModule = listOf(
    dataModule,
    domainModule,
    presentationModule
)