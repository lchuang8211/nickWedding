package com.example.demovm.support.textBanner

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.picture.adapter.AutoPicture
import timber.log.Timber
import java.lang.ref.WeakReference

//透過 RecyclerView 實現文字跑馬燈
//文字自動跑馬燈
class AutoPictureBannerRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    var autoBannerTask: AutoBannerTask

    init {
        autoBannerTask = AutoBannerTask(this)
    }

    companion object {
        // 移動速度 每幾毫秒移動一次
        private const val TIME_AUTO_BANNER: Long = 4000
    }

    private var running = false   // 標記是否正在自動跑馬燈
    private var canRun = false

    class AutoBannerTask(reference: AutoPictureBannerRecyclerView) : Runnable {

        var number = 0
        private var mReference: WeakReference<AutoPictureBannerRecyclerView>

        init {
            mReference = WeakReference<AutoPictureBannerRecyclerView>(reference)
        }

        override fun run() {
            val recyclerView: AutoPictureBannerRecyclerView
            recyclerView = mReference?.get()!!
            if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                var scroll = true
                // 每次移動幾像素

                recyclerView.smoothScrollBy( recyclerView.getChildAt(0).width , 0)
                recyclerView.postDelayed(recyclerView.autoBannerTask, TIME_AUTO_BANNER)

                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        when (newState) {
                            0 -> { //SCROLL_STATE_IDLE

                            }
                            1, 2 -> { //1: SCROLL_STATE_DRAGGING 2: SCROLL_STATE_SETTLING
                                recyclerView.smoothScrollBy(0, 0);
                            }
                        }
                    }
                })

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