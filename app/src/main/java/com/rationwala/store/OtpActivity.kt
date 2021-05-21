package com.rationwala.store

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_otp.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class OtpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        var cotp = intent.getStringExtra(("otp"))
        Toast.makeText(this@OtpActivity,"OTP sent to your mobile number", Toast.LENGTH_LONG).show()
        button.setOnClickListener{
            var otpx  = otp.text.toString()
            val credential = PhoneAuthProvider.getCredential(cotp!!, otpx)
            signInWithCredential(credential)

        }
        terms.setOnClickListener {
            startActivity(Intent(this,TermsActivity::class.java)
               )
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            );
        }

    }
    private fun signInWithCredential(credential: PhoneAuthCredential) {
        var cm=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(cm.activeNetworkInfo==null)
        {
            Toast.makeText(this@OtpActivity, "Please connect to Internet", Toast.LENGTH_LONG).show()
        }
        else {
             val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
            val pd = SimpleArcDialog(this)
            var ar = ArcConfiguration(this)
            ar.colors = colors
            pd.setConfiguration(ar)
            pd.setTitle("Verifying..")
            pd.setCancelable(false)
            pd.setCanceledOnTouchOutside(false)
            pd.show()
            var mAuth = FirebaseAuth.getInstance()
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {

                        /*val intent = Intent(this@SigninActivity, DetailsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)*/
                        var uid = FirebaseAuth.getInstance().uid
                        var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation")
                            .child(uid.toString())
                        dbase.addListenerForSingleValueEvent(
                            object : ValueEventListener
                            {
                                override fun onCancelled(p0: DatabaseError) {

                                }

                                @RequiresApi(Build.VERSION_CODES.O)
                                override fun onDataChange(p0: DataSnapshot) {
                                    if(p0.exists())
                                    {
                                        pd.dismiss()
                                        var i = Intent(this@OtpActivity, MainActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    }
                                    else
                                    {
                                        val formatter =
                                            SimpleDateFormat("dd.MM.yyyy, HH:mm")
                                        formatter.setLenient(false)
                                        var d =formatter.format(Date())
                                        pd.dismiss()
                                        dbase.child("fullname").setValue(intent.getStringExtra("fullname"))
                                        dbase.child("notificationtime").setValue(d.toString())
                                        dbase.child("mobile").setValue(intent.getStringExtra("mobile"))
                                        var i = Intent(this@OtpActivity, MainActivity::class.java)
                                        startActivity(i)
                                        finish()
                                    }
                                }

                            }
                        )



                    } else {
                        pd.dismiss()
                        Toast.makeText(this@OtpActivity, "invalid", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    override fun onStart() {
        super.onStart()
        var user = FirebaseAuth.getInstance().currentUser
        if(user!=null)
        {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}
