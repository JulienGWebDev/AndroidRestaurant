package fr.isen.guinhut.androiderestaurant.view

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guinhut.androiderestaurant.databinding.ItemBleBinding
import fr.isen.guinhut.androiderestaurant.models.Commande

internal class BLEAdapter(private var itemsList: MutableList<ScanResult>) : RecyclerView.Adapter<BLEAdapter.MyViewHolder>() {

    private lateinit var binding: ItemBleBinding

    internal inner class MyViewHolder(binding: ItemBleBinding) : RecyclerView.ViewHolder(binding.root) {
        val mac=binding.macAdd
        val name=binding.deviceName
        val numDis = binding.numDis
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemBleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.mac.text =  item.device.address
        holder.name.text =  item.device.name
        holder.name.text =  item.device.name
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }



}