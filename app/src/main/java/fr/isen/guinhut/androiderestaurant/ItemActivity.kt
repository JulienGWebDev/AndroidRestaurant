package fr.isen.guinhut.androiderestaurant

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import fr.isen.guinhut.androiderestaurant.databinding.ActivityItemBinding
import fr.isen.guinhut.androiderestaurant.models.Items


class ItemActivity : AppCompatActivity() {
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
            ListChar = it.name_fr  +", " + ListChar
            if(it.equals(obj.ingredients.last())){
                ListChar = ListChar + " " + it.name_fr + "."
            }
        }
        binding.textViewIngredient.text = ListChar





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