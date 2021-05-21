package com.rationwala.store.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rationwala.store.R
import com.rationwala.store.adapters.CategoryAdapter
import com.rationwala.store.classes.CategoryClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import kotlinx.android.synthetic.main.categories_frag.view.*

@Suppress("DEPRECATION")
class CategoriesFrag : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.categories_frag, null)
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
        var grid = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("categories")
        grid.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        var chi = p0.children
                        var lis = mutableListOf<CategoryClass>()
                        chi.forEach {
                            var list = mutableListOf<String>()
                            var chi1 = it.children
                            var map = HashMap<String,String>()
                            map.put("name",it.key.toString())
                            chi1.forEach {
                                when(it.key){
                                    "image"-> {
                                        map.put(it.key.toString(), it.value.toString())
                                    }
                                    "subcategory"->{
                                        var chi2 = it.children
                                        chi2.forEach {
                                            when(it.key){
                                                "image"->{}
                                                else->{
                                                    list.add(it.key.toString())
                                                }
                                        }

                                        }
                                    }
                                }
                            }
                            var l = CategoryClass(map.getValue("name"),map.getValue("image"),list)
                            lis.add(l)
                        }
                        //Toast.makeText(activity,""+lis.size,Toast.LENGTH_LONG).show()
                        v.rview.adapter = CategoryAdapter(activity!!,lis)
                        pd.dismiss()
                        /*v.rview.onItemClickListener = object : AdapterView.OnItemClickListener{
                            override fun onItemClick(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                //p1!!.setBackgroundColor(Color.WHITE)
                               /* var db = FirebaseDatabase.getInstance().getReference("categories").child(lis[p2].name).child("subcategory").child("no subcategory")
                                db.addListenerForSingleValueEvent(
                                    object : ValueEventListener
                                    {
                                        override fun onCancelled(p0: DatabaseError) {

                                        }

                                        override fun onDataChange(p0: DataSnapshot) {
                                            if(!p0.exists())
                                            {
                                                startActivity(Intent(activity,SubcatActivity::class.java).putExtra("cato",lis[p2].name))
                                            }
                                            else
                                            {
                                                startActivity(
                                                    Intent(activity,ItemsActivity::class.java).putExtra("cato",lis[p2].name)
                                                    .putExtra("activity","home").putExtra("subcato","no subcategory"))

                                            }
                                        }

                                    }
                                )*/
                                //startActivity(Intent(activity,SubcatActivity::class.java).putExtra("cato",lis[p2].name))

                                //Toast.makeText(activity,lis[p2].name,Toast.LENGTH_LONG).show()
                            }

                        }*/
                    }
                }

            }
        )


        return v
    }
}