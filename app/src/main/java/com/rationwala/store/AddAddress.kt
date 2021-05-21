package com.rationwala.store

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import com.rationwala.store.classes.LocationClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_address.*

@Suppress("DEPRECATION")
open class AddAddress : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);

        }
        if(intent.getStringExtra("from")!=null)
        {
            var lati = intent.getStringExtra("lati")
            var longi = intent.getStringExtra("longi")
            name.setText(intent.getStringExtra("lname"), TextView.BufferType.EDITABLE)
            hno.setText(intent.getStringExtra("hno"), TextView.BufferType.EDITABLE)
            landmark.setText(intent.getStringExtra("landmark"), TextView.BufferType.EDITABLE)
            area.setText(intent.getStringExtra("area"), TextView.BufferType.EDITABLE)
            pincode.setText(intent.getStringExtra("pincode"), TextView.BufferType.EDITABLE)
            mobile.setText(intent.getStringExtra("mobile"), TextView.BufferType.EDITABLE)
            save.setOnClickListener {
                var uid = FirebaseAuth.getInstance().uid
                var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation")
                    .child(uid.toString()).child("mylocation")
                var x=intent.getStringExtra("from")
                var l=LocationClass(name.text.toString(),hno.text.toString(),area.text.toString(),landmark.text.toString()
                    ,pincode.text.toString(),lati!!.toString(),longi!!.toString(),mobile.text.toString())
                dbase.child(x!!).setValue(l)
                FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation") .child(uid.toString()).child("address").setValue(l)
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                finish()

            }




        }
        else {
            save.setOnClickListener {
                if (TextUtils.isEmpty(name.text.toString())
                    || TextUtils.isEmpty(hno.text.toString()) || TextUtils.isEmpty(landmark.text.toString()) || TextUtils.isEmpty(
                        area.text.toString()
                    )
                    || TextUtils.isEmpty(pincode.text.toString())|| TextUtils.isEmpty(mobile.text.toString())
                )
                    Toast.makeText(this, "Fill all details", Toast.LENGTH_LONG).show()
                else {
                    var lati = intent.getStringExtra("lati")
                    var longi = intent.getStringExtra("longi")
                    var l = LocationClass(
                        name.text.toString(),
                        hno.text.toString(),
                        area.text.toString()
                        ,
                        landmark.text.toString(),
                        pincode.text.toString(),
                        lati!!.toString(),
                        longi!!.toString(),mobile.text.toString()
                    )
                    var uid = FirebaseAuth.getInstance().uid
                    var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation")
                        .child(uid.toString()).child("mylocation")
                    dbase.child(dbase.push().key.toString()).setValue(l)
                    FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation")
                        .child(uid.toString()).child("address").setValue(l)
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                    overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    );
                    finish()

                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right);
        finish()
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