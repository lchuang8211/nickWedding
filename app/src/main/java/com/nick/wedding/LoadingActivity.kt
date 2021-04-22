package com.nick.wedding

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nick.wedding.base.BaseActivity
import com.nick.wedding.base.BaseViewModel
import com.nick.wedding.databinding.ActivityLoadingBinding
import com.nick.wedding.databinding.ActivityLoginBinding
import com.nick.wedding.databinding.ActivityMerryMeBinding

class LoadingActivity : BaseActivity() {

    override lateinit var viewModel: BaseViewModel

    override lateinit var binding: ActivityLoadingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_loading)
        binding.lifecycleOwner = this

        startActivity(Intent(this, LoginActivity::class.java))
    }
}