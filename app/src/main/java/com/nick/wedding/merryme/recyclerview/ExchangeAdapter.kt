package com.nick.wedding.merryme.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.R
import com.nick.wedding.data.ExchangeItem
import com.nick.wedding.databinding.ItemExchangeListBinding
import com.nick.wedding.merryme.MerryMeViewModel
import com.nick.wedding.surpport.ToastUntil

class ExchangeAdapter(val viewModel: MerryMeViewModel) : RecyclerView.Adapter<ExchangeAdapter.exchangeHolder>()  {


    var data = listOf<ExchangeItem>()
    var page = 0
    var myCall: ExchangeListener ?= null
    var settingStatus = false

    interface ExchangeListener {
        fun onFuctionListener(title: String, oldTitle: String, price: Int, sql: Boolean, position: Int, page: Int)
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
        holder.bind(data[position], page, position, viewModel)
    }

    fun submit(data: List<ExchangeItem>, page: Int) {
        this.data = data
        this.page = page
        notifyDataSetChanged()
    }

    fun submitSetting(boolean: Boolean){
        this.settingStatus = boolean
        notifyDataSetChanged()
    }


    inner class exchangeHolder(val binding: ItemExchangeListBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: ExchangeItem, page: Int, position:Int, viewModel: MerryMeViewModel) {

            if (settingStatus)
                binding.ivNode.setImageResource(R.drawable.icon_pen)
            else {
                changeView(false)
                binding.ivNode.setImageResource(R.drawable.icon_node)
            }

//            val pair = ExchangeManager.getExchangeList(item, page)
            binding.tvTitle.text = item.title
            binding.tvPrice.text = "${item.price} 個"


            binding.llItem.setOnClickListener {
                if (item.visibility==false && !settingStatus) return@setOnClickListener
                if (settingStatus){
                    changeView(true)
                } else
                    myCall?.onFuctionListener(item.title, "", item.price, settingStatus, position+1, page)
            }

            binding.tvConfirm.setOnClickListener {
                if (binding.etTitle.text.toString().equals("")) {
                    ToastUntil("項目不得為空")
                    return@setOnClickListener
                }
                if (binding.etPrice.text.toString().equals("")) {
                    ToastUntil("數量不得為空")
                    return@setOnClickListener
                }
                val oldTitle = binding.tvTitle.text.toString()
                val newTitle = binding.etTitle.text.toString()
                val newPrice = binding.etPrice.text.toString().toInt()
                binding.tvTitle.text = newTitle
                binding.tvPrice.text = "$newPrice 個"
                changeView(false)

                myCall?.onFuctionListener(newTitle, oldTitle, newPrice, settingStatus, position+1, page)
            }

            binding.tvCancel.setOnClickListener {
                changeView(false)
            }

        }

        fun changeView(edit:Boolean){
            if (edit){
                binding.tvTitle.visibility = View.GONE
                binding.tvPrice.visibility = View.GONE
                binding.etTitle.visibility = View.VISIBLE
                binding.etPrice.visibility = View.VISIBLE
                binding.llSetting.visibility = View.VISIBLE
            } else{
                binding.tvTitle.visibility = View.VISIBLE
                binding.tvPrice.visibility = View.VISIBLE
                binding.etTitle.visibility = View.GONE
                binding.etPrice.visibility = View.GONE
                binding.llSetting.visibility = View.GONE
            }
        }
    }

}
/**
 * 使用了 DiffUtil ，有興趣可以自行理解
 */
