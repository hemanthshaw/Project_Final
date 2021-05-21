package com.rationwala.store

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_select_delivery.*


@Suppress("DEPRECATION")
class SelectDelivery : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_delivery)
        var lm= LinearLayoutManager(this@SelectDelivery,LinearLayoutManager.HORIZONTAL,false)
        //rview.layoutManager = lm
        change.setOnClickListener {
            startActivity(Intent(this,MyAddress::class.java))
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }
        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);

        }
        continu.setOnClickListener {
            paynow()
        }
        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("myinformation").child("ordertiming")
        db.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        ordertiming.text = p0.value.toString()
                    }
                }

            }
        )


        loadAddress(this)

    }

    private fun loadAddress(selectDelivery: SelectDelivery) {
        var uid = FirebaseAuth.getInstance().uid
        var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("address")

        dbase.addValueEventListener(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        var chi = p0.children
                        var map = HashMap<String,String>()
                        chi.forEach {
                            map.put(it.key.toString(),it.value.toString())
                        }
                        //name.text = map.getValue("locationname")
                        myaddress.text = "House No: "+map.getValue("address")+","+"Area: "+map.getValue("area")+","+
                                "Landmark: "+map.getValue("landmark")+","+
                                "Pincode: "+map.getValue("pincode")

                        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("deliveryprice").child(map.getValue("pincode"))
                        db.addValueEventListener(
                            object : ValueEventListener
                            {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if(p0.exists())
                                    {
                                        var x = intent.getStringExtra("subtotal")!!.toFloat()
                                        var y = intent.getStringExtra("discount")!!.toFloat()
                                        var z: Float ? = null
                                        if(x-y>250)
                                            z= 0.0F
                                        else
                                            z=p0.value.toString().toFloat()
                                        subtotal.text=" ₹"+x.toString()
                                        discount.text=" ₹"+y.toString()
                                        deliverycharges.text = " ₹"+z.toString()
                                        apply.setOnClickListener {
                                            if(apply.text.equals("remove"))
                                            {
                                                apply.text = "Apply Now"
                                                couponid.visibility = View.GONE
                                                coupon.text = "Avail coupon discount"
                                                amount.text = ""+ String.format("%.2f",x-y+z)

                                            }
                                            else {
                                                startActivity(
                                                    Intent(selectDelivery, ApplyCoupon::class.java).putExtra(
                                                        "subtotal",
                                                        intent.getStringExtra("subtotal")
                                                    )
                                                        .putExtra("discount", intent.getStringExtra("discount"))
                                                        .putExtra("price", intent.getStringExtra("price"))
                                                )
                                                finish()
                                                overridePendingTransition(
                                                    R.anim.slide_in_right,
                                                    R.anim.slide_out_left
                                                );
                                            }

                                        }
                                        if(!intent.getStringExtra("couponid").equals("no coupon"))
                                        {
                                            coupon.text = "coupon applied"
                                            apply.setText("remove")
                                            var couponprice = intent.getStringExtra("amount")!!.toFloat()
                                            couponid.text = "Your coupon - "+intent.getStringExtra("couponid")
                                            couponid.visibility = View.VISIBLE
                                            amount.text = ""+ String.format("%.2f",x-y+z-couponprice)
                                        }
                                        else{
                                            amount.text = ""+ String.format("%.2f",x-y+z)
                                        }


                                    }
                                    else
                                    {
                                        apply.setOnClickListener {
                                            Toast.makeText(this@SelectDelivery,"we cannot deliver to this address",Toast.LENGTH_LONG).show()
                                        }
                                        amount.text = ""
                                        Toast.makeText(this@SelectDelivery,"we cannot deliver to this address",Toast.LENGTH_LONG).show()
                                    }
                                }

                            }
                        )

                    }
                    else{
                        apply.setOnClickListener {
                            Toast.makeText(this@SelectDelivery,"please add your address",Toast.LENGTH_LONG).show()
                        }
                        change.setText("Add")
                        Toast.makeText(this@SelectDelivery,"Please add your address to make payment",Toast.LENGTH_LONG).show()


                    }


                }

            })

       /* var db= FirebaseDatabase.getInstance().getReference("timeslot")
        db.addValueEventListener(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var chi = p0.children
                    var lis = mutableListOf<String>()
                    chi.forEach {
                        lis.add(it.value.toString())
                    }
                    var adapter = TimeslotAdapter(this@SelectDelivery,lis)
                    rview.adapter = adapter

                    continu.setOnClickListener {
                        paynow(adapter.getSelected())
                    }


                }

            }
        )*/



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


    private fun paynow() {

        //var price = intent.getStringExtra("price")

        var ad = myaddress.text
        if(myaddress.text.toString().equals("Add delivery address here ..")){
            Toast.makeText(this,"Please add Your address",Toast.LENGTH_LONG).show()
        }
        else if(amount.text.toString().equals(""))
            Toast.makeText(this,"Cannot deliver to this address",Toast.LENGTH_LONG).show()
        else {

                var i = Intent(
                    this@SelectDelivery,
                    BillingActivity::class.java
                )
                i.putExtra(
                    "price",
                    amount.text.toString()
                )
                i.putExtra("timeslot", "")
                i.putExtra("address", myaddress.text.toString())
                i.putExtra(
                    "deliverycharges",
                    deliverycharges.text.toString()
                )
                i.putExtra("couponid",intent.getStringExtra("couponid"))
                startActivity(i)
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                );

        }



    }

    /*fun alertMessage(timeslot:String)
    {
        var pd= ProgressDialog(this@SelectDelivery)
        pd.setTitle("Please wait..")
        pd.show()
        var alert = AlertDialog.Builder(this@SelectDelivery)
        var v1 = layoutInflater.inflate(
            R.layout.freedelivery_view,
            null
        )
        alert.setView(v1)

        var dbase = FirebaseDatabase.getInstance().getReference("freedeliverypopup")
        dbase.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var chi = p0.children
                    chi.forEach {
                        when(it.key)
                        {
                            "1"->v1.tv1.text=it.value.toString()
                            "2"->v1.tv2.text = it.value.toString()
                        }
                    }
                    pd.dismiss()
                    var alert1 = alert.create()
                    alert1.setCanceledOnTouchOutside(false)
                    alert1.show()

                }

            }
        )
        v1.next.setOnClickListener {
            var i = Intent(
                this@SelectDelivery,
                BillingActivity::class.java
            )
            i.putExtra(
                "price",
                amount.text.toString().substring(1)
            )
            i.putExtra("timeslot", timeslot)
            i.putExtra("address", myaddress.text.toString())
            i.putExtra(
                "deliverycharges",
                deliverycharges.text.toString()
            )
            startActivity(i)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            );
        }
    }*/


}