package fr.isen.guinhut.androiderestaurant

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.isen.guinhut.androiderestaurant.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val buttonEntrees = binding.buttonEntree
        val buttonPlats = binding.buttonPlat
        val buttonDesserts = binding.buttonDessert
        val buttonBle = binding.buttonBLE
        var buttonval = "Default"
        val easterEgg = binding.imageAccueil
        mediaPlayer = MediaPlayer.create(this, R.raw.frv)





        easterEgg.setOnTouchListener { _, event ->
            handleTouch(event)
        }

        buttonEntrees.setOnClickListener {
            buttonval  = "Entrees"
            anotherOne(buttonval)
        }

        buttonPlats.setOnClickListener {
            buttonval  = "Plats"
            anotherOne(buttonval)

        }

        buttonDesserts.setOnClickListener {
            buttonval  = "Desserts"
            anotherOne(buttonval)
        }

        buttonBle.setOnClickListener {
            val intent = Intent(this, BLEScanActivity::class.java)
            startActivity(intent)
        }

    }

    private fun handleTouch(event:MotionEvent): Boolean {
        when (event.action){
            MotionEvent.ACTION_DOWN -> {
                mediaPlayer?.start()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP ->{
                mediaPlayer?.pause()
                mediaPlayer?.seekTo(0)
            }
        }
        return true

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