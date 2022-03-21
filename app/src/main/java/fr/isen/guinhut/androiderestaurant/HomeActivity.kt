package fr.isen.guinhut.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val buttonEntrees = findViewById<Button>(R.id.button2)
        val buttonPlats = findViewById<Button>(R.id.button3)
        val buttonDesserts = findViewById<Button>(R.id.button4)
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
        val intent = Intent(this, PlatsActivity::class.java)
        intent.putExtra("buttonval", buttonval);
        startActivity(intent)
    }




}