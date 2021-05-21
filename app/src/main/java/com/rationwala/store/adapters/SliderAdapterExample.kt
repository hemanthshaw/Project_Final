package com.rationwala.store.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.rationwala.store.R
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.image_layout.view.*


class SliderAdapterExample(var ac: Context, var lis:MutableList<String>) : SliderViewAdapter<SliderAdapterExample.SliderAdapterVH>() {

    // var mSliderItems = mutableListOf<SliderItem>()


    /* fun renewItems(sliderItems: MutableList<SliderItem>) {
         this.mSliderItems = sliderItems
         notifyDataSetChanged()
     }


     fun deleteItem(position: Int) {
         this.mSliderItems.removeAt(position)
         notifyDataSetChanged()
     }


     fun addItem(sliderItem: SliderItem) {
         this.mSliderItems.add(sliderItem)
         notifyDataSetChanged()
     }*/


    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterVH {
        val inflate = LayoutInflater.from(ac).inflate(R.layout.image_layout, null)
        var myholder = SliderAdapterVH(inflate)
        return myholder

    }

    override fun getCount(): Int {
        return lis.size
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {

        Glide.with(ac).load(lis[position]).into(viewHolder!!.imageGifContainer)


    }


    inner class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {


        var imageGifContainer: ImageView



        init {

            imageGifContainer = itemView.banner_image


        }

    }

}