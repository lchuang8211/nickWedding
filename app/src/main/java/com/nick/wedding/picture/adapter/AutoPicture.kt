package com.nick.wedding.picture.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.databinding.ItemPictureListBinding
import com.nick.wedding.databinding.TextBannerBinding

class AutoPicture() : ListAdapter<String, PictureBannerViewHolder>(BannerDiffCallBack()) {

    var data = listOf<Int>()
    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
        // Int.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureBannerViewHolder {
        return PictureBannerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PictureBannerViewHolder, position: Int) {
        if (data.size > 0) {
            holder.bind(data[position % data.size])
        }
    }

    fun submit(list: List<Int>) {
        this.data = list
        notifyDataSetChanged()
    }
}


class BannerDiffCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

class PictureBannerViewHolder constructor(var binding: ItemPictureListBinding) : RecyclerView.ViewHolder(binding.root){
    companion object{
        fun from(parent: ViewGroup): PictureBannerViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemPictureListBinding.inflate(layoutInflater,parent,false)
            return PictureBannerViewHolder(binding)
        }

    }
    fun bind(item: Int){
        binding.ivPicture.setImageResource(item)
    }
}