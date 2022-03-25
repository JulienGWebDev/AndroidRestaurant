package fr.isen.guinhut.androiderestaurant

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fr.isen.guinhut.androiderestaurant.databinding.ActivityPanierBinding
import fr.isen.guinhut.androiderestaurant.models.Commande
import fr.isen.guinhut.androiderestaurant.models.Panier
import fr.isen.guinhut.androiderestaurant.view.PanierAdapter
import java.io.File

class PanierActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPanierBinding
    private val itemsList = ArrayList<Commande>()
    private lateinit var panierAdapter: PanierAdapter
    private lateinit var panier: Panier


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanierBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        panier = lecturePanier()
        //titre fenetre
        title="Panier"

        //setup du recycler view
        val recyclerPanier: RecyclerView = binding.recyclerPanier
        panierAdapter = PanierAdapter(itemsList,PanierAdapter.OnClickListener { item ->
            onListPanierClickDelete(item)
        })
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerPanier.layoutManager = layoutManager
        recyclerPanier.adapter = panierAdapter
        panier.commandes.forEach { ligne: Commande-> itemsList.add(ligne) }
        panierAdapter.notifyDataSetChanged()

        //prix
        updateTotalPrice()

        //boutton passer commande
        binding.button.setOnClickListener {


        }
    }

    private fun updateTotalPrice(){
        var grandTotalPrice = 0.0F
        this.panier.commandes.forEach { commande:Commande -> grandTotalPrice+=commande.item.prices[0].price.toFloat()*commande.quantite }
        binding.GrandTotalPrice.text=grandTotalPrice.toString()+" €"
    }

    private fun onListPanierClickDelete(item: Commande) {
        Toast.makeText(this@PanierActivity, "${item.quantite.toInt()} ${item.item.name_fr} enlevé(s) du panier",
            Toast.LENGTH_SHORT).show()
        this.itemsList.remove(item)
        this.panier.commandes.remove(item)
        Log.d("PANIER",panier.commandes.size.toString())
        this.ecriturePanier()
        this.updateTotalPrice()
        this.panierAdapter.notifyDataSetChanged()
    }

    private fun ecriturePanier(){
        //sauvegarde du panier en json dans les fichiers
        val panierJson = Gson().toJson(panier)
        val filename = "panier.json"
        this.binding.root.context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(panierJson.toByteArray())
        }
    }

    private fun lecturePanier(): Panier {
        //lecture fichier panier
        val filename = "panier.json"
        val file = File(binding.root.context.filesDir, filename)
        if(file.exists()){
            val contents = file.readText()
            return Gson().fromJson(contents, Panier::class.java)
        }else{
            return Panier(ArrayList())
        }
    }
}