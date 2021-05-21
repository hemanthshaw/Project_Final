package com.rationwala.store


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
   var pd: SimpleArcDialog ? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var verificationid:String
    private lateinit var auth: FirebaseAuth
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        terms.setOnClickListener {
            startActivity(Intent(this,TermsActivity::class.java)
                )
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            );
        }

        login.setOnClickListener {

            var cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var e = mobile.text.toString()
            if(fullname.text.toString().isEmpty())
            {
                Toast.makeText(this@LoginActivity, "Please enter fullname", Toast.LENGTH_LONG)
                    .show()
            }
            else if(e.isEmpty())
             {
                 Toast.makeText(this@LoginActivity, "Please enter mobile number", Toast.LENGTH_LONG)
                     .show()
             }
            else if(cm.activeNetworkInfo == null) {
                Toast.makeText(this@LoginActivity, "Please connect to internet", Toast.LENGTH_LONG)
                    .show()

            } else {
                val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
                var ar = ArcConfiguration(this)
                ar.colors = colors
                pd = SimpleArcDialog(this)
                pd!!.setConfiguration(ar)
                pd!!.setTitle("Loading..")
                pd!!.setCanceledOnTouchOutside(false)
                pd!!.setCancelable(false)
                pd!!.show()
                sendVerificationCode("+91"+e)

            }
        }

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
            Toast.makeText(this@LoginActivity,"OTP sent to your mobile number", Toast.LENGTH_LONG).show()
            var i = Intent(this@LoginActivity, OtpActivity::class.java)
            i.putExtra("otp", verificationid)
            i.putExtra("fullname",fullname.text.toString())
            i.putExtra("mobile",mobile.text.toString())
            startActivity(i)
            pd!!.dismiss()
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            //val code = phoneAuthCredential.smsCode

        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@LoginActivity,"failed to send message", Toast.LENGTH_LONG).show()
           pd!!.dismiss()
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
