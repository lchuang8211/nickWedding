package com.nick.wedding.merryme.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.data.ExchangeRecord
import com.nick.wedding.databinding.ItemExchangeRecordBinding
import com.nick.wedding.merryme.MerryMeViewModel

class ExchangeRecordAdapter(val viewModel: MerryMeViewModel) : RecyclerView.Adapter<ExchangeRecordAdapter.exchangeRecordHolder>()  {

    var data = listOf<ExchangeRecord>()

    var myCall: ExchangeListener ?= null

    interface ExchangeListener {
        fun onFuctionListener(title: String, price: Int)
    }

    fun setListener(listener: ExchangeListener){
        this.myCall = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRecordAdapter.exchangeRecordHolder {
        //載入項目模板
        val inflater = LayoutInflater.from(parent.context)
        val example = ItemExchangeRecordBinding.inflate(inflater, parent, false)

        return exchangeRecordHolder(example)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ExchangeRecordAdapter.exchangeRecordHolder, position: Int) {
        holder.bind(data[position])
    }

    fun submit(data: List<ExchangeRecord>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class exchangeRecordHolder(val binding: ItemExchangeRecordBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: ExchangeRecord) {

            binding.tvSeq.text = "${item.seq}."
            binding.tvDate.text = item.date
            binding.tvTitle.text = "兌換\"${item.title}\""
            binding.tvPrice.text = "${item.price?:0} 個"

        }
    }
}
/**
 * 使用了 DiffUtil ，有興趣可以自行理解
 */
