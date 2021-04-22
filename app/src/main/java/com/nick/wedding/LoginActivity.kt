package com.nick.wedding

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.nick.wedding.base.BaseActivity
import com.nick.wedding.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    override lateinit var viewModel: MainViewModel

    override lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this

        initComponent()
        initObserver()
    }

    private fun initObserver() {

    }

    private fun initComponent() {
        binding.cvHead.postOnAnimation {
            ObjectAnimator.ofFloat(
                binding.cvHead,
                "translationY",
                *floatArrayOf(-binding.cvHead.height.toFloat(), 200f, 50f, 90f)
            ).apply {
                //動畫時間長度
                duration = 1000
                //開始執行動畫
                start()
            }
        }

        binding.ivHeart.setOnClickListener {
            startActivity(Intent(this, MerryMeActivity::class.java))
        }
    }
}