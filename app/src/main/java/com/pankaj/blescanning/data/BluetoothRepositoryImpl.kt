package com.pankaj.blescanning.data


import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BluetoothRepositoryImpl(
    private val context: Context
) : BluetoothRepository {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        manager.adapter
    }

    private val bleScanner: BluetoothLeScanner?
        get() = bluetoothAdapter.bluetoothLeScanner

    @SuppressLint("MissingPermission")
    override fun scanDevices(): Flow<BleDevice> = callbackFlow {
        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                trySend(
                    BleDevice(
                        name = result.device.name,
                        address = result.device.address,
                        rssi = result.rssi
                    )
                )
            }

            override fun onScanFailed(errorCode: Int) {
                close(Exception("BLE Scan failed: $errorCode"))
            }
        }

        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        bleScanner?.startScan(null, scanSettings, callback)

        awaitClose {
            bleScanner?.stopScan(callback)
        }
    }
}
