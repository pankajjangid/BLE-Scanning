package com.pankaj.blescanning.data

import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {
    fun scanDevices(): Flow<BleDevice>
}
