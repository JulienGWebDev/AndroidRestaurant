package fr.isen.guinhut.androiderestaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso

class ViewPagerAdapter (private val mContext: Context, private  val itemList: ArrayList<String>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater!!.inflate(R.layout.frag_content, container, false)


        var imgItem: ImageView = view.findViewById(R.id.slide_screen_item_iv)
        if (itemList[position].isEmpty()) {
            imgItem.setImageResource(R.drawable.img2)
        } else{
            Picasso.get().load(itemList[position]).into(imgItem)
        }


        container.addView(view, position)
        return view
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}
