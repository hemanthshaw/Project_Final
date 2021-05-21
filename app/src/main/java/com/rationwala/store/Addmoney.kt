package com.rationwala.store

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_addmoney.*

@Suppress("DEPRECATION")
class Addmoney : AppCompatActivity() {
    val UPI_PAYMENT = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmoney)

        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
        }
        addmoney.setOnClickListener {

            payusingUpi(money.text.toString())
        }
    }

    private fun payusingUpi(money: String) {
        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("myinformation").child("walletupi")
        db.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        val uri: Uri = Uri.parse("upi://pay").buildUpon()
                            .appendQueryParameter("pa", p0.value.toString())
                            .appendQueryParameter("pn", "E.Neeraj")
                            .appendQueryParameter("tn", "for Wallet")
                            .appendQueryParameter("am", money)
                            .appendQueryParameter("cu", "INR")
                            .build()


                        val upiPayIntent = Intent(Intent.ACTION_VIEW)
                        upiPayIntent.data = uri

                        // will always show a dialog to user to choose an app

                        // will always show a dialog to user to choose an app
                        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

                        // check if intent resolves

                        // check if intent resolves
                        if (null != chooser.resolveActivity(packageManager)) {
                            startActivityForResult(chooser, UPI_PAYMENT)
                        } else {
                            Toast.makeText(
                                this@Addmoney,
                                "No UPI app found, please install one to continue",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(
                            this@Addmoney,
                            "Sorry,We are currently not accepting UPI Payments",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UPI_PAYMENT -> if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    //Log.d("UPI", "onActivityResult: $trxt")
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add(trxt!!)
                    upiPaymentDataOperation(dataList)
                } else {
                    //Log.d("UPI", "onActivityResult: " + "Return data is null")
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                /* Log.d(
                     "UPI",
                     "onActivityResult: " + "Return data is null"
                 ) //when user simply back without payment*/
                val dataList: ArrayList<String> = ArrayList()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(this@Addmoney)) {
            // Toast.makeText(this,data[0],Toast.LENGTH_SHORT).show()
            var str = data[0]
            //Log.d("UPIPAY", "upiPaymentDataOperation: $str")
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&".toRegex()).toTypedArray()
            for (i in response.indices) {
                val equalStr =
                    response[i].split("=".toRegex()).toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equalStr[1].toLowerCase()
                    } else if (equalStr[0]
                            .toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0]
                            .toLowerCase() == "txnRef".toLowerCase()
                    ) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }
            if (status == "success") {
                //Code to handle successful transaction here.
                addwallet(money.text.toString())
                Toast.makeText(this@Addmoney, "Money added successfully.", Toast.LENGTH_SHORT)
                    .show()
                //Log.d("UPI", "responseStr: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(this@Addmoney, "Payment cancelled by user.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this@Addmoney,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this@Addmoney,
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addwallet(money: String) {
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
                        var my = p0.value.toString().toInt()
                        db.setValue(my+money.toInt())
                        finish()
                    }
                    else
                    {
                        db.setValue(money)
                        finish()
                    }
                }

            }
        )
    }

    @SuppressLint("MissingPermission")
    fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected
                && netInfo.isConnectedOrConnecting
                && netInfo.isAvailable
            ) {
                return true
            }
        }
        return false
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right);
    }
}