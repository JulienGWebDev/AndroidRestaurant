package fr.isen.guinhut.androiderestaurant

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plats)

        val cat:String = intent.getStringExtra("buttonval").toString()
        val titre = findViewById<TextView>(R.id.titre)
        titre.setText(cat)
    }
}