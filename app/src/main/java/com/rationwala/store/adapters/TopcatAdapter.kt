package com.rationwala.store.adapters


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rationwala.store.ItemsActivity
import com.rationwala.store.R
import com.rationwala.store.classes.TopCatclass
import kotlinx.android.synthetic.main.row_home_rv.view.*
import java.util.*


class TopcatAdapter(var activity: Context, var lis:MutableList<TopCatclass>): RecyclerView.Adapter<TopcatAdapter.Myholder>() {

    override fun onBindViewHolder(p0: Myholder, p1: Int) {


        val rnd = Random()
        //  int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));  //bright colors
        //  int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));  //bright colors
        val baseColor = Color.WHITE

        val baseRed = Color.red(baseColor)
        val baseGreen = Color.green(baseColor)
        val baseBlue = Color.blue(baseColor)

        val red = (baseRed + rnd.nextInt(256)) / 2
        val green = (baseGreen + rnd.nextInt(256)) / 2
        val blue = (baseBlue + rnd.nextInt(256)) / 2
        val clr1 = Color.rgb(red, green, blue) //pastel colors

        p0.linerlayout!!.setBackgroundColor(clr1)

        Glide.with(activity).load(lis[p1].image).into(p0.im!!)
        p0.lname!!.text = lis[p1].subcategory

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TopcatAdapter.Myholder {
        var inf = LayoutInflater.from(activity)
        var v = inf.inflate(R.layout.row_home_rv, p0, false)
        var myholder = TopcatAdapter.Myholder(v)
        v.setOnClickListener {
            activity.startActivity(Intent( activity,ItemsActivity::class.java)
                .putExtra("cato",lis[myholder.adapterPosition].category)
                .putExtra("subcato",lis[myholder.adapterPosition].subcategory)
                .putExtra("pos",lis[myholder.adapterPosition].pos))
        }
        return myholder
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    class Myholder(v: View) : RecyclerView.ViewHolder(v) {

        var lname: TextView? = null
        var cardv: CardView? =null
        var linerlayout: LinearLayout? =null
        var im:ImageView? =null

        init {
            lname = v.tv_home_title
            cardv = v.cardview1
            linerlayout = v.ll1
            im = v.iv_home_img
        }

    }
}
