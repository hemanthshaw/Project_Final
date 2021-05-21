package com.rationwala.store.adapters


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rationwala.store.ApplyCoupon
import com.rationwala.store.ItemsActivity
import com.rationwala.store.R
import com.rationwala.store.classes.Couponclass
import com.rationwala.store.fragments.ItemsFrag
import kotlinx.android.synthetic.main.coupon_view.view.*
import kotlinx.android.synthetic.main.hori_view.view.*


class CouponAdapter(var activity: ApplyCoupon, var lis:MutableList<Couponclass>,var price:String): RecyclerView.Adapter<CouponAdapter.Myholder>() {


    override fun onBindViewHolder(p0: Myholder, p1: Int) {
        p0.desc!!.text = lis[p1].desc
        p0.id!!.text = lis[p1].couponid
        p0.apply!!.setOnClickListener {
            //activity.addcoupon(lis[p1].amount,lis[p1].couponid)
            var total = price.toFloat()
            if(lis[p1].amount.toFloat()> total)
                Toast.makeText(activity,"Minimum order for this coupon needs to be Rs. "+lis[p1].amount+", please review your cart",Toast.LENGTH_LONG).show()
            else
            {
                activity.addcoupon(lis[p1].discount,lis[p1].couponid)
            }
        }
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CouponAdapter.Myholder {
        var inf = LayoutInflater.from(activity)
        var v = inf.inflate(R.layout.coupon_view, p0, false)
        var myholder = CouponAdapter.Myholder(v)

        return myholder
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    class Myholder(v: View) : RecyclerView.ViewHolder(v) {

        var id: TextView? = null
        var desc: TextView? =null
        var apply:TextView? =null

        init {
           id=v.couponid
            desc = v.desc
            apply = v.apply
        }

    }
}
