package com.rationwala.store.adapters


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rationwala.store.ItemsActivity
import com.rationwala.store.R
import com.rationwala.store.fragments.ItemsFrag
import kotlinx.android.synthetic.main.hori_view.view.*


class HorizantalAdapter(var activity: ItemsActivity, var lis:MutableList<String>, var cato:String,var x:String): RecyclerView.Adapter<HorizantalAdapter.Myholder>() {

    var c=x.toInt()
    override fun onBindViewHolder(p0: Myholder, p1: Int) {
        p0.lname!!.text =lis[p1]
        if(c==p0.adapterPosition)
        {
            //p0.l!!.setBackgroundColor(Color.parseColor("#11c87b"))
            p0.cardv!!.setBackgroundColor(Color.parseColor("#63c9ff"))
            p0.lname!!.setTextColor(Color.parseColor("#FFFFFF"))

        }
        else{
            p0.cardv!!.setBackgroundColor(Color.parseColor("#FFFFFF"))
            p0.lname!!.setTextColor(Color.parseColor("#707070"))
        }

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HorizantalAdapter.Myholder {
        var inf = LayoutInflater.from(activity)
        var v = inf.inflate(R.layout.hori_view, p0, false)
        var myholder = HorizantalAdapter.Myholder(v)
        v.setOnClickListener {
            myholder.cardv!!.setBackgroundColor(Color.parseColor("#63c9ff"))
            myholder.lname!!.setTextColor(Color.parseColor("#FFFFFF"))

            if(c!=myholder.adapterPosition)
            {
                notifyItemChanged(c)
                c=myholder.adapterPosition
            }
            var bundle = Bundle()
            bundle.putString("cato",cato)
            bundle.putString("subcat",lis[myholder.adapterPosition])
            var im = ItemsFrag()
            im.arguments = bundle
            var  frag = activity.supportFragmentManager
            var tx = frag.beginTransaction()
            tx.replace(R.id.frag1,im)
            tx.commit()
        }

        return myholder
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    class Myholder(v: View) : RecyclerView.ViewHolder(v) {

        var lname: TextView? = null
        var cardv: CardView? =null

        init {
            lname = v.lview
            cardv = v.cardv
        }

    }
}
