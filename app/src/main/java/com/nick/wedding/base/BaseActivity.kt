package com.nick.wedding.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.nick.wedding.R

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val viewModel : BaseViewModel

    protected abstract val binding : ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }
}