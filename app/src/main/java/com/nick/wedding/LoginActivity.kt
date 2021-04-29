package com.nick.wedding

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.nick.wedding.base.BaseActivity
import com.nick.wedding.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    override lateinit var viewModel: MainViewModel

    override lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
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
//            if (binding.evPassword.text.toString() == "大雞雞") {
                Toast.makeText(this,"這尺寸好..好猛唷! <3",Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MerryMeActivity::class.java))
                return@setOnClickListener
//            }
            Toast.makeText(this,"尺寸不對唷 請輸入\"大雞雞\" <3",Toast.LENGTH_LONG).show()
        }
    }
}