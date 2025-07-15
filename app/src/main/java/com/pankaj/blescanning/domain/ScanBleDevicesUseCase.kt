package com.pankaj.blescanning.domain

import com.pankaj.blescanning.data.BleDevice
import com.pankaj.blescanning.data.BluetoothRepository
import kotlinx.coroutines.flow.Flow

class ScanBleDevicesUseCase(
    private val bluetoothRepository: BluetoothRepository
) {
    operator fun invoke(): Flow<BleDevice> = bluetoothRepository.scanDevices()
}
