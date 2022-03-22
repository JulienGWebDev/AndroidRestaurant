package fr.isen.guinhut.androiderestaurant

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.guinhut.androiderestaurant.databinding.ActivityCatBinding
import fr.isen.guinhut.androiderestaurant.models.APICat
import fr.isen.guinhut.androiderestaurant.models.Items
import fr.isen.guinhut.androiderestaurant.view.CustomAdapter
import org.json.JSONObject
import java.nio.charset.Charset

class CatActivity : AppCompatActivity() {

    private val itemsList = ArrayList<Items>()
    private lateinit var customAdapter: CustomAdapter
    private lateinit var binding : ActivityCatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val categoryName = intent.getStringExtra("buttonval")
        title = categoryName

        val recyclerView: RecyclerView = binding.items
        customAdapter = CustomAdapter(itemsList)

        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter

        getDataFromApi()
    }

    private fun getDataFromApi(){
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val json = JSONObject()
        json.put("id_shop", "1")
        json.toString()
        val requestBody = json.toString()

        val stringReq : StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val apiCat= Gson().fromJson(strResp, APICat::class.java)
                    apiCat.categorie[0]
                    Log.d("API", strResp)
                    fillRecyclerView(apiCat)
                },
                Response.ErrorListener { error ->
                    Log.d("API", "error => $error")
                }
            ){
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
        queue.add(stringReq)
    }

    fun fillRecyclerView(catAPI: APICat){
        if(intent.getStringExtra("buttonval")=="Plats"){
            catAPI.categorie[1].items.forEach { item: Items -> itemsList.add(item) }
        }
        else if(intent.getStringExtra("buttonval")=="Desserts"){
            catAPI.categorie[2].items.forEach { item: Items -> itemsList.add(item) }
        }else{
            catAPI.categorie[0].items.forEach { item: Items -> itemsList.add(item) }
        }
        customAdapter.notifyDataSetChanged()
    }
}