package fr.isen.guinhut.androiderestaurant.view

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guinhut.androiderestaurant.databinding.ItemBleBinding

internal class BLEAdapter(private var itemsList: MutableList<ScanResult>, private val onClickListener: OnClickListener,private val context: Context) : RecyclerView.Adapter<BLEAdapter.MyViewHolder>() {

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
        holder.numDis.text =  item.rssi.toString()

        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
    }
    fun clearResults() {
        itemsList.clear()
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }

    class OnClickListener(val clickListener: (item: ScanResult) -> Unit) {
        fun onClick(item: ScanResult) = clickListener(item)
    }


}