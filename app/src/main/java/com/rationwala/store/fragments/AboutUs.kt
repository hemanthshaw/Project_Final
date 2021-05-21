package com.rationwala.store.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.rationwala.store.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.aboutus_frag.view.*

@Suppress("DEPRECATION")
class AboutUs : Fragment() {

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.aboutus_frag, null)

        var title = arguments!!.getString("title")
        var tv1 = v.tv1
        var wview = v.wview
        if(title.equals("about"))
        {
            tv1.text = "About US"
        }
        else if(title.equals("terms")){
            tv1.text = "Terms and Conditions"
        }
        else if(title.equals("privacy"))
        {
            tv1.text = "Privacy Policy"
        }

        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("myinformation")
        db.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var chi = p0.children
                    chi.forEach {
                        when (it.key) {
                            title->wview.loadUrl(it.value.toString())

                        }
                    }
                }
            })
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(activity)
        var ar = ArcConfiguration(activity)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setTitle("Loading..")
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        pd.show()
        wview.webViewClient = object: WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                pd.dismiss()
            }
        }


        wview.settings.javaScriptEnabled = true
        wview.settings.builtInZoomControls = true
        return v
    }
}