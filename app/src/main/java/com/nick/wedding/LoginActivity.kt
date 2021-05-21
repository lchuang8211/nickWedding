package com.nick.wedding

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nick.wedding.base.BaseActivity
import com.nick.wedding.databinding.ActivityLoginBinding
import com.nick.wedding.merryme.MerryMeActivity
import com.nick.wedding.surpport.ToastUntil
import timber.log.Timber

class LoginActivity : BaseActivity() {

    override lateinit var viewModel: MainViewModel

    override lateinit var binding : ActivityLoginBinding

    lateinit var biometricManager : BiometricManager
    lateinit var biometricPrompt : BiometricPrompt.PromptInfo

    val myPWD = "1226"
    val LoginFingerKey = "LoginFingerKey"
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this

        sharedPreferences = this.getSharedPreferences("Login", Context.MODE_PRIVATE)
        viewModel.finger.value = sharedPreferences.getBoolean(LoginFingerKey,false)

        initComponent()
        initObserver()
    }

    private fun initObserver() {
        biometricManager = BiometricManager.from(this)

        biometricPrompt = BiometricPrompt.PromptInfo.Builder()
            .setTitle("給我妳的小手手")
            .setNegativeButtonText("取消")
            .build()

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Timber.tag("hlcDebug").d(" BIOMETRIC_SUCCESS: 可以使用")
                binding.llFinger.visibility = View.VISIBLE
                if (sharedPreferences.getBoolean(LoginFingerKey,false)) {
                    useBiometricPrompt()
                    binding.ivFinger.setImageResource(R.drawable.icon_tick)
                } else {
                    binding.ivFinger.setImageResource(R.drawable.icon_cross)
                }
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Timber.tag("hlcDebug").d(" BIOMETRIC_ERROR_NO_HARDWARE: 硬體不支持此功能")
                binding.llFinger.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Timber.tag("hlcDebug").d(" BIOMETRIC_ERROR_HW_UNAVAILABLE: 目前無法使用")
                binding.llFinger.visibility = View.GONE
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.llFinger.visibility = View.GONE
                Timber.tag("hlcDebug").d(" BIOMETRIC_ERROR_NONE_ENROLLED: 沒有設置")
            }
        }

        viewModel.finger.observe(this, Observer { finger ->
            if (finger) {
                binding.ivFinger.setImageResource(R.drawable.icon_tick)
            } else {
                binding.ivFinger.setImageResource(R.drawable.icon_cross)
            }
        })




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
            val pwd = binding.evPassword.text.toString()
            if (pwd.equals("")) {
                ToastUntil("我們的愛情密碼，難道是一場空嗎？", Toast.LENGTH_LONG)
            } else if (!pwd.equals(myPWD)){
                ToastUntil("我們的愛情密碼，注定在那特別的紀念日裡", Toast.LENGTH_LONG)
            } else if (pwd.equals(myPWD)) {
                gogogo()
            }
        }

        binding.llFinger.setOnClickListener {
            if (viewModel.finger.value != null) {
                viewModel.finger.value = viewModel.finger.value!!.not()
                sharedPreferences.edit().putBoolean(LoginFingerKey, viewModel.finger.value!!).commit()
            }
        }
    }

    fun useBiometricPrompt(){
        BiometricPrompt(this, ContextCompat.getMainExecutor(this),object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Timber.tag("hlcDebug").d(" Error: ")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Timber.tag("hlcDebug").d(" Succeeded: ")
                gogogo()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Timber.tag("hlcDebug").d(" Failed: ")
            }
        }).authenticate(biometricPrompt)
    }

    fun gogogo(){
        startActivity(Intent(this, MerryMeActivity::class.java))
    }
}