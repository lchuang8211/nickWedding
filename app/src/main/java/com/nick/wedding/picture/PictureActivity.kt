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
import com.nick.wedding.surpport.WuBaiMediaPlayer
import timber.log.Timber

class PictureActivity : BaseActivity() {

    override lateinit var viewModel: BaseViewModel

    override lateinit var binding: ActivityPictureBinding

    lateinit var androidViewModel: PictureViewModel

    val picList = mutableListOf<Int>()

    init {
        WuBaiMediaPlayer.startMediaPlayer()
    }

    companion object{

    }

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
        picList.add(R.drawable.picbanner4)
        picList.add(R.drawable.picbanner5)
        picList.add(R.drawable.picbanner6)
        picList.add(R.drawable.picbanner7)
        picList.add(R.drawable.picbanner8)
        picList.add(R.drawable.picbanner_m1)
        picList.add(R.drawable.picbanner_m2)
        picList.add(R.drawable.picbanner_m3)
        picList.add(R.drawable.picbanner_m4)
        picList.add(R.drawable.picbanner_m5)
        picList.add(R.drawable.picbanner_m6)
        picList.add(R.drawable.picbanner_m7)
        picList.add(R.drawable.picbanner_m8)
        picList.add(R.drawable.picbanner_m9)
        picList.add(R.drawable.picbanner_m10)
        picList.add(R.drawable.picbanner_m11)
        picList.add(R.drawable.picbanner_m12)
        picList.add(R.drawable.picbanner_m13)
        picList.add(R.drawable.picbanner_m14)
        picList.add(R.drawable.picbanner_m15)
        picList.add(R.drawable.picbanner_m16)
        picList.add(R.drawable.picbanner_m17)
        picList.add(R.drawable.picbanner_m18)

    }

    private fun initRecyclerView() {
        val adapter = PictureAdapter(androidViewModel)
        binding.rvPicture.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPicture.adapter = adapter
        adapter.submit(picList)

        binding.autoPicture.adapter = AutoPicture().apply {
            this.submit(picList)
        }
        binding.autoPicture.layoutManager = SpeedyLinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        StartSnapHelper().attachToRecyclerView(binding.autoPicture)
        binding.autoPicture.scrollToPosition(3000*picList.size)
        binding.autoPicture.start()
    }

    override fun onPause() {
        WuBaiMediaPlayer.stopMediaPlayer()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        WuBaiMediaPlayer.startMediaPlayer()
    }

}