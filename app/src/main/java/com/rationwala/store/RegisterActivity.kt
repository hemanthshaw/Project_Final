package com.rationwala.store

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.core.util.Pair
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    lateinit var verificationid:String
    private lateinit var googleSignInClient: GoogleSignInClient

    var RC_SIGN_IN=12
    private lateinit var auth: FirebaseAuth

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        sign.setOnClickListener {
            //val email = Pair<View, String>(name, "otp")

            // val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity, email)
            var e = email.text.toString()
            var p = password.text.toString()
            var n = name.text.toString()
            var cm=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(n.isEmpty())
                name.setError("name!")
            else if(n.length>13)
                name.setError("name should be less than 13 characters!")
            else if (e.isEmpty())
                email.setError("Email!")
            else if (p.isEmpty())
                password.setError("Password!")
            else if (p.length < 8)
                password.setError("less than 8 digits")
            else if(!p.equals(cpass.text.toString()))
            {
                Toast.makeText(this@RegisterActivity,"password not matched",Toast.LENGTH_LONG).show()
            }
            else if(cm.activeNetworkInfo==null)
            {
                Toast.makeText(this@RegisterActivity,"Please connect to internet",Toast.LENGTH_LONG).show()

            }
            else {
                var auth = FirebaseAuth.getInstance()
                if(checkLong(e)) {
                    if (!e.startsWith("+91")) {
                        e = "+91" + e
                        sendVerificationCode(e)
                    }
                    else {
                        sendVerificationCode(e)

                    }
                }
                else{
                    var pd = ProgressDialog(this@RegisterActivity)
                    pd.setTitle("Creating Account..")
                    pd.show()
                    auth.createUserWithEmailAndPassword(e, p).addOnCompleteListener {
                        if (it.isSuccessful) {
                            pd.dismiss()
                            var uid = FirebaseAuth.getInstance().uid
                            var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString())
                            dbase.child("fullname").setValue(n)
                            dbase.child("email").setValue(e)
                            startActivity(Intent(this@RegisterActivity,MainActivity::class.java))
                            finish()


                        } else {
                            pd.dismiss()
                            Toast.makeText(this@RegisterActivity, "Used by other user", Toast.LENGTH_LONG)
                                .show()
                        }

                    }
                }
            }


        }
        /*google.setOnClickListener {
            signIn()
        }*/
        already.setOnClickListener {
            var i = Intent(this@RegisterActivity, LoginActivity::class.java)
            val email = Pair.create<View, String>(email, "email")
            val pass = Pair.create<View, String>(password, "otp")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity, email,pass)
            startActivity(i, options.toBundle())

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


    private fun sendVerificationCode(number: String) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallBack
        )
    }

    private val mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, forceResendingToken)
            verificationid = s!!
            var i = Intent(this@RegisterActivity, OtpActivity::class.java)
            i.putExtra("otp", verificationid)
            i.putExtra("email",email.text.toString())
            i.putExtra("password",password.text.toString())
            i.putExtra("name",name.text.toString())
            Toast.makeText(this@RegisterActivity, ""+verificationid, Toast.LENGTH_LONG)
                .show()
            startActivity(i)
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            //val code = phoneAuthCredential.smsCode

        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@RegisterActivity,"failed to send message", Toast.LENGTH_LONG).show()

        }
    }

}
