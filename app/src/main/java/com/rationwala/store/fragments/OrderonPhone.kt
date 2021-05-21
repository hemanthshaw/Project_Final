package com.rationwala.store.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rationwala.store.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.orderonph_frag.view.*

@Suppress("DEPRECATION")
class OrderonPhone : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.orderonph_frag, null)
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(activity)
        var ar = ArcConfiguration(activity)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        pd.setTitle("Loading..")
        pd.show()
        var uid = FirebaseAuth.getInstance().uid
        var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("myinformation")
        dbase.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var chi = p0.children
                    chi.forEach{
                        when(it.key)
                        {
                            "mobile1"->v.mobile.text = it.value.toString()
                        }
                    }

                    v.card.setOnClickListener{
                        var i = Intent(Intent.ACTION_DIAL)
                        i.data= Uri.parse("tel:"+v.mobile.text.toString())
                        startActivity(i)
                    }
                    pd.dismiss()
                }

            }
        )
        return v
    }
}