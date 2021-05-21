package com.rationwala.dukana

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rationwala.store.LoginActivity
import com.rationwala.store.R
import com.rationwala.store.adapters.OrderAdapter
import com.rationwala.store.classes.OrderClass
import com.rationwala.store.fragments.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.my_orders.*
import kotlinx.android.synthetic.main.my_orders.view.l1
import kotlinx.android.synthetic.main.my_orders.view.rview
import kotlinx.android.synthetic.main.my_orders.view.start


@Suppress("DEPRECATION")
class MyOrdersActivity : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.my_orders, null)
        var lm= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        v.rview.layoutManager = lm

        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(activity)
        var ar = ArcConfiguration(activity)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        pd.setTitle("Loading..")
        pd.show()
        v.start.setOnClickListener {
            val trending_fragment = HomeFragment()
            val manager = parentFragmentManager
            //                FragmentManager m = getSu();
            val fragmentTransaction =
                manager.beginTransaction()
            fragmentTransaction.replace(R.id.contentPanel, trending_fragment)
            fragmentTransaction.commit()
        }
        var user = FirebaseAuth.getInstance().currentUser
        if(user==null)
        {
            startActivity(Intent(activity,LoginActivity::class.java))
        }
        else {
            var uid = FirebaseAuth.getInstance().uid
            var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation")
                .child(uid.toString()).child("orders")
            db.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            l1.visibility = View.GONE
                            var lis = mutableListOf<OrderClass>()
                            var chi = p0.children
                            chi.forEach {
                                var map = HashMap<String, String>()
                                var orderid = it.key.toString()
                                map.put("orderid", orderid)
                                map.put("deliverytime","0")
                                var chi1 = it.children
                                chi1.forEach {
                                    when (it.key) {
                                        "details" -> {


                                        }
                                        else -> map.put(it.key.toString(), it.value.toString())
                                    }
                                }
                                var o = OrderClass(
                                    map.getValue("orderid"),
                                    map.getValue("amount"),
                                    map.getValue("datetime"),
                                    map.getValue("type"),
                                    map.getValue("couponid"),
                                    map.getValue("address"),
                                    map.getValue("status"),
                                    map.getValue("confirmed"),
                                    map.getValue("readyforpickup"),
                                    map.getValue("delivered"),
                                    map.getValue("deliverytime")
                                )
                                lis.add(o)
                            }
                            //Toast.makeText(activity,""+lis[0].orderid,Toast.LENGTH_SHORT).show()
                            if (lis.size != 0) {
                                lis.reverse()
                                //Toast.makeText(activity,""+lis[0].confirmed+lis[0].delivered,Toast.LENGTH_SHORT).show()
                                v.rview.adapter = OrderAdapter(activity!!, lis, lis.size.toString())
                                //spin_kit.visibility = View.GONE
                                v.rview.visibility = View.VISIBLE
                                v.l1.visibility = View.GONE
                                pd.dismiss()
                            }

                        } else {
                            v.l1.visibility = View.VISIBLE
                            v.rview.visibility = View.GONE
                            v.l1.visibility = View.VISIBLE
                            pd.dismiss()
                            //spin_kit.visibility = View.GONE
                        }
                        //spin_kit.visibility =View.GONE
                    }

                }
            )
        }
        return  v
    }

}