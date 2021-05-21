package com.rationwala.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rationwala.store.adapters.CouponAdapter
import com.rationwala.store.classes.Couponclass
import kotlinx.android.synthetic.main.activity_apply_coupon.*

class ApplyCoupon : AppCompatActivity(),couponinter {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_coupon)
        var lm= LinearLayoutManager(this@ApplyCoupon, LinearLayoutManager.VERTICAL,false)
        rview.layoutManager = lm
        back.setOnClickListener {
            startActivity(Intent(this,SelectDelivery::class.java).putExtra("subtotal",intent.getStringExtra("subtotal"))
                .putExtra("discount",intent.getStringExtra("discount")).putExtra("price",intent.getStringExtra("price"))
                .putExtra("couponid","no coupon"))
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
        }
        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("coupons")
        db.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        var chi = p0.children
                        var lis = mutableListOf<Couponclass>()
                        chi.forEach {
                            var chi2 = it.children
                            var map = HashMap<String,String>()
                            chi2.forEach {
                                map.put(it.key.toString(),it.value.toString())
                            }
                            var m = Couponclass(map.getValue("couponid"),map.getValue("desc"),map.getValue("amount"),map.getValue("discount"))
                            lis.add(m)
                        }

                        rview.adapter = CouponAdapter(this@ApplyCoupon,lis,intent.getStringExtra("price")!!)
                    }
                }

            }
        )
    }

    override fun addcoupon(s: String,id:String) {
           startActivity(Intent(this,SelectDelivery::class.java).putExtra("couponid",id).putExtra("amount",s)
               .putExtra("subtotal",intent.getStringExtra("subtotal"))
               .putExtra("discount",intent.getStringExtra("discount")).putExtra("price",intent.getStringExtra("price")))
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
            finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,SelectDelivery::class.java).putExtra("subtotal",intent.getStringExtra("subtotal"))
            .putExtra("discount",intent.getStringExtra("discount")).putExtra("price",intent.getStringExtra("price"))
            .putExtra("couponid","no coupon"))
        finish()
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right);

    }
}