package com.rationwala.store

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

@Suppress("DEPRECATION")
class ForgotPass : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        send.setOnClickListener {
            var e = email.text.toString()
            var cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(email.text.toString().isEmpty())
            {
                email.setError("Email!")
            }
            else if (cm.activeNetworkInfo == null) {
                Toast.makeText(this@ForgotPass, "please connect to internet", Toast.LENGTH_LONG)
                    .show()

            } else {
                if(checkLong(email.text.toString())) {
                    val min = 100000
                    val max = 999999
                    val random = Random.nextInt((max - min) + 1) + min
                    //Toast.makeText(this,random.toString(),Toast.LENGTH_LONG).show()
                    var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    if (!e.startsWith("+91")) {
                        e = "+91" + e
                        sendSms(e,random)
                    }
                    else {
                        sendSms(e,random)

                    }
                }


                var pd = ProgressDialog(this@ForgotPass)
                pd.setTitle("Please wait..")
                pd.show()
                var e = email.text.toString()
                var mAuth = FirebaseAuth.getInstance()
                mAuth.sendPasswordResetEmail(e)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            pd.dismiss()
                            if (p0.isSuccessful) {
                                Toast.makeText(
                                    this@ForgotPass,
                                    "check your email",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            } else
                                Toast.makeText(
                                    this@ForgotPass,
                                    "you are not registered",
                                    Toast.LENGTH_LONG
                                ).show()


                        }

                    }
                    )

            }
        }
    }

    private fun sendSms(num: String,random:Int){

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

            email.setText("")
            header.text = "Enter OTP"
            send1.text="CONFIRM"
            email.setHint("Enter OTP")
            if(send1.text.toString().equals("CONFIRM")) {
                send.setOnClickListener {
                    if (email.text.toString().isEmpty())
                        email.setError("enter otp!")
                    else {
                        var o = email.text.toString()
                        if (o.equals(random.toString())) {
                            //Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
                            var uid= FirebaseAuth.getInstance().uid.toString()
                           /* var i = Intent(this,NewPassword::class.java)
                            i.putExtra("num",num)
                            finish()*/

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

    private fun checkLong(e: String): Boolean {
        for( i in 1..e.length-1)
        {
            if(!(e[i]-'0'>=0 && e[i]-'0'<=9))
                return false
        }
        return true
    }

}