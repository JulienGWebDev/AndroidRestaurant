package fr.isen.guinhut.androiderestaurant.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guinhut.androiderestaurant.databinding.ItemPanierBinding
import fr.isen.guinhut.androiderestaurant.models.Commande

internal class PanierAdapter(private var itemsList: MutableList<Commande>,private val onClickListener: PanierAdapter.OnClickListener) : RecyclerView.Adapter<PanierAdapter.MyViewHolder>() {

    private lateinit var binding: ItemPanierBinding

    internal inner class MyViewHolder(binding: ItemPanierBinding) : RecyclerView.ViewHolder(binding.root) {
        val name=binding.nameItem
        val quantity=binding.quantite
        val totalItemPrice=binding.itemTotalPrice
        val btnDelete = binding.delete
        var com = binding.commentaireText

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = ItemPanierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.name.text=item.item.name_fr
        holder.quantity.text=item.quantite.toString()
        holder.totalItemPrice.text=(item.item.prices[0].price.toFloat()*item.quantite).toString()+" €"
        holder.btnDelete.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.com.text="Commentaire :${item.com}"
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }

    class OnClickListener(val clickListener: (item: Commande) -> Unit) {
        fun onClick(item: Commande) = clickListener(item)
    }


}