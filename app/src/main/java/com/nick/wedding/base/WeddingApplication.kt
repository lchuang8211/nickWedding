package com.nick.wedding.base

import android.app.Application
import com.nick.wedding.BuildConfig
import com.nick.wedding.surpport.WuBaiMediaPlayer
import timber.log.Timber

class WeddingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //檢查環境為 Debug 版時初始化 Timber(Log) 訊息，在正式版則不會初始化
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        WuBaiMediaPlayer.setMediaPlayerContext(applicationContext)
    }


}