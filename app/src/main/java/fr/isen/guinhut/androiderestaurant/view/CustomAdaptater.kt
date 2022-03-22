package fr.isen.guinhut.androiderestaurant.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.guinhut.androiderestaurant.R
import fr.isen.guinhut.androiderestaurant.databinding.ItemBinding
import fr.isen.guinhut.androiderestaurant.models.Items

internal class CustomAdapter(private var itemsList: List<Items>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>(){

    private lateinit var binding: ItemBinding


    internal inner class MyViewHolder(binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var nameText: TextView = binding.nomVar
        var priceVar : TextView = binding.priceVar
        var img : ImageView= binding.imgSrc

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.nameText.text=item.name_fr
        holder.priceVar.text= item.prices[0].price+" €"

        if (item.images[0].isEmpty()) {
            holder.img.setImageResource(R.drawable.img2)
        } else{
            Picasso.get().load(item.images[0]).into(holder.img)
        }

        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }

    }
    override fun getItemCount(): Int {
        return itemsList.size
    }

    class OnClickListener(val clickListener: (meme: Items) -> Unit) {
        fun onClick(meme: Items) = clickListener(meme)
    }
}