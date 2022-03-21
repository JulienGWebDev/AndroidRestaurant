package fr.isen.guinhut.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.isen.guinhut.androiderestaurant.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        val buttonEntrees = binding.buttonDessert
        val buttonPlats = binding.buttonPlat
        val buttonDesserts = binding.buttonDessert
        var buttonval = "Default"


        buttonEntrees.setOnClickListener {
            Toast.makeText(this@HomeActivity, "Entrées", Toast.LENGTH_LONG).show()
            buttonval  = "Entrees"
            anotherOne(buttonval)
        }

        buttonPlats.setOnClickListener {
            Toast.makeText(this@HomeActivity, "Plats", Toast.LENGTH_LONG).show()
            buttonval  = "Plats"
            anotherOne(buttonval)

        }

        buttonDesserts.setOnClickListener {
            Toast.makeText(this@HomeActivity, "Desserts", Toast.LENGTH_LONG).show()
            buttonval  = "Desserts"
            anotherOne(buttonval)
        }

    }

    override fun onDestroy() {
        val tag = "HomeActivity"
        Log.d(tag, "Home activity finito")
        super.onDestroy()

    }

    fun anotherOne(buttonval:String){
        val intent = Intent(this, CatActivity::class.java)
        intent.putExtra("buttonval", buttonval);
        startActivity(intent)
    }




}