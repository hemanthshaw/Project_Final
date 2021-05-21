package com.rationwala.store

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_net_connection.*

@Suppress("DEPRECATION")
class NetConnection : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_connection)

        tryagain.setOnClickListener {
            var cm=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(cm.activeNetworkInfo!=null)
            {
                finish()
            }
            else{
                Toast.makeText(this,"please connect to internet",Toast.LENGTH_LONG).show()
            }
        }
    }
}