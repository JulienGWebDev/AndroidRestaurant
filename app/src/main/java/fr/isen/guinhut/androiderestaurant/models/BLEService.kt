package fr.isen.guinhut.androiderestaurant.models

import android.bluetooth.BluetoothGattCharacteristic
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class BLEService(val name: String, characteristics: MutableList<BluetoothGattCharacteristic>) : ExpandableGroup<BluetoothGattCharacteristic>(name, characteristics)
