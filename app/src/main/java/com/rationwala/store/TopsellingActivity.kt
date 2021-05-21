package com.rationwala.store

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.rationwala.store.adapters.ViewpagerAdapter
import com.rationwala.store.classes.CartClass
import com.rationwala.store.classes.ItemClass1
import com.rationwala.store.fragments.TabFrag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_topselling.back
import kotlinx.android.synthetic.main.activity_topselling.bottoml
import kotlinx.android.synthetic.main.activity_topselling.cart
import kotlinx.android.synthetic.main.activity_topselling.ccount
import kotlinx.android.synthetic.main.activity_topselling.cost1
import kotlinx.android.synthetic.main.activity_topselling.hrview
import kotlinx.android.synthetic.main.activity_topselling.items
import kotlinx.android.synthetic.main.activity_topselling.next


@Suppress("DEPRECATION")
class TopsellingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topselling)
        var lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        hrview.layoutManager = lm

        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
        }
        var uid = FirebaseAuth.getInstance().uid
        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("cart")

        db.addValueEventListener(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        var c=0
                        var chi = p0.children
                        chi.forEach {
                            c++
                        }
                        ccount.text = c.toString()
                    }
                    else
                    {
                        ccount.setText("0")
                    }
                }

            }
        )
        cart.setOnClickListener {
            startActivity(Intent(this,CartActivity::class.java)
                .putExtra("ac","main"))
            finish()
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }
        //starts
        var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("cart")
        dbase.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        bottoml.visibility = View.VISIBLE
                        var lis = mutableListOf<CartClass>()
                        var chi = p0.children
                        var last = p0.children.last().key.toString()
                        chi.forEach {

                            var x = it.key.toString().split(":")
                            var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("categories")
                                .child(x[0]).child("subcategory")
                                .child(x[1]).child(x[2])
                            var qu = (x[3].toInt()).toString()
                            db.addListenerForSingleValueEvent(
                                object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {

                                    }

                                    override fun onDataChange(p1: DataSnapshot) {
                                        var chi1 = p1.children
                                        var map = HashMap<String, String>()


                                        chi1.forEach {
                                            when (it.key) {
                                                "quantity" -> {
                                                    var chi2 = it.children
                                                    chi2.forEach {
                                                        when (it.key) {
                                                            qu -> {
                                                                var chi3 = it.children
                                                                chi3.forEach {
                                                                    map.put(
                                                                        it.key.toString(),
                                                                        it.value.toString()
                                                                    )
                                                                }
                                                                //Toast.makeText(this@CartActivity,map.getValue("cost"),Toast.LENGTH_LONG).show()
                                                            }
                                                        }
                                                        //Toast.makeText(this@CartActivity,it.key.toString(),Toast.LENGTH_LONG).show()

                                                    }
                                                }
                                                else -> {
                                                    map.put(
                                                        it.key.toString(),
                                                        it.value.toString()
                                                    )
                                                }
                                            }

                                        }
                                        var i = ItemClass1(
                                            map.getValue("name"),
                                            map.getValue("image"),
                                            map.getValue("cost"),
                                            map.getValue("desc"),
                                            map.getValue("discount"),
                                            x[0],
                                            x[1],
                                            "",
                                            map.getValue("quantity"), it.key.toString()
                                        )
                                        var c = CartClass(x[2], i, it.value.toString().toInt())
                                        lis.add(c)
                                        if (lis.size != 0 && it.key.toString().equals(last)) {
                                            var price = 0.0
                                            var discount1 = 0.0
                                            var stotal = 0.0
                                            for (i in 0..lis.size - 1) {
                                                price =
                                                    price + ((lis[i].item.cost.toFloat() - lis[i].item.discount.toFloat()) * lis[i].count)
                                                discount1 += lis[i].item.discount.toFloat() * lis[i].count
                                                stotal += lis[i].item.cost.toFloat() * lis[i].count
                                                //price = price * 2
                                            }
                                            //Toast.makeText(this@ItemsActivity,String.format("%.2f", price),Toast.LENGTH_LONG).show()
                                            cost1.text =   "\u20B9 "+String.format("%.2f", price)
                                            items.text = lis.size.toString()+" items"
                                            next.setOnClickListener {
                                                startActivity(
                                                    Intent(
                                                        this@TopsellingActivity,
                                                        CartActivity::class.java
                                                    ).putExtra("ac","main"))

                                                finish()
                                                overridePendingTransition(R.anim.slide_in_right,
                                                    R.anim.slide_out_left);


                                            }

                                        }


                                    }

                                }
                            )

                        }

                    }
                    else
                    {
                        bottoml.visibility = View.GONE
                    }

                }
            }
        )


        //ends
        var lis = mutableListOf<String>()
        lis.add("TOP SELLING")
        lis.add("RECENT SELLING")
        lis.add("DEALS OF THE DAY")
        lis.add("WHAT'S NEW")
        hrview.adapter = ViewpagerAdapter(this,lis)
        var bundle = Bundle()
        bundle.putString("item","TOP SELLING")
        bundle.putString("activity","topactivity")
        var im = TabFrag()
        im.arguments = bundle
        var frag = supportFragmentManager
        var tx = frag.beginTransaction()
        tx.add(R.id.frag2,im)
        tx.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right);
    }
    override fun onStart() {
        super.onStart()
        var cm=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm.activeNetworkInfo==null)
        {
            startActivity(Intent(this,NetConnection::class.java))
        }
    }

}