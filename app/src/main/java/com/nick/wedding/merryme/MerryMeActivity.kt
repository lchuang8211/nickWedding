package com.nick.wedding.merryme

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.Constraints
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.bind.DateTypeAdapter
import com.nick.wedding.AutoText
import com.nick.wedding.R
import com.nick.wedding.base.BaseActivity
import com.nick.wedding.base.BaseViewModel
import com.nick.wedding.databinding.ActivityMerryMeBinding
import com.nick.wedding.databinding.LayoutPopupwindowBinding
import com.nick.wedding.merryme.recyclerview.SignDateAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable.interval
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.internal.wait
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class MerryMeActivity : BaseActivity() {

    override lateinit var viewModel: BaseViewModel

    lateinit var androidViewModel : MerryMeViewModel

    override lateinit var binding: ActivityMerryMeBinding

    lateinit var popupWindowBinding: LayoutPopupwindowBinding
    lateinit var popupWindow: PopupWindow

    lateinit var mediaPlayer: MediaPlayer

    var mVolume: Float = 0.7f

    override fun onPause() {
        mediaPlayer.pause()
        Timber.tag("hlcDebug").d(" onPause mediaPlayer stop")
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            Timber.tag("hlcDebug").d(" onResume mediaPlayer start")
        }
        flyAnimation()
    }

    override fun onBackPressed() {
        mediaPlayer.pause()
        super.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        androidViewModel = ViewModelProvider(this).get(MerryMeViewModel::class.java)

        mediaPlayer = MediaPlayer.create(this,
            R.raw.wu_bai_till_the_end_of_time_1
        )
        mediaPlayer.isLooping = true

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_merry_me
        )
        binding.lifecycleOwner = this

        binding.rvBanner.adapter = AutoText().apply {
            this.submit(
                arrayListOf(
                    "", "", "", "", "", "", "", "",
                    "漢皇重色思傾國，", "御宇多年求不得。", "楊家有女初長成，", "養在深閨人未識。",
                    "天生麗質難自棄，", "一朝選在君王側。", "回眸一笑百媚生，", "六宮粉黛無顏色。",
                    "春寒賜浴華清池，", "溫泉水滑洗凝脂。", "侍兒扶起嬌無力，", "始是新承恩澤時。",
                    "雲鬢花顏金步搖，", "芙蓉帳暖度春宵。", "春宵苦短日高起，", "從此君王不早朝。",
                    "承歡侍宴無閒暇，", "春從春遊夜專夜。", "後宮佳麗三千人，", "三千寵愛在一身。",
                    "金屋妝成嬌侍夜，", "玉樓宴罷醉和春。", "姊妹弟兄皆列土，", "可憐光彩生門戶。",
                    "遂令天下父母心，", "不重生男重生女。", "驪宮高處入青雲，", "仙樂風飄處處聞。",
                    "緩歌慢舞凝絲竹，", "盡日君王看不足。", "漁陽鼙鼓動地來，", "驚破霓裳羽衣曲。",
                    " ",
                    "九重城闕煙塵生，", "千乘萬騎西南行。", "翠華搖搖行復止，", "西出都門百餘里。",
                    "六軍不發無奈何，", "宛轉蛾眉馬前死。", "花鈿委地無人收，", "翠翹金雀玉搔頭。",
                    "君王掩面救不得，", "回看血淚相和流。", "黃埃散漫風蕭索，", "雲棧縈紆登劍閣。",
                    "峨嵋山下少人行，", "旌旗無光日色薄。", "蜀江水碧蜀山青，", "聖主朝朝暮暮情。",
                    "行宮見月傷心色，", "夜雨聞鈴腸斷聲。",
                    " ",
                    "天旋地轉迴龍馭，", "到此躊躇不能去。", "馬嵬坡下泥土中，", "不見玉顏空死處。",
                    "君臣相顧盡霑衣，", "東望都門信馬歸。", "歸來池苑皆依舊，", "太液芙蓉未央柳。",
                    "芙蓉如面柳如眉，", "對此如何不淚垂。", "春風桃李花開日，", "秋雨梧桐葉落時。",
                    "西宮南內多秋草，", "落葉滿階紅不掃。", "梨園弟子白髮新，", "椒房阿監青娥老。",
                    "夕殿螢飛思悄然，", "孤燈挑盡未成眠。", "遲遲鐘鼓初長夜，", "耿耿星河欲曙天。",
                    "鴛鴦瓦冷霜華重，", "翡翠衾寒誰與共。", "悠悠生死別經年，", "魂魄不曾來入夢。",
                    " ",
                    "臨邛道士鴻都客，", "能以精誠致魂魄。", "為感君王輾轉思，", "遂教方士殷勤覓。",
                    "排空馭氣奔如電，", "昇天入地求之遍。", "上窮碧落下黃泉，", "兩處茫茫皆不見。",
                    "忽聞海上有仙山，", "山在虛無縹緲間。", "樓閣玲瓏五雲起，", "其中綽約多仙子。",
                    "中有一人字太真，", "雪膚花貌參差是。", "金闕西廂叩玉扃，", "轉教小玉報雙成。",
                    "聞道漢家天子使，", "九華帳裏夢魂驚。", "攬衣推枕起徘徊，", "珠箔銀屏迤邐開。",
                    "雲髻半偏新睡覺，", "花冠不整下堂來。", "風吹仙袂飄颻舉，", "猶似霓裳羽衣舞。",
                    "玉容寂寞淚闌干，", "梨花一枝春帶雨。", "含情凝睇謝君王，", "一別音容兩渺茫。",
                    "昭陽殿裏恩愛絕，", "蓬萊宮中日月長。", "回頭下望人寰處，", "不見長安見塵霧。",
                    "唯將舊物表深情，", "鈿合金釵寄將去。", "釵留一股合一扇，", "釵擘黃金合分鈿。",
                    "但教心似金鈿堅，", "天上人間會相見。", "臨別殷勤重寄詞，", "詞中有誓兩心知。",
                    "七月七日長生殿，", "夜半無人私語時。", "在天願作比翼鳥，", "在地願為連理枝。",
                    "天長地久有時盡，", "此恨綿綿無絕期。"
                )
            )
        }

        binding.rvBanner.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvBanner.start(0)

        initComponent()
        initClick()
        initObserver()
    }

    private fun initObserver() {
        androidViewModel.changeDate.observe(this, androidx.lifecycle.Observer {
            if (it == false)
                return@Observer

            val adpter = SignDateAdapter(androidViewModel)
            popupWindowBinding.rvDate.apply {
                this.adapter = adpter
                this.layoutManager = GridLayoutManager(context,7)
            }

            adpter.submit(androidViewModel.mDate.value!!)
            androidViewModel.changeDate.value = false
        })
        androidViewModel.signYet.observe(this, androidx.lifecycle.Observer {
            if (it)
                Toast.makeText(this,":+:簽簽簽:+:",Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this,"~~ 簽過囉 ~~",Toast.LENGTH_SHORT).show()
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    private fun initComponent() {
        popupWindowBinding =
            LayoutPopupwindowBinding.inflate(LayoutInflater.from(this), null, false)
        popupWindow = PopupWindow(
            popupWindowBinding.root,
            Constraints.LayoutParams.WRAP_CONTENT,
            Constraints.LayoutParams.WRAP_CONTENT,
            true
        )
//        popupWindow.animationStyle = R.style.PopupWindowAnimation
        popupWindow.isOutsideTouchable = true

        binding.botNavLL.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    Timber.tag("hlcDebug").d(" ACTION_DOWN ACTION_MOVE: ")
//                    setAnimation(true)
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    Timber.tag("hlcDebug").d(" ACTION_UP: ")
//                    setAnimation(false)
                    return@setOnTouchListener false
                }
                else -> {
                    return@setOnTouchListener false
                }
            }
        }

    }

    private fun flyAnimation() {

        Observable.interval(5500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    binding.ivFly.visibility = View.VISIBLE
                    val anim = AnimatorSet()
                    val one = ObjectAnimator.ofFloat(
                        binding.ivFly,
                        "translationX",
                        *floatArrayOf(
                            -binding.ivFly.width.toFloat(),
                            binding.clBackground.width.toFloat()
                        )
                    )
                    val two = ObjectAnimator.ofFloat(
                        binding.ivFly,
                        "translationY",
                        *floatArrayOf( 0f, -20f, 20f, -20f, 20f, 0f)
                    )
                    anim.play(one).with(two)
                    anim.duration = 5000
                    anim.interpolator = LinearInterpolator()
                    anim.start()
                },
                {}
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
//            androidViewModel.setDate()

            androidViewModel.getHoldMonth()
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

        popupWindowBinding.smallV.setOnClickListener {
            mVolume -= 0.1f
            if (mVolume < 0.1f) mVolume = 0f
            mediaPlayer.setVolume(mVolume, mVolume)
            updateProgress(mVolume)
        }

        popupWindowBinding.bigV.setOnClickListener {
            mVolume += 0.1f
            if (mVolume >= 1f) mVolume = 1f
            mediaPlayer.setVolume(mVolume, mVolume)
            updateProgress(mVolume)
        }

        popupWindowBinding.btnSignIn.setOnClickListener {
            signInNow()
//            popupWindow.dismiss()
        }

        popupWindow.setOnDismissListener {
            Timber.tag("hlcDebug").d(" setOnDismissListener: ")
            smallPoAnimation(false)
        }

    }

    fun signInNow(){
        var currentTime = Calendar.getInstance()
        currentTime.get(Calendar.DAY_OF_MONTH)
        currentTime.get(Calendar.YEAR)
        currentTime.get(Calendar.MONTH)+1

        androidViewModel.signToday()

    }

    fun updateProgress(num: Float) {
        Single.just(1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                popupWindowBinding.progressBar.progress = (num * 100).toInt()
//                Timber.tag("hlcDebug").d("progress: $num ${num.toInt()}")
            }, {

            })

    }

    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }
}