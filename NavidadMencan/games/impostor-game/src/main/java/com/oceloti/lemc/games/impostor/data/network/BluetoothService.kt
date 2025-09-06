package com.oceloti.lemc.games.impostor.data.network

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.ParcelUuid
import androidx.core.app.ActivityCompat
import com.oceloti.lemc.games.impostor.domain.models.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.util.*

/**
 * Bluetooth Low Energy service for cross-platform communication
 * Supports both Android and iOS devices
 */
class BluetoothService(
    private val context: Context,
    private val json: Json = Json { 
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
) {
    
    companion object {
        // Service UUID for the Impostor Game - consistent across platforms
        private val SERVICE_UUID = UUID.fromString("12345678-1234-5678-9012-123456789abc")
        private val CHARACTERISTIC_UUID = UUID.fromString("87654321-4321-8765-2109-cba987654321")
        private val DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        
        // Service name for discovery
        private const val SERVICE_NAME = "ImpostorGame"
        
        // Maximum message size for BLE
        private const val MAX_MESSAGE_SIZE = 512
    }
    
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter
    
    // BLE components
    private var bluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var gattServer: BluetoothGattServer? = null
    private var gattClient: BluetoothGatt? = null
    
    // Connected devices
    private val connectedDevices = mutableMapOf<String, BluetoothDevice>()
    private val networkEventFlow = MutableSharedFlow<NetworkEvent>()
    
    // Current game session
    private var currentGameId: GameId? = null
    
    /**
     * Check if Bluetooth is available and enabled
     */
    fun isBluetoothAvailable(): Boolean {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled
    }
    
    /**
     * Check if required permissions are granted
     */
    fun hasRequiredPermissions(): Boolean {
        val permissions = listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        
        return permissions.all { permission ->
            ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    /**
     * Start hosting a game (advertise as server)
     */
    @SuppressLint("MissingPermission")
    suspend fun startHosting(gameId: GameId, hostName: String): Result<Unit> {
        if (!isBluetoothAvailable()) {
            return Result.failure(IllegalStateException("Bluetooth not available"))
        }
        
        if (!hasRequiredPermissions()) {
            return Result.failure(SecurityException("Missing Bluetooth permissions"))
        }
        
        return try {
            currentGameId = gameId
            
            // Setup GATT Server
            setupGattServer()
            
            // Start advertising
            startAdvertising(hostName)
            
            networkEventFlow.emit(NetworkEvent.ConnectionStatusChanged(ConnectionStatus.HOST_DISCONNECTED))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Start scanning for available games
     */
    fun discoverGames(): Flow<List<DiscoverableGame>> = callbackFlow {
        if (!isBluetoothAvailable() || !hasRequiredPermissions()) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        
        val discoveredGames = mutableListOf<DiscoverableGame>()
        
        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = result.device
                val scanRecord = result.scanRecord
                
                scanRecord?.let { record ->
                    val serviceUuids = record.serviceUuids
                    if (serviceUuids?.contains(ParcelUuid(SERVICE_UUID)) == true) {
                        val deviceName = record.deviceName ?: device.name ?: "Unknown"
                        
                        // Extract game info from advertisement data
                        val gameInfo = parseAdvertisementData(record.getBytes())
                        gameInfo?.let { info ->
                            val discoverableGame = DiscoverableGame(
                                gameId = GameId(info.gameId),
                                hostName = deviceName,
                                playerCount = info.playerCount,
                                maxPlayers = info.maxPlayers,
                                gameState = GameState.WaitingForPlayers,
                                connectionInfo = ConnectionInfo(
                                    connectionType = ConnectionType.BLUETOOTH,
                                    address = device.address
                                )
                            )
                            
                            val existingIndex = discoveredGames.indexOfFirst { 
                                it.gameId.value == info.gameId 
                            }
                            
                            if (existingIndex >= 0) {
                                discoveredGames[existingIndex] = discoverableGame
                            } else {
                                discoveredGames.add(discoverableGame)
                            }
                            
                            trySend(discoveredGames.toList())
                        }
                    }
                }
            }
            
            override fun onScanFailed(errorCode: Int) {
                networkEventFlow.tryEmit(
                    NetworkEvent.Error("Bluetooth scan failed: $errorCode")
                )
            }
        }
        
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
            
        val scanFilters = listOf(
            ScanFilter.Builder()
                .setServiceUuid(ParcelUuid(SERVICE_UUID))
                .build()
        )
        
        try {
            bluetoothLeScanner?.startScan(scanFilters, scanSettings, scanCallback)
        } catch (e: SecurityException) {
            close(e)
            return@callbackFlow
        }
        
        awaitClose {
            try {
                bluetoothLeScanner?.stopScan(scanCallback)
            } catch (e: SecurityException) {
                // Ignore
            }
        }
    }
    
    /**
     * Join a discovered game
     */
    @SuppressLint("MissingPermission")
    suspend fun joinGame(gameId: GameId, player: Player): Result<Unit> {
        if (!isBluetoothAvailable() || !hasRequiredPermissions()) {
            return Result.failure(IllegalStateException("Bluetooth not available"))
        }
        
        return try {
            currentGameId = gameId
            
            // Find and connect to the game host
            val targetDevice = findGameHost(gameId.value)
                ?: return Result.failure(IllegalStateException("Game host not found"))
            
            connectToDevice(targetDevice, player)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Send a network event to all connected devices
     */
    suspend fun sendNetworkEvent(event: NetworkEvent): Result<Unit> {
        return try {
            val message = json.encodeToString<NetworkEvent>(event)
            broadcastMessage(message)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Observe network events from other devices
     */
    fun observeNetworkEvents(): Flow<NetworkEvent> = networkEventFlow.asSharedFlow()
    
    /**
     * Stop all Bluetooth operations
     */
    @SuppressLint("MissingPermission")
    suspend fun stopService(): Result<Unit> {
        return try {
            // Stop advertising
            bluetoothLeAdvertiser?.stopAdvertising(object : AdvertiseCallback() {})
            
            // Close GATT connections
            gattClient?.disconnect()
            gattClient?.close()
            gattServer?.close()
            
            // Clear connected devices
            connectedDevices.clear()
            
            networkEventFlow.emit(NetworkEvent.ConnectionStatusChanged(ConnectionStatus.DISCONNECTED))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Private helper methods
    
    @SuppressLint("MissingPermission")
    private fun setupGattServer() {
        val service = BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY)
        
        val characteristic = BluetoothGattCharacteristic(
            CHARACTERISTIC_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ or 
            BluetoothGattCharacteristic.PROPERTY_WRITE or
            BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ or 
            BluetoothGattCharacteristic.PERMISSION_WRITE
        )
        
        val descriptor = BluetoothGattDescriptor(
            DESCRIPTOR_UUID,
            BluetoothGattDescriptor.PERMISSION_READ or BluetoothGattDescriptor.PERMISSION_WRITE
        )
        
        characteristic.addDescriptor(descriptor)
        service.addCharacteristic(characteristic)
        
        val gattServerCallback = object : BluetoothGattServerCallback() {
            override fun onConnectionStateChange(
                device: BluetoothDevice,
                status: Int,
                newState: Int
            ) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        connectedDevices[device.address] = device
                        networkEventFlow.tryEmit(
                            NetworkEvent.ConnectionStatusChanged(ConnectionStatus.CONNECTED)
                        )
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        connectedDevices.remove(device.address)
                        networkEventFlow.tryEmit(
                            NetworkEvent.PlayerDisconnected(PlayerId(device.address))
                        )
                    }
                }
            }
            
            override fun onCharacteristicWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                characteristic: BluetoothGattCharacteristic,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray
            ) {
                try {
                    val message = String(value)
                    val networkEvent = json.decodeFromString<NetworkEvent>(message)
                    networkEventFlow.tryEmit(networkEvent)
                    
                    if (responseNeeded) {
                        gattServer?.sendResponse(
                            device,
                            requestId,
                            BluetoothGatt.GATT_SUCCESS,
                            0,
                            null
                        )
                    }
                } catch (e: Exception) {
                    if (responseNeeded) {
                        gattServer?.sendResponse(
                            device,
                            requestId,
                            BluetoothGatt.GATT_FAILURE,
                            0,
                            null
                        )
                    }
                }
            }
        }
        
        gattServer = bluetoothManager.openGattServer(context, gattServerCallback)
        gattServer?.addService(service)
    }
    
    @SuppressLint("MissingPermission")
    private fun startAdvertising(hostName: String) {
        bluetoothLeAdvertiser = bluetoothAdapter.bluetoothLeAdvertiser
        
        val advertiseSettings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            .setConnectable(true)
            .build()
            
        val advertiseData = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .setIncludeTxPowerLevel(false)
            .addServiceUuid(ParcelUuid(SERVICE_UUID))
            .build()
            
        val scanResponseData = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceData(
                ParcelUuid(SERVICE_UUID),
                createGameInfoBytes(hostName)
            )
            .build()
            
        val advertiseCallback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                networkEventFlow.tryEmit(
                    NetworkEvent.ConnectionStatusChanged(ConnectionStatus.CONNECTED)
                )
            }
            
            override fun onStartFailure(errorCode: Int) {
                networkEventFlow.tryEmit(
                    NetworkEvent.Error("Failed to start advertising: $errorCode")
                )
            }
        }
        
        bluetoothLeAdvertiser?.startAdvertising(
            advertiseSettings,
            advertiseData,
            scanResponseData,
            advertiseCallback
        )
    }
    
    @SuppressLint("MissingPermission")
    private suspend fun connectToDevice(device: BluetoothDevice, player: Player) {
        val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                when (newState) {
                    BluetoothProfile.STATE_CONNECTED -> {
                        gatt.discoverServices()
                        networkEventFlow.tryEmit(
                            NetworkEvent.ConnectionStatusChanged(ConnectionStatus.CONNECTED)
                        )
                    }
                    BluetoothProfile.STATE_DISCONNECTED -> {
                        networkEventFlow.tryEmit(
                            NetworkEvent.ConnectionStatusChanged(ConnectionStatus.DISCONNECTED)
                        )
                    }
                }
            }
            
            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // Send join request
                    val joinEvent = NetworkEvent.PlayerJoined(player)
                    val message = json.encodeToString<NetworkEvent>(joinEvent)
                    writeToCharacteristic(gatt, message)
                }
            }
        }
        
        gattClient = device.connectGatt(context, false, gattCallback)
    }
    
    @SuppressLint("MissingPermission")
    private fun writeToCharacteristic(gatt: BluetoothGatt, message: String) {
        val service = gatt.getService(SERVICE_UUID)
        val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID)
        
        characteristic?.let {
            it.value = message.toByteArray()
            gatt.writeCharacteristic(it)
        }
    }
    
    @SuppressLint("MissingPermission")
    private fun broadcastMessage(message: String) {
        gattServer?.let { server ->
            val service = server.getService(SERVICE_UUID)
            val characteristic = service?.getCharacteristic(CHARACTERISTIC_UUID)
            
            characteristic?.let {
                it.value = message.toByteArray()
                
                connectedDevices.values.forEach { device ->
                    server.notifyCharacteristicChanged(device, it, false)
                }
            }
        }
    }
    
    private fun findGameHost(gameId: String): BluetoothDevice? {
        // This would typically be implemented using the scan results
        // For now, return null - would need to be enhanced with proper device discovery
        return null
    }
    
    private fun createGameInfoBytes(hostName: String): ByteArray {
        // Create a compact representation of game info for advertisement
        val gameInfo = GameAdvertisementInfo(
            gameId = currentGameId?.value ?: "",
            hostName = hostName,
            playerCount = connectedDevices.size + 1, // +1 for host
            maxPlayers = 10
        )
        
        return json.encodeToString<GameAdvertisementInfo>(gameInfo).toByteArray()
    }
    
    private fun parseAdvertisementData(data: ByteArray?): GameAdvertisementInfo? {
        return try {
            data?.let {
                val json_string = String(it)
                json.decodeFromString<GameAdvertisementInfo>(json_string)
            }
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Compact game information for BLE advertisement
 */
@kotlinx.serialization.Serializable
private data class GameAdvertisementInfo(
    val gameId: String,
    val hostName: String,
    val playerCount: Int,
    val maxPlayers: Int
)