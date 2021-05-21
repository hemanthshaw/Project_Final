package com.rationwala.store.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rationwala.store.R
import com.rationwala.store.TabInterface
import com.rationwala.store.TopsellingActivity
import com.rationwala.store.adapters.SliderAdapterExample
import com.rationwala.store.adapters.TopcatAdapter
import com.rationwala.store.adapters.viewpager
import com.rationwala.store.classes.TopCatclass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.leo.simplearcloader.ArcConfiguration
import com.leo.simplearcloader.SimpleArcDialog
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.home_frag.view.*

@Suppress("DEPRECATION")
class HomeFragment : Fragment(),TabInterface {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.home_frag, null)
        var lm = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        v.hrview.layoutManager = lm
        v.ll3.setOnClickListener(View.OnClickListener {
            val trending_fragment = SearchFrag()
            val manager = parentFragmentManager
            //                FragmentManager m = getSu();
            val fragmentTransaction =
                manager.beginTransaction()
            fragmentTransaction.replace(R.id.contentPanel, trending_fragment).addToBackStack(null)
            fragmentTransaction.commit()
        })
        val colors = intArrayOf(R.color.gradStart,R.color.gradEnd)
        val pd = SimpleArcDialog(activity)
        var ar = ArcConfiguration(activity)
        ar.colors = colors
        pd.setConfiguration(ar)
        pd.setCanceledOnTouchOutside(false)
        pd.setCancelable(false)
        pd.setTitle("Loading..")
        //pd.show()

        var sliderView = v.recycler_image_slider
        //sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();


        var banner = FirebaseDatabase.getInstance("https://rationwala-d207b-default-rtdb.firebaseio.com/").getReference("homepage").child("banner")
        banner.addValueEventListener(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists())
                    {
                        var chi = p0.children
                        var lis = mutableListOf<String>()
                        chi.forEach {
                            lis.add(it.value.toString())
                        }
                        v.recycler_image_slider.setSliderAdapter(SliderAdapterExample(activity!!,lis))
                        pd.dismiss()
                    }
                }

            }
        )

        var rv_items = v.rv_items
        // home_list.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.HORIZONTAL, false));
        val gridLayoutManager = GridLayoutManager(activity, 3)
        rv_items.layoutManager = gridLayoutManager
        rv_items.itemAnimator = DefaultItemAnimator()
        rv_items.isNestedScrollingEnabled = false
        var topcat =FirebaseDatabase.getInstance().getReference("topcategories")
        topcat.addListenerForSingleValueEvent(
            object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        var chi = p0.children
                        var map = HashMap<String, String>()
                        var lis = mutableListOf<TopCatclass>()
                        chi.forEach {
                            var chi1 = it.children
                            chi1.forEach {
                                map.put(it.key.toString(), it.value.toString())
                            }
                            var t = TopCatclass(
                                map.getValue("category"),
                                map.getValue("subcategory"),
                                map.getValue("image"),map.getValue("pos")
                            )
                            lis.add(t)
                        }
                        rv_items.adapter = TopcatAdapter(activity!!, lis)
                        pd.dismiss()

                    }
                    else{
                        pd.dismiss()
                    }
                }
            }
        )

        var lis = mutableListOf<String>()
        lis.add("TOP SELLING")
        lis.add("RECENT SELLING")
        lis.add("DEALS OF THE DAY")
        lis.add("WHAT'S NEW")
        v.hrview.adapter = viewpager(activity,lis,this@HomeFragment)
        var bundle = Bundle()
        bundle.putString("item","TOP SELLING")
        bundle.putString("activity","frag")
        var im = TablayoutFrag()
        im.arguments = bundle
        var frag = childFragmentManager
        var tx = frag.beginTransaction()
        tx.add(R.id.frag2,im)
        tx.commit()
        v.viewall.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    TopsellingActivity::class.java
                ))
            requireActivity().overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        }
        return v
    }

    override fun changefrag(x: String) {
        var bundle = Bundle()
        bundle.putString("item",x)
        bundle.putString("activity","frag")
        var im = TablayoutFrag()
        im.arguments = bundle
        var frag = childFragmentManager
        var tx = frag.beginTransaction()
        tx.add(R.id.frag2,im)
        tx.commit()
    }


}