package com.rationwala.store.adapters


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.rationwala.dukana.OrderItems
import com.rationwala.store.R
import com.rationwala.store.classes.OrderClass
import kotlinx.android.synthetic.main.listtem_pendingorder.view.*


class OrderAdapter(var activity: Context, var lis:MutableList<OrderClass>,var x:String): RecyclerView.Adapter<OrderAdapter.Myholder>() {
    override fun onBindViewHolder(p0: Myholder, p1: Int) {

        p0.oid!!.text = lis[p1].orderid
        p0.status!!.text = lis[p1].status
        p0.slot!!.text = lis[p1].timeslot
        if(p0.status!!.text.equals("pending"))
            p0.status!!.setTextColor(Color.parseColor("#00cd6b"))
        else
            p0.status!!.setTextColor(Color.parseColor("#FE2F3c"))
        p0.date!!.text = lis[p1].date
        p0.amount!!.text = lis[p1].amount
        p0.type!!.text = lis[p1].type
        if(lis[p1].deliverytime.equals("0"))
        p0.time!!.text = "it's on process"
        else
            p0.time!!.text = lis[p1].deliverytime
        p0.view!!.setOnClickListener {
            activity.startActivity(
                Intent(activity, OrderItems::class.java).putExtra("oid", lis[p1].orderid)
                    .putExtra("address", lis[p1].address)
            )
        }
         if(lis[p1].confirmed.equals("1"))
         {
             p0.cimage!!.visibility = View.GONE
             p0.cimage1!!.visibility = View.VISIBLE
         }
            if(lis[p1].readyforpickup.equals("1"))
            {
                p0.rimage!!.visibility = View.GONE
                p0.rimage1!!.visibility = View.VISIBLE
            }
            if(lis[p1].delivered.equals("1"))
            {
                p0.dimage!!.visibility = View.GONE
                p0.dimage1!!.visibility = View.VISIBLE
            }
          /*  activity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);*/
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderAdapter.Myholder {
        var inf = LayoutInflater.from(activity)
        var v = inf.inflate(R.layout.listtem_pendingorder, p0, false)
        var myholder = OrderAdapter.Myholder(v)
        /*v.setOnClickListener{
            activity.startActivity(Intent(activity,OrderItems::class.java).putExtra("oid",lis[myholder.adapterPosition].orderid)
                .putExtra("address",lis[myholder.adapterPosition].address))

        }*/
        return myholder
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    class Myholder(v: View) : RecyclerView.ViewHolder(v) {

        var oid: TextView? = null
        var date:TextView? =null
        var time: TextView ? =null
        var status:TextView? =null
        var type:TextView? =null
        var view:Button? =null
        var amount:TextView?=null
        var cimage:ImageView? =null
        var rimage:ImageView? =null
        var dimage:ImageView? =null
        var cimage1:ImageView? =null
        var rimage1:ImageView? =null
        var dimage1:ImageView? =null
        var slot:TextView ? =null
        init {
           oid = v.tv_order_no
            date = v.tv_order_date
            time = v.tv_order_time
            status = v.tracking_date
            type = v.method1
            amount = v.tv_order_price
            view = v.order_details
            cimage = v.confirm_image
            rimage = v.delivered_image
            dimage = v.cancal_image
            cimage1 = v.confirm_image1
            rimage1 = v.delivered_image1
            dimage1 = v.cancal_image1
            slot = v.timeslot
        }

    }
}
