package com.rationwala.store.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rationwala.store.R
import com.rationwala.store.adapters.ItemsAdapter1
import com.rationwala.store.classes.CostClass
import com.rationwala.store.classes.ItemClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.items_frag.view.rview

@Suppress("DEPRECATION")
class TablayoutFrag : Fragment() {
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.items_frag, null)
        var lm= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        v.rview.layoutManager = lm
        //v.rview.isNestedScrollingEnabled=false
        var item = arguments!!.getString("item")
        var act = arguments!!.getString("activity")
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(activity)
        var ar = ArcConfiguration(activity)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        pd.setTitle("Loading..")
        pd.show()
            var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("homepage").child(item!!)
            var y = 0
            //Toast.makeText(activit y,item,Toast.LENGTH_LONG).show()
            dbase.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            var lis = mutableListOf<ItemClass>()
                            lis.clear()
                            var chi = p0.children
                            var last = p0.children.last().key.toString()
                            chi.forEach {

                                var x = it.key.toString().split(":")
                                var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("categories")
                                    .child(x[0]).child("subcategory")
                                    .child(x[1]).child(x[2])
                                db.addListenerForSingleValueEvent(
                                    object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {

                                        }

                                        override fun onDataChange(p1: DataSnapshot) {
                                            var chi1 = p1.children
                                            //starts
                                            var map = HashMap<String, String>()
                                            map.put("key", it.key.toString())
                                            var c = mutableListOf<CostClass>()
                                            chi1.forEach {
                                                when (it.key) {
                                                    "quantity" -> {
                                                        var chi2 = it.children
                                                        var map1 = HashMap<String, String>()
                                                        map1.put("available", "1")
                                                        chi2.forEach {
                                                            map1.put("key", it.key.toString())
                                                            var chi3 = it.children
                                                            chi3.forEach {
                                                                map1.put(
                                                                    it.key.toString(),
                                                                    it.value.toString()
                                                                )
                                                            }
                                                            var co = CostClass(
                                                                map1.getValue("quantity"),
                                                                map1.getValue("cost"),
                                                                map1.getValue("discount"),
                                                                map1.getValue("key"),
                                                                map1.getValue("available")
                                                            )
                                                            c.add(co)
                                                        }

                                                    }
                                                    else -> {
                                                        map.put(
                                                            it.key.toString(),
                                                            it.value.toString()
                                                        )

                                                    }
                                                }
                                            }
                                            var i = ItemClass(
                                                map.getValue("name"),
                                                map.getValue("image"),
                                                c,
                                                map.getValue("desc"),
                                                x[0],
                                                x[1],
                                                "1",
                                                map.getValue("key")
                                            )
                                            lis.add(i)
                                            if (lis.size != 0 && it.key.toString().equals(last)) {
                                                //Toast.makeText(activity,lis.size.toString(),Toast.LENGTH_LONG).show()
                                                //Toast.makeText(activity,lis[0].key,Toast.LENGTH_LONG).show()
                                                lis = lis.subList(0,4)
                                                    v.rview.adapter = ItemsAdapter1(activity!!, lis)
                                                pd.dismiss()

                                            }


                                            //ends

                                        }
                                    }
                                )

                            }

                        }
                        else
                        {
                            pd.dismiss()
                        }

                    }
                }
            )

        return v
    }
}