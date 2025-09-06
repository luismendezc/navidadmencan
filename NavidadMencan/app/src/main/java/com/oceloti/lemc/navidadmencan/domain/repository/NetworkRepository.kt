package com.oceloti.lemc.navidadmencan.domain.repository

import com.oceloti.lemc.navidadmencan.domain.model.DeviceInfo
import com.oceloti.lemc.navidadmencan.domain.model.NetworkEvent
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for network-related operations in LAN multiplayer
 */
interface NetworkRepository {
    
    /**
     * Start network discovery service to find other devices
     */
    suspend fun startDiscovery(): Result<Unit>
    
    /**
     * Stop network discovery service
     */
    suspend fun stopDiscovery(): Result<Unit>
    
    /**
     * Get discovered devices on the network
     */
    suspend fun getDiscoveredDevices(): Flow<List<DeviceInfo>>
    
    /**
     * Get current device information
     */
    suspend fun getCurrentDeviceInfo(): DeviceInfo
    
    /**
     * Start hosting a network service
     */
    suspend fun startHosting(port: Int = 8080): Result<Unit>
    
    /**
     * Stop hosting the network service
     */
    suspend fun stopHosting(): Result<Unit>
    
    /**
     * Connect to a host device
     */
    suspend fun connectToHost(deviceInfo: DeviceInfo): Result<Unit>
    
    /**
     * Disconnect from current host
     */
    suspend fun disconnect(): Result<Unit>
    
    /**
     * Send a network event
     */
    suspend fun sendEvent(event: NetworkEvent): Result<Unit>
    
    /**
     * Observe incoming network events
     */
    suspend fun observeNetworkEvents(): Flow<NetworkEvent>
    
    /**
     * Check if currently connected to a network
     */
    suspend fun isConnected(): Boolean
    
    /**
     * Check if currently hosting
     */
    suspend fun isHosting(): Boolean
    
    /**
     * Get connection status
     */
    suspend fun getConnectionStatus(): Flow<ConnectionStatus>
}

enum class ConnectionStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    HOSTING,
    ERROR
}