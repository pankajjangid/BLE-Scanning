package com.pankaj.blescanning.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pankaj.blescanning.data.BleDevice
import com.pankaj.blescanning.domain.ScanBleDevicesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BleScannerViewModel(
    private val scanBleDevicesUseCase: ScanBleDevicesUseCase
) : ViewModel() {

    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())
    val devices: StateFlow<List<BleDevice>> get() = _devices

    init {
        startScan()
    }

    private fun startScan() {
        viewModelScope.launch {
            scanBleDevicesUseCase().collect { device ->
                _devices.value = (_devices.value + device).distinctBy { it.address }
            }
        }
    }
}
