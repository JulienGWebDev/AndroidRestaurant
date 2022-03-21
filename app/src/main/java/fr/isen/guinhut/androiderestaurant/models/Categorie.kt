package fr.isen.guinhut.androiderestaurant.models

data class Categorie (
    val name_fr : String,
    val name_en: String,
    val items : ArrayList<Items>
)