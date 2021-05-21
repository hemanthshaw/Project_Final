package com.rationwala.store

import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_addmobile.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random


class Addmobile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmobile)
        back.setOnClickListener {
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
            finish()
        }
        if(send1.text.toString().equals("send otp")) {
            send.setOnClickListener {
                var num = otp.text.toString()
                if (num.isEmpty()) {
                    otp.setError("enter your number!")
                } else {
                    val min = 100000
                    val max = 999999
                    val random = Random.nextInt((max - min) + 1) + min
                    //Toast.makeText(this,random.toString(),Toast.LENGTH_LONG).show()
                    var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    sendsms(num, random)
                }

            }
        }
    }

    private fun sendsms(num: String,random:Int){

        try
        {
            // Construct data
            val apiKey = "apikey=" + "6MAfDYoV3Ps-3xZ3Q0QJZ79zKlNQonPq89z5AETroz"
            val message = "&message=" + "Hi,Your OTP is"+random
            val sender = "&sender=" + "TXTLCL"
            val numbers = "&numbers=" + num

            // Send data
            val conn: HttpURLConnection =
                URL("https://api.textlocal.in/send/?").openConnection() as HttpURLConnection
            val data = apiKey + numbers + message + sender
            conn.setDoOutput(true)
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Content-Length", Integer.toString(data.length))
            conn.getOutputStream().write(data.toByteArray(charset("UTF-8")))
            val rd = BufferedReader(InputStreamReader(conn.getInputStream()))
            val stringBuffer = StringBuffer()
            var line: String? = null
            while (rd.readLine().also({ line = it }) != null) {
                stringBuffer.append(line!!)
            }
            rd.close()
            Toast.makeText(this,"check your mobile",Toast.LENGTH_LONG).show()

            otp.setText("")
            header.text = "Enter OTP"
            send1.text="CONFIRM"
            otp.setHint("Enter OTP")
            if(send1.text.toString().equals("CONFIRM")) {
                send.setOnClickListener {
                    if (otp.text.toString().isEmpty())
                        otp.setError("enter otp!")
                    else {
                        var o = otp.text.toString()
                        if (o.equals(random.toString())) {
                            Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
                            var uid= FirebaseAuth.getInstance().uid.toString()
                            var db= FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid)
                            db.child("mobile").setValue(num)

                            overridePendingTransition(R.anim.slide_in_left,
                                R.anim.slide_out_right);

                            finish()

                        } else {
                            Toast.makeText(this, "please provide correct otp", Toast.LENGTH_LONG)
                                .show()

                        }
                    }
                }
            }

        } catch (e: Exception) {
            Toast.makeText(this,"Try again",Toast.LENGTH_LONG).show()

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right);
        finish()
    }
}