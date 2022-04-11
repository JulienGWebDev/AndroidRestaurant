package fr.isen.guinhut.androiderestaurant

import android.annotation.SuppressLint
import android.bluetooth.*
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.guinhut.androiderestaurant.databinding.ActivityBledeviceBinding
import fr.isen.guinhut.androiderestaurant.models.BLEService
import fr.isen.guinhut.androiderestaurant.view.BLEServiceAdapter

import java.util.*


class BLEDeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBledeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null
    private var timer: Timer? = null
    private lateinit var adapter: BLEServiceAdapter

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBledeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val device = intent.getParcelableExtra<BluetoothDevice?>("Device")
        Toast.makeText(this, device?.address, Toast.LENGTH_SHORT).show()
        binding.deviceName.text = device?.name ?: "Unknown name"
        binding.deviceStatus.text = "déconnecté"

        connectToDevice(device)
    }

    override fun onStop() {
        super.onStop()
        closeBluetoothGatt()
    }

    @SuppressLint("MissingPermission")
    private fun closeBluetoothGatt() {
        bluetoothGatt?.close()
        bluetoothGatt=null
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice?) {
        this.bluetoothGatt= device?.connectGatt(this, true, gattCallback)
        this.bluetoothGatt?.connect()
    }

    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when (newState) {
                BluetoothGatt.STATE_CONNECTED -> {
                    gatt?.discoverServices()
                    runOnUiThread {  binding.deviceStatus.text = "connecté"}
                }
                BluetoothGatt.STATE_CONNECTING -> { runOnUiThread {  binding.deviceStatus.text = "connexion en cours..."} }
                else -> {runOnUiThread {  binding.deviceStatus.text = "déconnecté"}
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            val bleServices=gatt?.services?.map{ BLEService(it.uuid.toString(),it.characteristics) } ?: arrayListOf()
            runOnUiThread {
                binding.bleServicesList.layoutManager=LinearLayoutManager(this@BLEDeviceActivity)


                    adapter= BLEServiceAdapter(bleServices,
                    this@BLEDeviceActivity,
                    { characteristic -> gatt?.readCharacteristic(characteristic) },
                    { characteristic -> writeIntoCharacteristic(gatt!!, characteristic) },
                    { characteristic, enable ->
                        toggleNotificationOnCharacteristic(
                            gatt!!,
                            characteristic,
                            enable
                        )
                    }
                )
                binding.bleServicesList.adapter=adapter
            }

            Log.d("BluetoothLeService", "onServicesDiscovered()")
            val gattServices: List<BluetoothGattService> = gatt!!.services
            Log.d("onServicesDiscovered", "Services count: " + gattServices.size)
            for (gattService in gattServices) {
                val serviceUUID = gattService.uuid.toString()
                Log.d("onServicesDiscovered", "Service uuid $serviceUUID")

                val gattChara: List<BluetoothGattCharacteristic> = gattService.characteristics
                for(gattC in gattChara ){
                    val charaUUID = gattC.uuid.toString()
                    Log.d("onServicesDiscovered", "        cara uuid $charaUUID")
                }
            }
        }
        override fun onCharacteristicRead(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) { super.onCharacteristicRead(gatt, characteristic, status)
            runOnUiThread {
                adapter.updateFromChangedCharacteristic(characteristic)
                adapter.notifyDataSetChanged()
            }
        }
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) { super.onCharacteristicWrite(gatt, characteristic, status)}
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?
        ) {super.onCharacteristicChanged(gatt, characteristic) }





        @SuppressLint("MissingPermission")
        private fun toggleNotificationOnCharacteristic(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            enable: Boolean
        ) {
            characteristic.descriptors.forEach {
                it.value =
                    if (enable) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(it)
            }
            gatt.setCharacteristicNotification(characteristic, enable)
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    gatt.readCharacteristic(characteristic)
                }
            }, 0, 1000)
        }

        @SuppressLint("MissingPermission")
        private fun writeIntoCharacteristic(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            runOnUiThread {
                val input = EditText(this@BLEDeviceActivity)
                input.inputType = InputType.TYPE_CLASS_TEXT
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(16, 0, 16, 0)
                input.layoutParams = params

                AlertDialog.Builder(this@BLEDeviceActivity)
                    .setTitle("Ecrire la valeur")
                    .setView(input)
                    .setPositiveButton("Valider") { _, _ ->
                        characteristic.value = input.text.toString().toByteArray()
                        gatt.writeCharacteristic(characteristic)
                        gatt.readCharacteristic(characteristic)
                    }
                    .setNegativeButton("Annuler") { dialog, _ -> dialog.cancel() }
                    .create()
                    .show()
            }
        }
    }

}







