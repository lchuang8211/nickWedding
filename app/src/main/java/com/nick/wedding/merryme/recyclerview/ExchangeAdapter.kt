package com.nick.wedding.merryme.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.databinding.ItemExchangeListBinding
import com.nick.wedding.merryme.MerryMeViewModel
import com.nick.wedding.surpport.ExchangeManager

class ExchangeAdapter(val viewModel: MerryMeViewModel) : RecyclerView.Adapter<ExchangeAdapter.exchangeHolder>()  {


    var data = listOf<String>()
    var page = 0
    var myCall: ExchangeListener ?= null

    interface ExchangeListener {
        fun onFuctionListener(title: String, price: Int)
    }

    fun setListener(listener: ExchangeListener){
        this.myCall = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeAdapter.exchangeHolder {
        //載入項目模板
        val inflater = LayoutInflater.from(parent.context)
        val example = ItemExchangeListBinding.inflate(inflater, parent, false)

        return exchangeHolder(example)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ExchangeAdapter.exchangeHolder, position: Int) {
        holder.bind(data[position], page, viewModel)
    }

    fun submit(data: List<String>, page: Int) {
        this.data = data
        this.page = page
        notifyDataSetChanged()
    }


    inner class exchangeHolder(val binding: ItemExchangeListBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: String, page: Int, viewModel: MerryMeViewModel) {

            val pair = ExchangeManager.getExchangeList(item, page)
            binding.tvTitle.text = pair.first
            binding.tvPrice.text = "${pair.second} 個"


            binding.llItem.setOnClickListener {
                myCall?.onFuctionListener(pair.first, pair.second)
            }

        }
    }
}
/**
 * 使用了 DiffUtil ，有興趣可以自行理解
 */
