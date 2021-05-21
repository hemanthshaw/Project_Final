package com.rationwala.store.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rationwala.store.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.contact_frag.view.*

@Suppress("DEPRECATION")
class Contactus : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.contact_frag, null)

        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(activity)
        var ar = ArcConfiguration(activity)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        pd.setTitle("Loading..")
        pd.show()

        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("myinformation")
        db.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    var chi = p0.children
                    var lati = ""
                    var longi = ""
                    chi.forEach {
                        when(it.key)
                        {
                            "whatsapp"->v.twh.text = it.value.toString()
                            "phone1"->v.mob1.text = it.value.toString()
                            "phone2"->v.mob2.text = it.value.toString()
                            "email"->v.temail.text = it.value.toString()
                            "address"->v.taddress.text = it.value.toString()
                            "latitude"->lati = it.value.toString()
                            "longitude"->longi =it.value.toString()

                        }
                    }
                    v.address.setOnClickListener {
                       /* startActivity(
                            Intent(activity,MapsActivity::class.java).putExtra("lati",lati).putExtra("longi",longi)
                            .putExtra("activity","contact"))*/

                    }
                    v.mob1.setOnClickListener {
                        var i = Intent(Intent.ACTION_DIAL)
                        i.data= Uri.parse("tel:"+v.mob1.text.toString())
                        activity!!.startActivity(i)


                    }
                    v.mob2.setOnClickListener {
                        var i = Intent(Intent.ACTION_DIAL)
                        i.data=Uri.parse("tel:"+v.mob2.text.toString())
                        activity!!.startActivity(i)
                    }

                    v.whatsapp.setOnClickListener {
                        val phoneNumber = v.twh.text.toString()
                        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
                        try {
                            //packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(url)
                            activity!!.startActivity(i)
                        } catch (e: PackageManager.NameNotFoundException) {
                            Toast.makeText(activity, "Whatsapp is not installed in your phone.", Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                    }

                    v.email.setOnClickListener {

                        val email = Intent(Intent.ACTION_SEND)
                        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(v.temail.text.toString()))
                        email.putExtra(Intent.EXTRA_SUBJECT, "")
                        email.putExtra(Intent.EXTRA_TEXT, "")

                        //need this to prompts email client only

                        //need this to prompts email client only
                        email.type = "message/rfc822"
                        activity!!.startActivity(Intent.createChooser(email, "Choose an Email client :"))
                    }

                    pd.dismiss()


                }

            }
        )

        return v
    }
}