package fr.isen.guinhut.androiderestaurant.models

data class Prices (
    val id : Int,
    val id_pizza : Int,
    val id_size : Int,
    val price : String,
    val create_date: String,
    val update_date: String,
    val size : String
        )