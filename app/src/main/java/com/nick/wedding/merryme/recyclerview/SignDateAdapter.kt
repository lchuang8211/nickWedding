package com.nick.wedding.merryme.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.R
import com.nick.wedding.data.DateSign
import com.nick.wedding.databinding.ItemCalendarDateBinding
import com.nick.wedding.merryme.MerryMeViewModel
import kotlinx.android.synthetic.main.item_calendar_date.view.*
import okhttp3.internal.concurrent.Task
import timber.log.Timber
import java.util.*

class SignDateAdapter(val viewModel: MerryMeViewModel): RecyclerView.Adapter<myHolder>() {

    var data = listOf<DateSign>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        //載入項目模板
        val inflater = LayoutInflater.from(parent.context)
        val example = ItemCalendarDateBinding.inflate(inflater, parent, false)

        return myHolder(example)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: myHolder, position: Int) {
        holder.bind(data[position])
    }

    fun submit(data: List<DateSign>){
        this.data = data
        Timber.tag("hlcDebug").d(" size : ${data.size}")
        notifyDataSetChanged()
    }
}

class myHolder(val binding: ItemCalendarDateBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DateSign){
        Timber.tag("hlcDebug").d(" item : ${item.signed}")
        binding.item = item
        binding.ivCaledarDate.setImageResource(item.picture)
        if (item.signed)
            binding.ivSignOK.visibility = View.VISIBLE
        else
            binding.ivSignOK.visibility = View.GONE


    }
}

/**
 * 使用了 DiffUtil ，有興趣可以自行理解
 */
