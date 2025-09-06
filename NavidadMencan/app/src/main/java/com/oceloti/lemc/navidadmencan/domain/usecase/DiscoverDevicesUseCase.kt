package com.oceloti.lemc.navidadmencan.domain.usecase

import com.oceloti.lemc.navidadmencan.domain.model.DeviceInfo
import com.oceloti.lemc.navidadmencan.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to discover available devices on the local network
 */
class DiscoverDevicesUseCase(
    private val networkRepository: NetworkRepository
) {
    
    suspend operator fun invoke(): Result<Flow<List<DeviceInfo>>> {
        // Start discovery service
        val discoveryResult = networkRepository.startDiscovery()
        if (discoveryResult.isFailure) {
            return Result.failure(
                Exception("Failed to start device discovery", discoveryResult.exceptionOrNull())
            )
        }
        
        // Return flow of discovered devices
        return Result.success(networkRepository.getDiscoveredDevices())
    }
    
    suspend fun stopDiscovery(): Result<Unit> {
        return networkRepository.stopDiscovery()
    }
}