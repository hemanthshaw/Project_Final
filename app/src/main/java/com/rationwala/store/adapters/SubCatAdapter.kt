package com.rationwala.store.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.rationwala.store.ItemsActivity
import com.rationwala.store.R
import kotlinx.android.synthetic.main.row_sub_cat_item.view.*


class SubCatAdapter(var activity: Context, var lis:MutableList<String>, var cato:String): RecyclerView.Adapter<SubCatAdapter.Myholder>() {


    override fun onBindViewHolder(p0: Myholder, p1: Int) {
        p0.name!!.text =lis[p1]
        p0.l!!.setOnClickListener {
            activity.startActivity(Intent(activity,ItemsActivity::class.java).putExtra("cato",cato).putExtra("subcato",lis[p1])
                .putExtra("pos",p1.toString()))
        }
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SubCatAdapter.Myholder {
        var inf = LayoutInflater.from(activity)
        var v = inf.inflate(R.layout.row_sub_cat_item, p0, false)
        var myholder = SubCatAdapter.Myholder(v)


        return myholder
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    class Myholder(v: View) : RecyclerView.ViewHolder(v) {

        var name: TextView? = null
        var image: ImageView? =null
        var l : LinearLayout? =null
        init {
            name = v.Pname
            image = v.Pimage
            l = v.cardView
        }

    }
}
