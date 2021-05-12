package com.nick.wedding.picture

import android.os.Bundle
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demovm.support.textBanner.SpeedyLinearLayoutManager
import com.example.demovm.support.textBanner.StartSnapHelper
import com.nick.wedding.R
import com.nick.wedding.base.BaseActivity
import com.nick.wedding.base.BaseViewModel
import com.nick.wedding.databinding.ActivityPictureBinding
import com.nick.wedding.picture.adapter.AutoPicture
import com.nick.wedding.picture.adapter.PictureAdapter

class PictureActivity : BaseActivity() {

    override lateinit var viewModel: BaseViewModel

    override lateinit var binding: ActivityPictureBinding

    lateinit var androidViewModel: PictureViewModel

    val picList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture)
        androidViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        initData()
        initRecyclerView()
    }

    private fun initData() {

        picList.add(R.drawable.picbanner1)
        picList.add(R.drawable.picbanner2)
        picList.add(R.drawable.picbanner3)

    }

    private fun initRecyclerView() {
        val adapter = PictureAdapter(androidViewModel)
        binding.rvPicture.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPicture.adapter = adapter
        adapter.submit(picList)
//        SpeedyLinearLayoutManager
        binding.autoPicture.adapter = AutoPicture().apply {
            this.submit(picList)
        }
        binding.autoPicture.layoutManager = SpeedyLinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        StartSnapHelper().attachToRecyclerView(binding.autoPicture)
        binding.autoPicture.start()
    }
}