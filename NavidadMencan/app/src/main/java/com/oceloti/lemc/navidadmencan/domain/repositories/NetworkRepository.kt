package com.oceloti.lemc.navidadmencan.domain.repositories

import com.oceloti.lemc.navidadmencan.core.util.Result
import com.oceloti.lemc.navidadmencan.domain.models.NetworkEvent
import com.oceloti.lemc.navidadmencan.domain.models.Player
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getNetworkEvents(): Flow<NetworkEvent>
    suspend fun startDiscovery(): Result<Unit>
    suspend fun stopDiscovery(): Result<Unit>
    fun getDiscoveredDevices(): Flow<Result<List<String>>>
    suspend fun createPlayer(name: String, deviceName: String): Result<Player>
    suspend fun broadcastMessage(message: String): Result<Unit>
    suspend fun sendMessageToDevice(ipAddress: String, message: String): Result<Unit>
}