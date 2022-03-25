package fr.isen.guinhut.androiderestaurant

import android.content.Context
import android.content.Intent
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
import java.io.File


class ItemActivity : AppCompatActivity() {
    private val commandes = ArrayList<Commande>()
    private var panier = Panier(commandes)
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

        binding.toolbar.title=obj.name_fr

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


        var listChar = ""
        obj.ingredients.forEach(){
            var addString = upperFirst(it.name_fr)
            if(it.equals(obj.ingredients.last())){

                listChar = listChar + addString + "."
            }else{
                listChar = addString  +", " + listChar
            }
        }
        binding.textViewIngredient.text = listChar


        val picker = binding.numpick
        picker.setPickerValue(1f)
        var price = binding.button
        var panierButton = binding.btnPanier

        var prix = picker.getValue()*obj.prices[0].price.toFloat()
        price.setText(prix.toString()+ " €")

        picker.setClickNumberPickerListener(ClickNumberPickerListener { previousValue, currentValue, pickerClickType ->
            var prix = (currentValue)*(obj.prices[0].price.toFloat())
            price.setText(prix.toString()+ " €")
        })

        binding.badgePanier.text=initPanier()


        price.setOnClickListener {
            val snack = Snackbar.make(it,"Ajouté au panier",Snackbar.LENGTH_LONG)
            snack.show()
            binding.badgePanier.text=ecriturePanier(binding.numpick.value,obj)
        }

        panierButton.setOnClickListener {
            this.panier=lecturePanier()
            val intent = Intent(this,PanierActivity::class.java)
            startActivity(intent)
        }




    }

    override fun onRestart() {
        super.onRestart()
        binding.badgePanier.text=panier.commandes.size.toString()
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

    private fun upperFirst(word: String): String {
        var firstLetStr: String = word.substring(0, 1)
        val remLetStr: String = word.substring(1)
        firstLetStr = firstLetStr.uppercase()
        return firstLetStr + remLetStr
    }



    private fun ecriturePanier(value: Float, item:Items): String {
        //sauvegarde du panier en json dans les fichiers
        val commande = Commande(value,item)
        //lecture du fichier
        val filename1 = "panier.json"
        val file = File(binding.root.context.filesDir, filename1)
        //si le fichier existe on lit le panier directement dedans
        panier = if(file.exists()){
            val contents = file.readText()
            Gson().fromJson(contents,Panier::class.java)
        }
        //si le fichier n'existe pas on cree un panier vide
        else{
            Panier(ArrayList())
        }
        //puis on ajoute notre element au panier et on ecrit le fichier
        panier.commandes.add(commande)
        val panierJson = Gson().toJson(panier)
        Log.d("Panier",panierJson)
        val filename = "panier.json"
        this.binding.root.context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(panierJson.toByteArray())
        }
        return panier.commandes.size.toString()
    }

    private fun initPanier(): String {

        //lecture du fichier
        val filename1 = "panier.json"
        val file = File(binding.root.context.filesDir, filename1)
        //si le fichier existe on lit le panier directement dedans
        panier = if(file.exists()){
            val contents = file.readText()
            Gson().fromJson(contents,Panier::class.java)
        }
        //si le fichier n'existe pas on cree un panier vide
        else{
            Panier(ArrayList())
        }
        val panierJson = Gson().toJson(panier)
        Log.d("Panier",panierJson)
        val filename = "panier.json"
        this.binding.root.context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(panierJson.toByteArray())
        }
        return panier.commandes.size.toString()
    }





}