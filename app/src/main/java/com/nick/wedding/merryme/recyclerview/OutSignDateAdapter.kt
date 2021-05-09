package com.nick.wedding.merryme.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.data.DateSign
import com.nick.wedding.databinding.ItemCalenderOutBinding
import com.nick.wedding.merryme.MerryMeViewModel
import com.nick.wedding.surpport.DatePictureHelper
import timber.log.Timber

class OutSignDateAdapter(val viewModel: MerryMeViewModel): RecyclerView.Adapter<outHolder>() {


    var monthSize = listOf<Pair<Int, Int>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): outHolder {
        //載入項目模板
        val inflater = LayoutInflater.from(parent.context)
        val example = ItemCalenderOutBinding.inflate(inflater, parent, false)

        return outHolder(example)
    }

    override fun getItemCount(): Int = monthSize.size

    override fun onBindViewHolder(holder: outHolder, position: Int) {
        holder.bind(monthSize[position].first, monthSize[position].second, viewModel )
    }

    fun submit(monthSize: List<Pair<Int, Int>>){
        this.monthSize = monthSize
        notifyDataSetChanged()
    }

}

class outHolder(val binding: ItemCalenderOutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(year: Int, month: Int, viewModel: MerryMeViewModel){

//        binding.item = item
        val data = viewModel.getHoldMonth(year, month)
        Timber.tag("hlcDebug").d("data : $year/$month -> $data")

        binding.tvYear.text = " $year 年"
        binding.tvMonth.text = " $month 月"
        binding.rvDate.apply {
            this.adapter = SignDateAdapter(viewModel).apply {
                this.submit(data)
            }
            this.layoutManager = GridLayoutManager(context,7)
        }

//        if (viewModel.selectCurrentSign(year, month) > 0)
            binding.tvCurrentCount.text = "本月 ${viewModel.selectCurrentSign(year, month)} 次"
//        else
//            binding.tvCurrentCount.visibility = View.GONE

//        if (item.date > 0)
//            binding.ivCaledarDate.setImageResource(DatePictureHelper.DataChoice.values()[item.date-1].value)
//        else {
//            binding.ivCaledarDate.visibility = View.GONE
//            binding.ivSignOK.visibility = View.GONE
//        }
//
//        if (item.signed)
//            binding.ivSignOK.visibility = View.VISIBLE
//        else
//            binding.ivSignOK.visibility = View.GONE


    }
}

/**
 * 使用了 DiffUtil ，有興趣可以自行理解
 */
