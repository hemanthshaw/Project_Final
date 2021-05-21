package com.rationwala.store.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.rationwala.store.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import com.rationwala.dukana.MyOrdersActivity
import kotlinx.android.synthetic.main.myaccount_frag.view.*

@Suppress("DEPRECATION")
class MyAccountfrag : Fragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.myaccount_frag, null)
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(activity)
        var ar = ArcConfiguration(activity)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setTitle("Loading..")
        pd.show()
       /* v.changepass.setOnClickListener {
                startActivity(Intent(activity, ChangePass::class.java))
        }
        v.addmobile.setOnClickListener {
            startActivity(Intent(activity, Addmobile::class.java))
        }
        v.change.setOnClickListener {
            startActivity(Intent(activity, MyAddress::class.java))
        }*/
        var user = FirebaseAuth.getInstance().currentUser

        if(user==null)
        {
            startActivity(Intent(activity,LoginActivity::class.java))
        }
        else {
            var uid = FirebaseAuth.getInstance().uid
            var dbase = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("usersinformation")
                .child(uid.toString())
            dbase.addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(p0: DataSnapshot) {
                        var chi = p0.children
                        chi.forEach {
                            when (it.key) {
                                "fullname" -> v.name.text = "Hi, " + it.value.toString()
                                "mobile" -> v.tv_address.text = it.value.toString()
                                "address" -> {
                                    var chi1 = it.children
                                    var map = HashMap<String,String>()
                                    map.put("mobile","xx-xx-xxx")
                                    chi1.forEach {
                                        map.put(it.key.toString(),it.value.toString())
                                    }
                                    /*v.address.text = "House No: "+map.getValue("address")+","+"Area: "+map.getValue("area")+","+
                                            "Landmark: "+map.getValue("landmark")+","+
                                            "Pincode: "+map.getValue("pincode")*/
                                    v.address.text = "Landmark: "+map.getValue("landmark")+","+
                                            "Pincode: "+map.getValue("pincode")


                                }
                            }
                        }
                        pd.dismiss()
                    }

                }
            )

        }
        v.change.setOnClickListener {
            startActivity(Intent(activity,MyAddress::class.java))
            requireActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }

        v.logout.setOnClickListener {
            if(user!=null) {
                var alert= AlertDialog.Builder(activity)
                alert.setMessage("Do you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("yes",
                        DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                            FirebaseAuth.getInstance().signOut()
                            Toast.makeText(activity,"You have logged out ", Toast.LENGTH_LONG).show()
                          startActivity(Intent(activity,LoginActivity::class.java))
                            requireActivity().overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);
                        })
                    .setNegativeButton("no",
                        DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
                        dialogInterface.cancel()
                    })
                var al = alert.create()
                al.show()
            }
            else
                Toast.makeText(activity,"no user is logged in ", Toast.LENGTH_LONG).show()
        }
        v.cart.setOnClickListener {
            startActivity(Intent(activity,CartActivity::class.java).putExtra("ac","myprofile"))
            requireActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

        }
        v.wallet.setOnClickListener {
            startActivity(Intent(activity,MyWallet::class.java).putExtra("title","My Wallet"))
            requireActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        }
        v.rewards.setOnClickListener {
            startActivity(Intent(activity,MyWallet::class.java).putExtra("title","My Rewards"))
            requireActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        }
        v.orders.setOnClickListener {
            val trending_fragment = MyOrdersActivity()
            val manager = parentFragmentManager
            //                FragmentManager m = getSu();
            val fragmentTransaction =
                manager.beginTransaction()
            fragmentTransaction.replace(R.id.contentPanel, trending_fragment).addToBackStack(null)
            fragmentTransaction.commit()
        }
            return v
        }

    }