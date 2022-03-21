package fr.isen.guinhut.androiderestaurant.models

data class Items (
    val id : Int,
    val name_fr : String,
    val name_en: String,
    val id_categorie : Int,
    val categ_name_fr : String,
    val categ_name_en: String,
    val images: ArrayList<String>,
    val ingredients : ArrayList<Ingredients>,
    val prices : ArrayList<Prices>
        )