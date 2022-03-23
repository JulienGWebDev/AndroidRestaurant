package fr.isen.guinhut.androiderestaurant

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import fr.isen.guinhut.androiderestaurant.databinding.ActivityItemBinding
import fr.isen.guinhut.androiderestaurant.models.Commande
import fr.isen.guinhut.androiderestaurant.models.Items
import fr.isen.guinhut.androiderestaurant.models.Panier
import fr.isen.guinhut.androiderestaurant.view.ViewPagerAdapter
import pl.polak.clicknumberpicker.ClickNumberPickerListener
import java.io.FileWriter
import java.io.PrintWriter


class ItemActivity : AppCompatActivity() {
    private val commandes = ArrayList<Commande>()
    private val panier = Panier(commandes)
    private lateinit var binding : ActivityItemBinding
    private lateinit var viewPager: ViewPager
    private lateinit var mViewPagerAdapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val gson = Gson()
        val strObj = intent.getStringExtra("item")
        val obj: Items = gson.fromJson(strObj, Items::class.java)
        title = obj.name_fr

        var textList = obj.images
        viewPager = binding.viewpager

        mViewPagerAdapter = ViewPagerAdapter(this, textList)

        viewPager.pageMargin = 15
        viewPager.setPadding(50, 0, 50, 0);
        viewPager.setClipToPadding(false)
        viewPager.setPageMargin(25)
        viewPager.adapter = mViewPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        binding.textViewName.text = obj.name_fr


        var ListChar = ""
        obj.ingredients.forEach(){
            if(it.equals(obj.ingredients.last())){
                ListChar = ListChar + it.name_fr + "."
            }else{
                ListChar = it.name_fr  +", " + ListChar
            }
        }
        binding.textViewIngredient.text = ListChar

        val picker = binding.numpick
        picker.setPickerValue(1f)
        var price = binding.button
        var prix = picker.getValue()*obj.prices[0].price.toFloat()
        price.setText(prix.toString()+ " €")


        picker.setClickNumberPickerListener(ClickNumberPickerListener { previousValue, currentValue, pickerClickType ->
            var prix = (currentValue)*(obj.prices[0].price.toFloat())
            price.setText(prix.toString()+ " €")
        })

        price.setOnClickListener {
            val commande = Commande(obj.name_fr, picker.getValue())
            panier.commandes.add(commande)
            Log.d("ItemActivity", Gson().toJson(panier))


            val path = "/json/data.json"
            try {
                PrintWriter(FileWriter(path)).use {
                    it.write(Gson().toJson(panier))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }





            val snack = Snackbar.make(it,obj.name_fr +" ajouté au panier",Snackbar.LENGTH_LONG)
            snack.show()

        }













    }
    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                // your logic here
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // your logic here
            }

            override fun onPageSelected(position: Int) {
                // your logic here
            }
        }


}