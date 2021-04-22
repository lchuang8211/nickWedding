package com.example.demovm.support.textBanner

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

//透過 RecyclerView 實現文字跑馬燈
//文字自動跑馬燈(垂直)
class AutoTextVerticalBannerRecyclerView(context: Context, attrs: AttributeSet?) :
    RecyclerView(context, attrs) {

    var autoBannerTask: AutoBannerTask
    var autoBannerTaskSmooth: AutoBannerTask

    init {
        autoBannerTask = AutoBannerTask(this,false)
        autoBannerTaskSmooth = AutoBannerTask(this,true)
    }

    companion object {
        // 移動速度 每幾毫秒移動一次
        private var TIME_AUTO_BANNER: Long = 3000
        private var row: Int = 1
    }

    private var running = false   // 標記是否正在自動跑馬燈
    private var canRun = false

    class AutoBannerTask(reference: AutoTextVerticalBannerRecyclerView, private var smooth: Boolean) : Runnable {

        private var mReference: WeakReference<AutoTextVerticalBannerRecyclerView>

        init {
            mReference = WeakReference<AutoTextVerticalBannerRecyclerView>(reference)
        }

        override fun run() {
            val recyclerView: AutoTextVerticalBannerRecyclerView
            recyclerView = mReference?.get()!!
            recyclerView.let {
                if (recyclerView.running && recyclerView.canRun) {
                    // 每次移動幾row
                    if (!smooth) {
                        recyclerView.scrollBy(0, recyclerView.getChildAt(0).height * row)
                        recyclerView.postDelayed(recyclerView.autoBannerTask,
                            TIME_AUTO_BANNER
                        )
                    }
                    else {
                        recyclerView.scrollBy(0, 2)
                        recyclerView.postDelayed(recyclerView.autoBannerTaskSmooth, TIME_AUTO_BANNER)
                    }
                }

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

    fun setTimeAutoBanner(time: Long) {
        TIME_AUTO_BANNER = time
    }

    fun start(row: Int) {
        if (running) stop()
        canRun = true
        running = true

        if (row == 0){
            TIME_AUTO_BANNER = 50
            postDelayed(autoBannerTaskSmooth, 1000)
        } else {
            Companion.row = row
            postDelayed(autoBannerTask,
                50
            )
        }
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