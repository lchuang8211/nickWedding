package com.example.demovm.support.textBanner

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * 改變 Recycler View 的捲動速度(smooth)
 * */
class SpeedyLinearLayoutManager : LinearLayoutManager {

    companion object {
        /**
         * 原生系統 default is 25f (bigger = slower)
         * 目前自訂 1000f
         * */
        private const val MILLISECONDS_PER_INCH = 1000f //default is 25f (bigger = slower)
    }

    constructor(context: Context?) : super(context) {}
    constructor(
        context: Context?,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(context, orientation, reverseLayout) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(recyclerView.context) {
                override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                    return super.computeScrollVectorForPosition(targetPosition)
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
                }
            }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }


}