package com.rationwala.store.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rationwala.dukana.classes.NotificationClass
import com.rationwala.store.LoginActivity
import com.rationwala.store.R
import com.rationwala.store.adapters.NotificationAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.notifications_frag.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotificationFrag : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.notifications_frag, null)

        var hm= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        v.rview.layoutManager = hm

        var uid= FirebaseAuth.getInstance().uid
        v.clear.setOnClickListener {
            var alert= AlertDialog.Builder(activity)
            alert.setMessage("Do you want to clear all Notifications?")
                .setCancelable(false)
                .setPositiveButton("yes",
                    DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation").child(uid.toString()).child("notificationtime")
                        val formatter =
                            SimpleDateFormat("dd.MM.yyyy, HH:mm")
                        formatter.setLenient(false)
                        val curDate = Date()
                        val curTime: String = formatter.format(curDate)
                        db.setValue(curTime)
                        v.rview.visibility = View.GONE
                        v.nonoti.visibility = View.VISIBLE
                        v.clear.visibility = View.GONE
                    })
                .setNegativeButton("no",
                    DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                })
            var al = alert.create()
            al.show()

        }
        var user = FirebaseAuth.getInstance().currentUser
        if(user==null)
        {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
        else {
            var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation")
                .child(uid.toString()).child("notificationtime")
            db.addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            var x = p0.value.toString()
                            val formatter =
                                SimpleDateFormat("dd.MM.yyyy, HH:mm")
                            formatter.setLenient(false)
                            val cdate = formatter.parse(x)!!
                            val ctime = cdate.time
                            notify(ctime, v)
                        } else {
                            v.clear.visibility = View.GONE
                            v.nonoti.visibility = View.VISIBLE
                        }
                    }

                }
            )
        }
        return v
    }

    private fun notify(x: Long,v:View) {
        var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("notifications")
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(activity)
        var ar = ArcConfiguration(activity)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setTitle("Loading..")
        pd.show()
        dbase.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        var chi = p0.children
                        var lis = mutableListOf<NotificationClass>()
                        chi.forEach {
                            var map = HashMap<String,String>()
                            var chi1=it.children
                            map.put("pos","0")
                            chi1.forEach {
                                map.put(it.key.toString(),it.value.toString())
                            }
                            var time=map.getValue("time")
                            val formatter =
                                SimpleDateFormat("dd.MM.yyyy, HH:mm")
                            formatter.setLenient(false)
                            val cdate = formatter.parse(map.getValue("time"))
                            val ctime = cdate.time
                            if(ctime>x)
                            {
                                var n = NotificationClass(map.getValue("title"),map.getValue("body"),map.getValue("img"),map.getValue("time")
                                    ,map.getValue("category"),map.getValue("subcategory"),map.getValue("itemkey"),map.getValue("pos"))
                                lis.add(n)
                            }


                        }
                        if(lis.size>0)
                        {
                            lis.reverse()
                            v.rview.adapter = NotificationAdapter(activity!!,lis)
                            v.clear.visibility=View.VISIBLE
                            v.rview.visibility = View.VISIBLE
                            v.nonoti.visibility = View.GONE
                            //spin_kit.visibility=View.GONE
                        }
                        else
                        {
                            v.clear.visibility = View.GONE
                            // spin_kit.visibility = View.GONE
                            v.rview.visibility = View.GONE
                            v.nonoti.visibility = View.VISIBLE
                        }
                        pd.dismiss()
                    }
                    else{
                        pd.dismiss()
                        v.clear.visibility = View.GONE
                        v.rview.visibility = View.GONE
                        v.nonoti.visibility = View.VISIBLE

                    }


                }


            }
        )
    }

}