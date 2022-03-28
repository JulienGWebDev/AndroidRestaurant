package fr.isen.guinhut.androiderestaurant

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guinhut.androiderestaurant.databinding.ActivityBlescanBinding
import fr.isen.guinhut.androiderestaurant.models.Commande
import fr.isen.guinhut.androiderestaurant.view.BLEAdapter
import fr.isen.guinhut.androiderestaurant.view.PanierAdapter


class BLEScanActivity : AppCompatActivity() {
    companion object{
        private const val ALL_PERMISSION_REQUEST_CODE = 1
    }
    private var scanning:Boolean=false
    private lateinit var binding : ActivityBlescanBinding
    private val bluetoothAdapter: BluetoothAdapter ? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val itemsList = ArrayList<ScanResult>()
    private lateinit var listBleAdapter: BLEAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlescanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        when {
            bluetoothAdapter?.isEnabled == true ->
                binding.playpause.setOnClickListener {
                    startLeScanBLEWithPermission(!scanning)
                }
            bluetoothAdapter != null ->
                askBluetoothPermission()
            else -> {
                displayNoBleAvailable()
            }
        }


        val recyclerBle: RecyclerView = binding.Recyble
        listBleAdapter = BLEAdapter(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerBle.layoutManager = layoutManager
        recyclerBle.adapter = listBleAdapter

    }
    override fun onStop(){
        super.onStop()
        startLeScanBLEWithPermission(false)
    }

    private fun startLeScanBLEWithPermission(enable: Boolean){
        if (checkAllPermissionGranted()) {
            startLeScanBLE(enable)
        }else{
            ActivityCompat.requestPermissions(this, getAllPermissions() ,ALL_PERMISSION_REQUEST_CODE)
        }
    }

    private fun checkAllPermissionGranted(): Boolean {
        return getAllPermissions().all { permission ->
            ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getAllPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLeScanBLE(enable: Boolean) {

        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if(enable) {
                scanning=true
                startScan(scanCallBack)
            }else{
                scanning=false
                stopScan(scanCallBack)
            }
            btnPlayClick()
        }
    }

    private val scanCallBack = object : ScanCallback(){
        override fun onScanResult(callBackType : Int, result:ScanResult){
            Log.d("BLEScanActivity","${result.device.address} - ${result.rssi}")
            addToList(result)

        }
    }
    private fun addToList(res:ScanResult){
        val index:Int = itemsList.indexOfFirst{ it.device.address==res.device.address }
        if(index == -1){
            itemsList.add(res)
        }else{
            itemsList[index]=res
        }
        itemsList.sortBy { kotlin.math.abs(it.rssi) }
        listBleAdapter.notifyDataSetChanged()
    }

    private fun displayNoBleAvailable() {
        binding.playpause.isVisible=false
        binding.launchText.text="NO GOOD"
        binding.progressBar.isIndeterminate=true
    }

    private fun btnPlayClick() {
        if (scanning) {
            binding.playpause.setImageResource(R.drawable.pause)
            binding.launchText.text = "Scanning..."
            binding.progressBar.isIndeterminate = true
        } else {
            binding.playpause.setImageResource(R.drawable.play)
            binding.launchText.text = "Go scan"
            binding.progressBar.isIndeterminate = false
        }
    }

    private fun askBluetoothPermission() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            startActivityForResult(enableBtIntent, ALL_PERMISSION_REQUEST_CODE)
        }
    }
}