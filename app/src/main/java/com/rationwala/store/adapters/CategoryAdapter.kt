package com.rationwala.store.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rationwala.store.R
import com.rationwala.store.classes.CategoryClass
import kotlinx.android.synthetic.main.category_view.view.*
import java.util.*


class CategoryAdapter(var activity: FragmentActivity?, var lis:MutableList<CategoryClass>): RecyclerView.Adapter<CategoryAdapter.Myholder>() {


    override fun onBindViewHolder(p0: Myholder, p1: Int) {

//        boolean mines = true ;
        val rnd = Random()
        val currentColor = Color.argb(
            255,
            rnd.nextInt(256),
            rnd.nextInt(256),
            rnd.nextInt(256)
        ) //bright colors

        p0.lin!!.setBackgroundColor(currentColor)

        Glide.with(activity!!).load(lis[p1].image).placeholder(R.mipmap.ic_launcher).dontAnimate().dontTransform().into(p0.image!!)
        var name = lis[p1].name.substring(2)
        p0.name!!.text = name[0].toUpperCase().toString()+name.substring(1)
       p0.r!!.visibility = View.GONE
        p0.ad!!.visibility = View.GONE

        p0.lin!!.setOnClickListener {

            if(p0.m)
            {
                p0.m =false
                p0.r!!.visibility = View.VISIBLE
                get_subcategory(lis[p1].lis,p0.r!!,lis[p1].name)
                p0.au!!.visibility = View.GONE
                p0.ad!!.visibility = View.VISIBLE

            }
            else
            {
                p0.m =true
                p0.r!!.visibility = View.GONE
                p0.au!!.visibility = View.VISIBLE
                p0.ad!!.visibility = View.GONE
            }
        }


    }

    private fun get_subcategory(lis: MutableList<String>, r: RecyclerView,cat:String) {
        val cateAdapter = SubCatAdapter(activity!!,lis ,cat)
        r.setLayoutManager(LinearLayoutManager(activity))
        r.setAdapter(cateAdapter)
        cateAdapter.notifyDataSetChanged()
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CategoryAdapter.Myholder {
        var inf = LayoutInflater.from(activity)
        var v = inf.inflate(R.layout.category_view, p0, false)
        var myholder = CategoryAdapter.Myholder(v)


        return myholder
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    class Myholder(v: View) : RecyclerView.ViewHolder(v) {

        var name: TextView? = null
        var image:ImageView? =null
        var lin: LinearLayout? =null
        var au:ImageView? =null
        var ad:ImageView? =null
        var r:RecyclerView? = null
        var m =true
        init {
            name = v.name
            image = v.image
            lin=v.ll1
            au = v.arrowup
            ad = v.arrowdown
            r = v.subrecycle
        }

    }
}
