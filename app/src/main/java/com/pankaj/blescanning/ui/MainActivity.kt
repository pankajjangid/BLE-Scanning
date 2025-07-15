package com.pankaj.blescanning.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pankaj.blescanning.data.BluetoothRepositoryImpl
import com.pankaj.blescanning.databinding.ActivityMainBinding
import com.pankaj.blescanning.domain.ScanBleDevicesUseCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: BleScannerViewModel
    private lateinit var adapter: BleDeviceAdapter

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.any { it }) {
            setupScanner()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        permissionLauncher.launch(permissions.toTypedArray())
    }

    private fun setupScanner() {
        val repository = BluetoothRepositoryImpl(this)
        val useCase = ScanBleDevicesUseCase(repository)
        viewModel = BleScannerViewModel(useCase)

        adapter = BleDeviceAdapter()
        binding.recyclerViewDevices.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDevices.adapter = adapter

        lifecycleScope.launch {
            viewModel.devices.collectLatest { deviceList ->
                adapter.submitList(deviceList)
            }
        }
    }
}
