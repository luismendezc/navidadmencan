package com.oceloti.lemc.navidadmencan

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.oceloti.lemc.navidadmencan.di.appModule

class NavidadApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NavidadApp)
            modules(appModule)
        }
    }
}