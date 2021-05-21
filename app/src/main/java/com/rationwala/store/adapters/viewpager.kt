package com.rationwala.store.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.rationwala.store.R
import com.rationwala.store.fragments.HomeFragment
import kotlinx.android.synthetic.main.hori_view.view.*


class viewpager(var activity: FragmentActivity?, var lis:MutableList<String>,var h:HomeFragment): RecyclerView.Adapter<viewpager.Myholder>() {

    var c=0
    override fun onBindViewHolder(p0: Myholder, p1: Int) {
        p0.lname!!.text =lis[p1]
        if(c==p0.adapterPosition)
        {
            //p0.l!!.setBackgroundColor(Color.parseColor("#11c87b"))
            p0.lname!!.setTextColor(Color.parseColor("#63c9ff"))

        }
        else{
            p0.lname!!.setTextColor(Color.parseColor("#707070"))
        }

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): viewpager.Myholder {
        var inf = LayoutInflater.from(activity)
        var v = inf.inflate(R.layout.hori_view1, p0, false)
        var myholder = viewpager.Myholder(v)
        v.setOnClickListener {
            myholder.lname!!.setTextColor(Color.parseColor("#63c9ff"))

            if(c!=myholder.adapterPosition)
            {
                notifyItemChanged(c)
                c=myholder.adapterPosition
            }
            h.changefrag(lis[myholder.adapterPosition])
        }

        return myholder
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    class Myholder(v: View) : RecyclerView.ViewHolder(v) {

        var lname: TextView? = null

        init {
            lname = v.lview
        }

    }
}
