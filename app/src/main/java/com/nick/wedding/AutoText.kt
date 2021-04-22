package com.nick.wedding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.databinding.TextBannerBinding

class AutoText() : ListAdapter<String, TextBannerViewHolder>(BannerDiffCallBack()) {

    var data = emptyList<String>()
    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
        // Int.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextBannerViewHolder {
        return TextBannerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TextBannerViewHolder, position: Int) {
        if (data.size > 0) {
            holder.bind(data[position % data.size])
        }
    }

    fun submit(list: ArrayList<String>) {
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

class TextBannerViewHolder constructor(var binding: TextBannerBinding) : RecyclerView.ViewHolder(binding.root){
    companion object{
        fun from(parent: ViewGroup): TextBannerViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TextBannerBinding.inflate(layoutInflater,parent,false)
            return TextBannerViewHolder(binding)
        }

    }
    fun bind(item: String){
        binding.tvBanner.text = item
    }
}