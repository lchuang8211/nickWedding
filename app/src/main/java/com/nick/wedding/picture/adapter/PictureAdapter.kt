package com.nick.wedding.picture.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.R
import com.nick.wedding.databinding.ItemExchangeListBinding
import com.nick.wedding.databinding.ItemPictureListBinding
import com.nick.wedding.merryme.MerryMeViewModel
import com.nick.wedding.picture.PictureViewModel
import com.nick.wedding.surpport.ExchangeManager

class PictureAdapter(val viewModel: PictureViewModel) : RecyclerView.Adapter<PictureAdapter.pictureHolder>()  {

    var data = listOf<Int>()
    var page = 0
    var myCall: PictureListener ?= null

    interface PictureListener {
        fun onFuctionListener(title: String, price: Int)
    }

    fun setListener(listener: PictureListener){
        this.myCall = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureAdapter.pictureHolder {
        //載入項目模板
        val inflater = LayoutInflater.from(parent.context)
        val example = ItemPictureListBinding.inflate(inflater, parent, false)

        return pictureHolder(example)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PictureAdapter.pictureHolder, position: Int) {
        holder.bind(data[position], viewModel)
    }

    fun submit(data: List<Int>) {
        this.data = data
        notifyDataSetChanged()
    }


    inner class pictureHolder(val binding: ItemPictureListBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: Int, viewModel: PictureViewModel) {
            binding.ivPicture.setImageResource(item)
        }
    }
}
/**
 * 使用了 DiffUtil ，有興趣可以自行理解
 */
