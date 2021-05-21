package com.rationwala.store

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_my_wallet.*

@Suppress("DEPRECATION")
class MyWallet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)
        titl.text = intent.getStringExtra("title")
        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
        }
        var uid = FirebaseAuth.getInstance().uid
        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("wallet")
        db.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                   if(p0.exists())
                   {
                       balance.text ="₹ "+ p0.value.toString()
                   }
                    else
                   {
                       balance.text = "₹ 0"
                   }
                }

            }
        )
        addmoney.setOnClickListener {
            startActivity(Intent(this,Addmoney::class.java))
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        }
        start.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right);
    }

    override fun onStart() {
        super.onStart()
        var user  = FirebaseAuth.getInstance().currentUser
        if(user==null)
        {
            startActivity(Intent(this,LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }
        var cm=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm.activeNetworkInfo==null)
        {
            startActivity(Intent(this,NetConnection::class.java))
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        }
    }
}