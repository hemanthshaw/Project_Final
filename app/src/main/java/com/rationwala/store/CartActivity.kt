package com.rationwala.store

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rationwala.store.adapters.CartAdapter
import com.rationwala.store.classes.CartClass
import com.rationwala.store.classes.ItemClass1
import com.rationwala.store.classes.LocationClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.back
import kotlinx.android.synthetic.main.activity_cart.items



@Suppress("DEPRECATION")
class CartActivity : AppCompatActivity(),AddAddress1 {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        var uid = FirebaseAuth.getInstance().uid
        var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("cart")
        var lm= LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL,false)
        rview.layoutManager = lm
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(this)
        var ar = ArcConfiguration(this)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        pd.setTitle("Loading..")
        pd.show()
        // Toast.makeText(this,intent.getStringExtra("cato")+" "+intent.getStringExtra("subcato")+" "+intent.getStringExtra("activity"),Toast.LENGTH_LONG).show()
        back.setOnClickListener {
            if(intent.getStringExtra("ac")!!.equals("item"))
            {
                startActivity(Intent(this, ItemsActivity::class.java).putExtra("cato",intent.getStringExtra("cato")
                ).putExtra("subcato",intent.getStringExtra("subcato")).putExtra("activity",intent.getStringExtra("activity"))
                    .putExtra("pos",intent.getStringExtra("pos")))
                finish()
                overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
            }
            else if(intent.getStringExtra("ac")!!.equals("main"))
            {
                startActivity(Intent(this,TopsellingActivity::class.java))
                finish()
                overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
            }
            else{
                finish()
                overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
            }


        }
        start.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);


        }

        var y=0
        //var count=0
        //var s = Semaphore(0)
        var user =FirebaseAuth.getInstance().currentUser
        if(user==null)
        {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }
        else {
            dbase.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
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
                                                //Toast.makeText(this@CartActivity,lis[0].key1,Toast.LENGTH_LONG).show()
                                                rview.visibility = View.VISIBLE
                                                l1.visibility = View.GONE
                                                ll_Checkout.visibility = View.VISIBLE
                                                pd.dismiss()
                                                if (y == 0)
                                                    rview.adapter =
                                                        CartAdapter(this@CartActivity, lis)
                                                y++
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
                                                cost.text =   "\u20B9 "+String.format("%.2f", price)
                                                items.text = lis.size.toString()+" items"

                                            }


                                        }

                                    }
                                )

                            }

                        } else {
                            pd.dismiss()
                            rview.visibility = View.GONE
                            l1.visibility = View.VISIBLE
                           ll_Checkout.visibility = View.GONE


                        }


                    }
                }
            )

            dbase.addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
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
                                                cost.text =   "\u20B9 "+String.format("%.2f", price)
                                                items.text = lis.size.toString()+" items"

                                                continu.setOnClickListener {
                                                    startActivity(
                                                        Intent(
                                                            this@CartActivity,
                                                            SelectDelivery::class.java
                                                        ).putExtra(
                                                            "price",
                                                            String.format("%.2f", price)
                                                        )
                                                            .putExtra(
                                                                "subtotal",
                                                                String.format("%.2f", stotal)
                                                            )
                                                            .putExtra(
                                                                "discount",
                                                                String.format("%.2f", discount1)
                                                            )
                                                            .putExtra("couponid","no coupon")
                                                    )
                                                    overridePendingTransition(R.anim.slide_in_right,
                                                        R.anim.slide_out_left);

                                                }

                                            }

                                        }

                                    }
                                )

                            }

                        }

                    }
                }
            )
        }
    }




    override fun addAddress(l: LocationClass) {
    }

    override fun clearcart()
    {
        rview.visibility = View.GONE
        l1.visibility = View.VISIBLE
        ll_Checkout.visibility = View.GONE

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(intent.getStringExtra("ac")!!.equals("item"))
        {
            startActivity(Intent(this,ItemsActivity::class.java).putExtra("cato",intent.getStringExtra("cato")
            ).putExtra("subcato",intent.getStringExtra("subcato")).putExtra("activity",intent.getStringExtra("activity"))
                .putExtra("pos",intent.getStringExtra("pos")))
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
        }
        else if(intent.getStringExtra("ac")!!.equals("main"))
        {
            startActivity(Intent(this,TopsellingActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
        }
        else{
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
        }


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