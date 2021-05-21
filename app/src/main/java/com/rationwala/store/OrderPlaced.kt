package com.rationwala.store
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_order_placed.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Suppress("DEPRECATION")
class OrderPlaced : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)
        var id = intent.getStringExtra("orderid")
        var num = intent.getStringExtra("mobile")
        if(!num.equals("xxx")) {
            var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("myinformation").child("apikey")
            db.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            var key = p0.value.toString()
                            var mt = MyTask(this@OrderPlaced, id!!, num!!, key)
                            mt.execute()
                        }
                    }

                }
            )

            orderid.text = id
        }
        //Toast.makeText(this,num, Toast.LENGTH_LONG).show()
        done.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java).putExtra("activity","orderplaced"))
            overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
            startActivity(Intent(this, MainActivity::class.java).putExtra("activity","orderplaced"))
        Toast.makeText(this,"you cant go back",Toast.LENGTH_LONG).show()
            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            );

    }
    class MyTask(var context:OrderPlaced,var x:String,var num:String,var key:String
    ) : AsyncTask<Unit, Unit, Unit>()

    {

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: Unit?) {
            try
            {

                // Construct data
                val apiKey = "apikey=" + key
                val message = "&message=" +("you have an order! \n orderid: "+x)
                val sender = "&sender=" + "TXTLCL"
                val numbers = "&numbers=" + num

                // Send data
                val conn=
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
            } catch (e: Exception) {

            }

            var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)


        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

        }
    }
}