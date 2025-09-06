package com.oceloti.lemc.navidadmencan.domain.usecases

import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.domain.repositories.NetworkRepository
import kotlinx.coroutines.flow.Flow

class DiscoverDevicesUseCase(
    private val networkRepository: NetworkRepository
) {
    suspend fun startDiscovery(): Result<Unit> {
        return networkRepository.startDiscovery()
    }
    
    suspend fun stopDiscovery(): Result<Unit> {
        return networkRepository.stopDiscovery()
    }
    
    fun getDiscoveredDevices(): Flow<Result<List<String>>> {
        return networkRepository.getDiscoveredDevices()
    }
}