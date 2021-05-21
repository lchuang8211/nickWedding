package com.nick.wedding.merryme

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.Constraints
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nick.wedding.AutoText
import com.nick.wedding.R
import com.nick.wedding.base.BaseActivity
import com.nick.wedding.base.BaseViewModel
import com.nick.wedding.databinding.ActivityMerryMeBinding
import com.nick.wedding.databinding.LayoutPopupwindowBinding
import com.nick.wedding.databinding.LayoutPopupwindowExchangeCheckBinding
import com.nick.wedding.merryme.recyclerview.ExchangeAdapter
import com.nick.wedding.merryme.recyclerview.ExchangeRecordAdapter
import com.nick.wedding.merryme.recyclerview.OutSignDateAdapter
import com.nick.wedding.picture.PictureActivity
import com.nick.wedding.surpport.WuBaiMediaPlayer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class MerryMeActivity : BaseActivity() , ExchangeAdapter.ExchangeListener{

    override lateinit var viewModel: BaseViewModel

    lateinit var androidViewModel : MerryMeViewModel

    override lateinit var binding: ActivityMerryMeBinding

    lateinit var popupWindowBinding: LayoutPopupwindowBinding
    lateinit var popupWindow: PopupWindow

    lateinit var popupWindowExchangeBinding: LayoutPopupwindowExchangeCheckBinding
    lateinit var popupWindowExchange: PopupWindow

    /** 兌換區 Adapter */
    lateinit var exchangeAdapter : ExchangeAdapter

    val duckAnimation = Observable.interval(5500, TimeUnit.MILLISECONDS)
    val animator = AnimatorSet()

    var popHeight = 0
    var popOutHeight = 0

    var lHeight = 0
    var mVolume: Float = 0.7f

    var exchangeItemTitle = ""
    var exchangePrice = 0

    var totalWidth = 0f

    override fun onPause() {
        WuBaiMediaPlayer.stopMediaPlayer()
        animator.pause()
        duckAnimation?.unsubscribeOn(AndroidSchedulers.mainThread())
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        WuBaiMediaPlayer.startMediaPlayer()
        animator.resume()
        flyAnimation()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        androidViewModel = ViewModelProvider(this).get(MerryMeViewModel::class.java)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_merry_me
        )

        binding.lifecycleOwner = this

        binding.rvBanner.adapter = AutoText().apply {
            this.submit(
                arrayListOf(
                    "", "", "", "", "", "", "", "",
                    "我是OPEN將",
                    "快樂的一天",
                    "跟你一起OPEN",
                    "我就是卡哇伊的OPEN將",
                    "啦啦啦 OH~ OPEN",
                    "",
                    "每天都跟你在一起呦"
                )
            )
//            "克堂梨花春光輝",
//            "風吹落春草色已",
//            "邪拭淚落葉紛紛",
//            "我為此日高風起",
//            "愛君恩深做女伴",
//            "妳騷人不出塵土",
//            "嫁七十四海風霜",
//            "給殘燈前事無力",
//            "我今古今日蕭蕭",
//            "吧笙歌吹落處處"
        }

        binding.rvBanner.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvBanner.start(0)

        exchangeAdapter = ExchangeAdapter(androidViewModel)

        initPopupWindow()
        initClick()
        initObserver()
    }

    private fun initObserver() {
        androidViewModel.exchangeClick.observe(this, androidx.lifecycle.Observer {
            if (it){ // 打開兌換
                popupWindowBinding.rvItemOut.visibility = View.GONE
                popupWindowBinding.btnExchange.visibility = View.GONE
                popupWindowBinding.btnSignIn.visibility = View.GONE

                popupWindowBinding.layoutExchange.root.visibility = View.VISIBLE
            }else{
                popupWindowBinding.rvItemOut.visibility = View.VISIBLE
                popupWindowBinding.btnExchange.visibility = View.VISIBLE
                popupWindowBinding.btnSignIn.visibility = View.VISIBLE

                popupWindowBinding.layoutExchange.root.visibility = View.GONE
            }
        })

        androidViewModel.exchangeList.observe(this, androidx.lifecycle.Observer {

            popupWindowBinding.layoutExchange.rvExchange.let {
                it.adapter = exchangeAdapter
                it.layoutManager = LinearLayoutManager(this)
                exchangeAdapter.setListener(this)
            }

            exchangeAdapter.submit(it, androidViewModel.exchangePage)
        })

        androidViewModel.changeDate.observe(this, androidx.lifecycle.Observer {
            if (it == false)
                return@Observer

            val currentTime = Calendar.getInstance()
            val yBefore = Calendar.getInstance()
            yBefore.set(2021,Calendar.JANUARY,1)
            val monthSize = (currentTime.get(Calendar.YEAR) - yBefore.get(Calendar.YEAR))*12 + (currentTime.get(Calendar.MONTH) - yBefore.get(Calendar.MONTH) +1)
//            Timber.tag("hlcDebug").d("array size : ${(currentTime.get(Calendar.YEAR) - yBefore.get(Calendar.YEAR))*12 + (currentTime.get(Calendar.MONTH) - yBefore.get(Calendar.MONTH) +1)}")

            var list = mutableListOf<Pair<Int, Int>>()
            var range = 0
            for (i in 1..monthSize){
                if (i%12==0) range+=1
                list.add(Pair(2021+range, i%12))
            }
            Timber.tag("hlcDebug").d("list : $list")

            val adpter = OutSignDateAdapter(androidViewModel)
            popupWindowBinding.rvItemOut.apply {
                this.adapter = adpter
                this.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
            }
            adpter.submit(list)
            popupWindowBinding.rvItemOut.scrollToPosition(list.size-1)

            popupWindowBinding.tvCookieCount.text = "累計 ${androidViewModel.totalCookie.toString()} 個"
            popupWindowBinding.tvDateCount.text = "簽到 ${androidViewModel.totalDate.toString()} 天"

            androidViewModel.changeDate.value = false
        })

        androidViewModel.signYet.observe(this, androidx.lifecycle.Observer {
            if (it > 0)
                Toast.makeText(this, ":+:簽簽簽:+:", Toast.LENGTH_SHORT).show()
            else if (it == -1)
                Toast.makeText(this, "~~ 簽過囉 ~~", Toast.LENGTH_SHORT).show()
        })

        androidViewModel.exchangeRecordList.observe(this, androidx.lifecycle.Observer {
            val adapter = ExchangeRecordAdapter(androidViewModel)
            popupWindowExchangeBinding.rvExchangeRecord.adapter = adapter
            popupWindowExchangeBinding.rvExchangeRecord.layoutManager = LinearLayoutManager(this)
            adapter.submit(it)
        })

        androidViewModel.exchangeSuccess.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it)
                    Toast.makeText(
                        this,
                        "兌換 ${androidViewModel.exchangeItemTitle} 花費 ${androidViewModel.exchangePrice} 個",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    Toast.makeText(this, "寶寶吐司不夠唷", Toast.LENGTH_SHORT).show()
            }
        })

        /** 監聽 View 的 layout 變動 (在初始化之前都是尚未繪製完成的狀態，即 width, heigh = 0) */
        binding.clBackground.viewTreeObserver.addOnGlobalLayoutListener {
            totalWidth = binding.clBackground.width.toFloat() + binding.ivFly.width.toFloat()
            setAnim()
            flyAnimation()
        }
    }

    private fun initPopupWindow() {
        popupWindowBinding =
            LayoutPopupwindowBinding.inflate(LayoutInflater.from(this), null, false)
        popupWindow = PopupWindow(
            popupWindowBinding.root,
            Constraints.LayoutParams.WRAP_CONTENT,
            Constraints.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.isOutsideTouchable = true

        popupWindowExchangeBinding =
            LayoutPopupwindowExchangeCheckBinding.inflate(LayoutInflater.from(this), null, false)
        popupWindowExchange = PopupWindow(
            popupWindowExchangeBinding.root,
            Constraints.LayoutParams.WRAP_CONTENT,
            Constraints.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindowExchange.isOutsideTouchable = false
    }

    fun setAnim() {
        val one = ObjectAnimator.ofFloat(
            binding.ivFly,
            "translationX",
            *floatArrayOf(0f, totalWidth) //-currentX
        )
        val two = ObjectAnimator.ofFloat(
            binding.ivFly,
            "translationY",
            *floatArrayOf(0f, -20f, 20f, -20f, 20f, 0f)
        )
        animator.play(one).with(two)
        animator.duration = 5000
        animator.interpolator = LinearInterpolator()
    }

    private fun flyAnimation() {
        duckAnimation
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    animator.start()
                },
                {
                    Timber.tag("hlcDebug").d(" throwable: $it")
                }
            )
    }

    fun smallPoAnimation(open:Boolean){
        Timber.tag("hlcDebug").d("smallPoAnimation : $open")
        binding.ivSmallPo.postOnAnimation {
            if (open) {
                ObjectAnimator.ofFloat(
                    binding.ivSmallPo,
                    "translationX",
                    *floatArrayOf(
                        0f,
                        binding.ivSmallPo.width.toFloat() - binding.tvSmallPo.width.toFloat()
                    )
                ).apply {
                    duration = 200
                    start()
                }
            }else{
                ObjectAnimator.ofFloat(
                    binding.ivSmallPo,
                    "translationX",
                    *floatArrayOf(binding.ivSmallPo.width.toFloat() - binding.tvSmallPo.width.toFloat(), 0f)
                ).apply {
                    duration = 200
                    start()
                }
            }
        }
    }

    private fun initClick() {

        binding.ivSmallPo.setOnClickListener {

            androidViewModel.scrollOut()

            val rect = Rect()
            val window = window
            window.decorView.getWindowVisibleDisplayFrame(rect)
            val statusBarHeight = rect.top
            val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).getTop()
            var location = IntArray(2)
            binding.ivSmallPo.getLocationOnScreen(location)
            popupWindow.showAtLocation(
                binding.clBackground,
                Gravity.TOP,
                0,
                location[1] - statusBarHeight
            )
            smallPoAnimation(true)
        }

        binding.ivDinosaur.setOnClickListener {
            startActivity(Intent(this, PictureActivity::class.java))
        }

        /** 月曆區 popupwindow */
        popupWindowBinding.btnSignIn.setOnClickListener {
            androidViewModel.signToday()
        }

        popupWindowBinding.btnExchange.setOnClickListener {
            popOutHeight = popupWindowBinding.viewBackground.layoutParams.height
            popupWindowBinding.viewBackground.layoutParams.height += 200
            androidViewModel.getExchangePage()
            androidViewModel.exchangeClick.value = true
        }

        /** 兌換區列表 popupwindow */
        popupWindowBinding.layoutExchange.btnClose.setOnClickListener {
            popupWindowBinding.viewBackground.layoutParams.height -= 200
            androidViewModel.exchangeClick.value = false
        }

        popupWindowBinding.layoutExchange.btnRecord.setOnClickListener {

            popupWindowExchange.showAtLocation(
                binding.root,
                Gravity.CENTER,
                0,
                0
            )
            popupWindowExchangeBinding.llExchangeRecord.visibility = View.VISIBLE
            popupWindowExchangeBinding.topIconll.visibility = View.GONE
            popupWindowExchangeBinding.btnConfirm.visibility = View.GONE

            popHeight = popupWindowExchangeBinding.viewBackground.layoutParams.height
            popupWindowExchangeBinding.viewBackground.layoutParams.height = popHeight + 400

            androidViewModel.getExchangeRecord()
        }

        popupWindowBinding.layoutExchange.clTagButtonOne.setOnClickListener {
            androidViewModel.getExchangePage(popupWindowBinding.layoutExchange.tvFirstItem.text.toString())
        }

        popupWindowBinding.layoutExchange.clTagButtonTwo.setOnClickListener {
            androidViewModel.getExchangePage(popupWindowBinding.layoutExchange.tvSecondItem.text.toString())
        }

        popupWindowBinding.layoutExchange.clTagButtonThree.setOnClickListener {
            androidViewModel.getExchangePage(popupWindowBinding.layoutExchange.tvThirdItem.text.toString())
        }

        /** 兌換區確定 popupwindow */
        popupWindowExchangeBinding.btnBack.setOnClickListener {
            popupWindowExchange.dismiss()
        }

        popupWindowExchangeBinding.btnConfirm.setOnClickListener {
            Timber.tag("hlcDebug").d(" exchange : $exchangeItemTitle - $exchangePrice")

            popupWindowExchange.dismiss()
            // TODO 計算 function
            androidViewModel.checkCookie()
        }

        var settingStatus = false
        popupWindowBinding.layoutExchange.ivSetting.setOnClickListener {
            exchangeAdapter.submitSetting(!settingStatus)
            settingStatus = settingStatus.not()
            Timber.tag("hlcDebug").d(" ivSetting : $settingStatus")
        }

        popupWindow.setOnDismissListener {
            smallPoAnimation(false)
            settingStatus = false
            if (popOutHeight > 0) popupWindowBinding.viewBackground.layoutParams.height = popOutHeight
            androidViewModel.exchangeClick.value = false
            exchangeAdapter.submitSetting(false)
        }

        popupWindowExchange.setOnDismissListener {
            popupWindowExchangeBinding.viewBackground.layoutParams.height = popHeight
        }

    }

    fun updateProgress(num: Float) {
//        Single.just(1)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                popupWindowBinding.progressBar.progress = (num * 100).toInt()
////                Timber.tag("hlcDebug").d("progress: $num ${num.toInt()}")
//            }, {
//
//            })
    }


    override fun onFuctionListener(title: String, oldTitle: String, price: Int, settingStatus: Boolean, position: Int, page: Int) {

        if (settingStatus) {
            val category = when(page){
                1->{
                    popupWindowBinding.layoutExchange.tvFirstItem.text.toString()
                }
                2->{
                    popupWindowBinding.layoutExchange.tvSecondItem.text.toString()
                }
                3->{
                    popupWindowBinding.layoutExchange.tvThirdItem.text.toString()
                }
                else -> ""
            }
            androidViewModel.insertExchangeItem(category, position, title, oldTitle, price)
            return
        }

        androidViewModel.exchangeItemTitle = title
        androidViewModel.exchangePrice = price

        popupWindowExchange.showAtLocation(
            binding.root,
            Gravity.CENTER,
            0,
            0
        )

        popHeight = popupWindowExchangeBinding.viewBackground.layoutParams.height

        popupWindowExchangeBinding.llExchangeRecord.visibility = View.GONE
        popupWindowExchangeBinding.topIconll.visibility = View.VISIBLE
        popupWindowExchangeBinding.btnConfirm.visibility = View.VISIBLE

        popupWindowExchangeBinding.tvSpendTitle.text = "要兌換${title}嗎？"
        popupWindowExchangeBinding.tvSpendValues.text = "${price}個"
    }

}