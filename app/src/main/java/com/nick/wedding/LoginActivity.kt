package com.nick.wedding

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.nick.wedding.base.BaseActivity
import com.nick.wedding.databinding.ActivityLoginBinding
import com.nick.wedding.merryme.MerryMeActivity
import timber.log.Timber

class LoginActivity : BaseActivity() {

    override lateinit var viewModel: MainViewModel

    override lateinit var binding : ActivityLoginBinding

    lateinit var biometricManager : BiometricManager
    lateinit var biometricPrompt : BiometricPrompt.PromptInfo

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
        biometricManager = BiometricManager.from(this)

        biometricPrompt = BiometricPrompt.PromptInfo.Builder()
            .setTitle("給我妳的小手手")
            .setNegativeButtonText("取消")
            .build()

        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Timber.tag("hlcDebug").d(" BIOMETRIC_SUCCESS: 可以使用")
                useBiometricPrompt()
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Timber.tag("hlcDebug").d(" BIOMETRIC_ERROR_NO_HARDWARE: 硬體不支持此功能")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Timber.tag("hlcDebug").d(" BIOMETRIC_ERROR_HW_UNAVAILABLE: 目前無法使用")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Timber.tag("hlcDebug").d(" BIOMETRIC_ERROR_NONE_ENROLLED: 沒有設置")
        }


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
                return@setOnClickListener
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