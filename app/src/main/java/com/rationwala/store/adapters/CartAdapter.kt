package com.rationwala.store.adapters


import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rationwala.store.CartActivity
import com.rationwala.store.R
import com.rationwala.store.classes.CartClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.cart_view.view.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.cart_view.view.cost
import kotlinx.android.synthetic.main.cart_view.view.image
import kotlinx.android.synthetic.main.cart_view.view.name


class CartAdapter(var activity: CartActivity, var lis:MutableList<CartClass>): RecyclerView.Adapter<CartAdapter.Myholder>() {
    var c = 0
    override fun onBindViewHolder(p0: Myholder, p1: Int) {
        if(lis.size==1)
            c=1
        var uid = FirebaseAuth.getInstance().uid
        var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("cart").child(lis[p1].item.key)
        Glide.with(activity).load(lis[p1].item.image).placeholder(R.mipmap.ic_launcher).into(p0.im!!)
        p0.name!!.text = lis[p1].item.name
        p0.quantity!!.text = ""+lis[p1].item.quantity

        dbase.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p2: DataSnapshot) {
                    if(p2.exists())
                    {
                        p0.count!!.text = p2.value.toString()
                        //p0.mrp!!.text ="\u20B9"+ String.format("%.2f",lis[p1].item.cost.toFloat()*p0.count!!.text.toString().toInt())
                        //p0.mrp!!.setPaintFlags(p0.mrp!!.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
                       // p0.discount!!.text = "You save \u20B9"+ String.format("%.2f",lis[p1].item.discount.toFloat()*p0.count!!.text.toString().toInt())
                        p0.cost!!.text = "\u20B9"+ String.format("%.2f",(lis[p1].item.cost.toFloat()-lis[p1].item.discount.toFloat())*p0.count!!.text.toString().toInt())

                    }

                }

            }
        )
        p0.minus!!.setOnClickListener {
            if(p0.count!!.text.toString().equals("1"))
            {

                dbase.removeValue()
                lis.removeAt(p1)
                if(c==1)
                    activity.clearcart()
                //notifyItemRemoved(p1)
                //notifyItemRangeChanged(p1,lis.size)
                notifyDataSetChanged()

            }
            else
            {
                dbase.setValue((p0.count!!.text.toString().toInt()-1).toString())
                notifyItemChanged(p1)
            }

        }
        p0.plus!!.setOnClickListener {
            if(p0.count!!.text.toString().toInt()>9)
            {
                val builder =
                    AlertDialog.Builder(activity)
                builder.setMessage("You have exceeded limit!")
                    .setCancelable(false)
                    .setPositiveButton(
                        "OK"
                    ) { dialog, id ->
                        //do things
                        dialog.cancel()
                    }
                val alert = builder.create()
                alert.show()
            }
            else{
                dbase.setValue((p0.count!!.text.toString().toInt()+1).toString())
                notifyItemChanged(p1)
            }

        }


        p0.remove!!.setOnClickListener {
            var alert= AlertDialog.Builder(activity)
            alert.setMessage("Delete item from cart?")
                .setCancelable(false)
                .setPositiveButton("yes",
                    DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                        dbase.removeValue()
                        lis.removeAt(p1)
                        if(c==1)
                            activity.clearcart()
                        //notifyItemRemoved(p1)
                        //notifyItemRangeChanged(p1,lis.size)
                        notifyDataSetChanged()
                    })
                .setNegativeButton("no",DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                })
            var al = alert.create()
            al.show()

        }


    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CartAdapter.Myholder {
        var inf = LayoutInflater.from(activity)
        var v = inf.inflate(R.layout.cart_view, p0, false)
        var myholder = CartAdapter.Myholder(v)
        return myholder
    }

    override fun getItemCount(): Int {
        return lis.size
    }

    class Myholder(v: View) : RecyclerView.ViewHolder(v) {

        //var discount: TextView? = null
        var cost:TextView? =null
        //var mrp:TextView? =null
        var im:ImageView? =null
        var name:TextView? =null
        var count:TextView? =null
        var plus:TextView? =null
        var minus:TextView? =null
        var quantity:TextView? =null
        var remove:TextView? =null

        init {
            //discount = v.discount
            cost = v.cost
            //mrp = v.mrp
            name = v.name
            im = v.image
            count = v.count
            plus = v.plus
            minus = v.minus
            quantity = v.quan
            remove = v.remove
        }

    }
}
