package com.rationwala.store.adapters


import com.rationwala.dukana.classes.NotificationClass
import com.rationwala.store.R
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rationwala.store.ItemsActivity

import kotlinx.android.synthetic.main.noti_view.view.*


class NotificationAdapter(var activity: FragmentActivity, var lis:MutableList<NotificationClass>): RecyclerView.Adapter<NotificationAdapter.Myholder>() {


    override fun onBindViewHolder(p0: Myholder, p1: Int) {
       p0.title!!.text = lis[p1].title
        p0.body!!.text = lis[p1].body
        p0.date!!.text = "Notification on "+lis[p1].time
        Glide.with(activity).load(lis[p1].image).into(p0.img!!)
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NotificationAdapter.Myholder {
        var inf = LayoutInflater.from(activity)
        var v = inf.inflate(R.layout.noti_view, p0, false)
        var myholder = NotificationAdapter.Myholder(v)

        v.setOnClickListener {
            if(!lis[myholder.adapterPosition].category.equals("0")) {
                activity.startActivity(
                    Intent(activity, ItemsActivity::class.java)
                        .putExtra("cato", lis[myholder.adapterPosition].category)
                        .putExtra("subcato", lis[myholder.adapterPosition].subcategory)
                        .putExtra("pos",lis[myholder.adapterPosition].pos)
                )
                activity.overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left);
            }


        }

        return myholder
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    class Myholder(v: View) : RecyclerView.ViewHolder(v) {

        var title: TextView? = null
        var body: TextView? = null
        var img: ImageView? = null
        var date: TextView? = null

        init {
            title = v.title
            body = v.body
            img = v.img
            date = v.date
        }

    }
}
