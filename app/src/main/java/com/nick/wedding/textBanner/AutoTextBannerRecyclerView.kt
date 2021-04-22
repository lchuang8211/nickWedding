package com.example.demovm.support.textBanner

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

//透過 RecyclerView 實現文字跑馬燈
//文字自動跑馬燈
class AutoTextBannerRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    var autoBannerTask: AutoBannerTask

    init {
        autoBannerTask = AutoBannerTask(this)
    }

    companion object {
        // 移動速度 每幾毫秒移動一次
        private const val TIME_AUTO_BANNER: Long = 10

    }

    private var running = false   // 標記是否正在自動跑馬燈
    private var canRun = false

    class AutoBannerTask(reference: AutoTextBannerRecyclerView) : Runnable {

        private var mReference: WeakReference<AutoTextBannerRecyclerView>

        init {
            mReference = WeakReference<AutoTextBannerRecyclerView>(reference)
        }

        override fun run() {
            val recyclerView: AutoTextBannerRecyclerView
            recyclerView = mReference?.get()!!
            if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                // 每次移動幾像素
                recyclerView.scrollBy(2, 0)
                recyclerView.postDelayed(recyclerView.autoBannerTask, TIME_AUTO_BANNER)
            }
        }
    }

    fun start() {
        if (running) stop()
        canRun = true
        running = true
        postDelayed(
            autoBannerTask,
            TIME_AUTO_BANNER
        )
    }

    fun stop() {
        running = false
        removeCallbacks(autoBannerTask)
    }

    // 關閉觸碰事件
    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        return false
    }


}