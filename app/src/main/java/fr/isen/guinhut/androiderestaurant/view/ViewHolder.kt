package fr.isen.guinhut.androiderestaurant.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.guinhut.androiderestaurant.R
import fr.isen.guinhut.androiderestaurant.models.Categorie

fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return(ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_plats, parent, false)
    ))
}
class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name_fr: TextView = itemView.findViewById(R.id.name_fr)
    val name_en : TextView = itemView.findViewById(R.id.name_en)
    val items : TextView = itemView.findViewById(R.id.items)


    fun bind(categorie: Categorie) {
        name_fr.text = categorie.name_fr
        name_en.text = categorie.name_en
        //items.text = categorie.items
    }
}

