package fr.isen.guinhut.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.guinhut.androiderestaurant.databinding.ActivityCatBinding
import fr.isen.guinhut.androiderestaurant.models.APICat
import fr.isen.guinhut.androiderestaurant.models.Items
import fr.isen.guinhut.androiderestaurant.view.CustomAdapter
import org.json.JSONObject
import java.io.Serializable
import java.nio.charset.Charset

class CatActivity : AppCompatActivity(), Serializable {

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
        customAdapter = CustomAdapter(itemsList,CustomAdapter.OnClickListener { item ->
            onListItemClick(item)
        })

        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(refreshListener);


        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter
        getDataFromApi()

    }

    private fun onListItemClick(item: Items) {
        Toast.makeText(this@CatActivity, item.name_fr, Toast.LENGTH_LONG).show()
        val intent = Intent(this, ItemActivity::class.java)
        intent.putExtra("item", Gson().toJson(item))
        startActivity(intent)

    }

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout

        swipeRefreshLayout.isRefreshing = true
        clearData()
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
                    apiCat.data[0]
                    Log.d("API", strResp)
                    fillRecyclerView(apiCat)
                    val swipeRefreshLayout: SwipeRefreshLayout = binding.swipeRefreshLayout
                    swipeRefreshLayout.isRefreshing = false
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

    private fun clearData() {
        itemsList.clear()
    }

    fun fillRecyclerView(catAPI: APICat){
        if(intent.getStringExtra("buttonval")=="Plats"){
            catAPI.data[1].items.forEach { item: Items -> itemsList.add(item) }
        }
        else if(intent.getStringExtra("buttonval")=="Desserts"){
            catAPI.data[2].items.forEach { item: Items -> itemsList.add(item) }
        }else{
            catAPI.data[0].items.forEach { item: Items -> itemsList.add(item) }
        }
        customAdapter.notifyDataSetChanged()
    }
}