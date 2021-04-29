package com.nick.wedding

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.nick.wedding.databinding.LayoutBottomBinding
import timber.log.Timber

class BottomNavLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutBottomBinding
    private var mCall : onFuctionListener ?= null


    interface onFuctionListener {
        fun onClick()
    }

    fun setFuctionLitsener(mCall: onFuctionListener){
        this.mCall = mCall
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = LayoutBottomBinding.inflate(inflater,this,true)

        initComponent()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initComponent() {
//        binding.ivBotOne.setOnTouchListener { v, event ->
//            onViewTouch(v, event)
//        }
        binding.ivBotTwo.setOnTouchListener { v, event ->
            onViewTouch(v, event)
        }
        binding.ivBotThree.setOnTouchListener { v, event ->
            onViewTouch(v, event)
        }
        binding.ivBotFour.setOnTouchListener { v, event ->
            onViewTouch(v, event)
        }
        binding.ivBotFive.setOnTouchListener { v, event ->
            onViewTouch(v, event)
        }

//        binding.ivBotOne.setOnClickListener {
//            Timber.tag("hlcDebug").d(" Click: 1")
//        }
        binding.ivBotTwo.setOnClickListener {
            Timber.tag("hlcDebug").d(" Click: 2")
        }
        // 兌換
        binding.ivBotThree.setOnClickListener {
            Timber.tag("hlcDebug").d(" Click: 3")
            mCall?.onClick()
        }
        binding.ivBotFour.setOnClickListener {
            Timber.tag("hlcDebug").d(" Click: 4")
        }
        binding.ivBotFive.setOnClickListener {
            Timber.tag("hlcDebug").d(" Click: 5")
        }

    }
    fun onViewTouch(view: View, event: MotionEvent): Boolean{
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                Timber.tag("hlcDebug").d(" ACTION_DOWN: ")
//                    setAnimation(view, true)
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                Timber.tag("hlcDebug").d("ACTION_MOVE: (${view.x},${view.y}) ~  (${event.x}, ${event.y}) ~ (${view.x + view.width},${view.y + view.height})")
//                if (event.x < view.x || event.x > (view.x + view.width) || event.y < view.y || event.y > (view.y + view.height) ){
//                    setAnimation(view, false)
//                }else {
//                    setAnimation(view, true)
//                    performClick(event.x, event.y)
//                }
                return false
            }
//            MotionEvent.ACTION_UP -> {
//                Timber.tag("hlcDebug").d(" ACTION_UP: ")
//                setAnimation(view, false)
//                return false
//            }
            else -> {
                return false
            }
        }
    }

    fun performClick(x:Float,y:Float){

    }

    fun setAnimation(view: View, boolean: Boolean){
        val imageView: ImageView = when(view.id){
//            binding.ivBotOne.id -> binding.ivBotOne
            binding.ivBotTwo.id -> binding.ivBotTwo
            binding.ivBotThree.id -> binding.ivBotThree
            binding.ivBotFour.id -> binding.ivBotFour
            else -> binding.ivBotFive
        }
        if (boolean){
            Timber.tag("hlcDebug").d("height : ${imageView.height.toFloat()}")
            imageView.postOnAnimation{
                ObjectAnimator.ofFloat(
                    imageView,
                    "translationY",
                    *floatArrayOf(-30f)
                )
                    .apply {
                        //動畫時間長度
                        duration = 100
                        //開始執行動畫
                        start()
                    }
            }
        } else {
            imageView.postOnAnimation{
                ObjectAnimator.ofFloat(
                    imageView,
                    "translationY",
                    *floatArrayOf( 0f )
                )
                    .apply {
                        //動畫時間長度
                        duration = 100
                        //開始執行動畫
                        start()
                    }
            }
        }
    }


}