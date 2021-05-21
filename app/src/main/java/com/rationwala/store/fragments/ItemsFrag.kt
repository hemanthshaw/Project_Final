package com.rationwala.store.fragments


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rationwala.store.R
import com.rationwala.store.adapters.ItemsAdapter
import com.rationwala.store.classes.CostClass
import com.rationwala.store.classes.ItemClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.items_frag.view.*


@Suppress("DEPRECATION")
class ItemsFrag : Fragment() {
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.items_frag, null)
        var lm= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        v.rview.layoutManager = lm
        var cato = arguments!!.getString("cato")
        var subcat = arguments!!.getString("subcat")

        nonhome(cato!!,subcat!!,v)


        return v
    }


    private fun nonhome(cato:String,subcat:String,v:View)
    {
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(activity)
        var ar = ArcConfiguration(activity)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        pd.setTitle("Loading..")
        pd.show()
        var db = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("categories").child(cato).
        child("subcategory").child(subcat)

        db.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        var chi = p0.children
                        var lis = mutableListOf<ItemClass>()
                        chi.forEach {
                            when(it.key){
                                "image"->{

                                }
                                else->
                                {
                                    var chi1 = it.children
                                    var map = HashMap<String,String>()
                                    map.put("key",it.key.toString())
                                    var c = mutableListOf<CostClass>()
                                    chi1.forEach {
                                        when(it.key)
                                        {
                                            "quantity"->{
                                                var chi2 = it.children
                                                var map1 = HashMap<String,String>()
                                                map1.put("available","1")
                                                chi2.forEach {
                                                    map1.put("key",it.key.toString())
                                                    var chi3 = it.children
                                                    chi3.forEach {
                                                        map1.put(it.key.toString(),it.value.toString())
                                                    }
                                                    var co = CostClass(map1.getValue("quantity"),map1.getValue("cost"),map1.getValue("discount"),
                                                        map1.getValue("key"),map1.getValue("available"))
                                                    c.add(co)
                                                }

                                            }
                                            else->{
                                                map.put(it.key.toString(),it.value.toString())

                                            }
                                        }
                                    }
                                    var i = ItemClass(map.getValue("name"),map.getValue("image"),c,map.getValue("desc"),
                                        cato,subcat,"1",map.getValue("key"))
                                    lis.add(i)
                                }
                            }
                        }
                        v.rview.adapter = ItemsAdapter(activity!!,lis)
                        v.rview.visibility = View.VISIBLE
                        //v.spin_kit.visibility = View.GONE
                        pd.dismiss()
                    }
                    else{
                        pd.dismiss()
                    }
                }

            }
        )
    }
}