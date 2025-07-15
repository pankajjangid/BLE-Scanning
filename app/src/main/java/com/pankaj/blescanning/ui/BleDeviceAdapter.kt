package com.pankaj.blescanning.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pankaj.blescanning.data.BleDevice
import com.pankaj.blescanning.databinding.ItemBleDeviceBinding

class BleDeviceAdapter : RecyclerView.Adapter<BleDeviceAdapter.DeviceViewHolder>() {

    private val devices = mutableListOf<BleDevice>()

    fun submitList(newList: List<BleDevice>) {
        devices.clear()
        devices.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemBleDeviceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int = devices.size

    class DeviceViewHolder(private val binding: ItemBleDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(device: BleDevice) {
            binding.tvDeviceName.text = device.name ?: "N/A"
            binding.tvDeviceAddress.text = device.address
            binding.tvDeviceRssi.text = "RSSI: ${device.rssi}"
        }
    }
}
