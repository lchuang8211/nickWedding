package com.nick.wedding.picture

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demovm.support.textBanner.SpeedyLinearLayoutManager
import com.example.demovm.support.textBanner.StartSnapHelper
import com.nick.wedding.R
import com.nick.wedding.base.BaseActivity
import com.nick.wedding.base.BaseViewModel
import com.nick.wedding.databinding.ActivityPictureBinding
import com.nick.wedding.picture.adapter.AutoPicture
import com.nick.wedding.picture.adapter.PictureAdapter
import com.nick.wedding.surpport.WuBaiMediaPlayer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class PictureActivity : BaseActivity() {

    override lateinit var viewModel: BaseViewModel

    override lateinit var binding: ActivityPictureBinding

    lateinit var androidViewModel: PictureViewModel

    val picList = mutableListOf<Int>()

    val dogAnimation = Observable.interval(3550, TimeUnit.MILLISECONDS)
    val mouseAnimation = Observable.interval(3050, TimeUnit.MILLISECONDS)
    val ganbateAnimation = Observable.interval(3550, TimeUnit.MILLISECONDS)

    val dogAnimator = AnimatorSet()
    val mouseAnimator = AnimatorSet()
    val ganbateAnimator = AnimatorSet()

    init {
        WuBaiMediaPlayer.startMediaPlayer()
    }

    companion object{

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture)
        androidViewModel = ViewModelProvider(this).get(PictureViewModel::class.java)

        initData()
        initRecyclerView()
        initObserver()
    }

    private fun initObserver() {
        binding.ivMouse.viewTreeObserver.addOnGlobalLayoutListener {
            setMouseAnim()
            mouseAnimation()
        }
        binding.ivGanbate.viewTreeObserver.addOnGlobalLayoutListener {
            setGanbateAnim()
            ganbateAnimation()
        }
        binding.ivDog.viewTreeObserver.addOnGlobalLayoutListener {
            setDogAnim()
            dogAnimation()
        }
    }

    private fun initData() {

        //TODO add picture from SD card

        picList.add(R.drawable.picbanner13)
        picList.add(R.drawable.picbanner14)
        picList.add(R.drawable.picbanner15)
        picList.add(R.drawable.picbanner4)
        picList.add(R.drawable.picbanner5)
        picList.add(R.drawable.picbanner6)
        picList.add(R.drawable.picbanner7)
        picList.add(R.drawable.picbanner8)
        picList.add(R.drawable.picbanner9)
        picList.add(R.drawable.picbanner10)
        picList.add(R.drawable.picbanner11)
        picList.add(R.drawable.picbanner12)
//        picList.add(R.drawable.picbanner_m1)
//        picList.add(R.drawable.picbanner_m2)
//        picList.add(R.drawable.picbanner_m3)
//        picList.add(R.drawable.picbanner_m4)
//        picList.add(R.drawable.picbanner_m5)
//        picList.add(R.drawable.picbanner_m6)
//        picList.add(R.drawable.picbanner_m7)
//        picList.add(R.drawable.picbanner_m8)
//        picList.add(R.drawable.picbanner_m9)
//        picList.add(R.drawable.picbanner_m10)
//        picList.add(R.drawable.picbanner_m11)
//        picList.add(R.drawable.picbanner_m12)
//        picList.add(R.drawable.picbanner_m13)
//        picList.add(R.drawable.picbanner_m14)
//        picList.add(R.drawable.picbanner_m15)
//        picList.add(R.drawable.picbanner_m16)
//        picList.add(R.drawable.picbanner_m17)
//        picList.add(R.drawable.picbanner_m18)

    }

    private fun initRecyclerView() {
//        val adapter = PictureAdapter(androidViewModel)
//        binding.rvPicture.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.rvPicture.adapter = adapter
//        adapter.submit(picList)

        binding.autoPicture.adapter = AutoPicture().apply {
            this.submit(picList)
        }
        binding.autoPicture.layoutManager = SpeedyLinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        StartSnapHelper().attachToRecyclerView(binding.autoPicture)
        binding.autoPicture.scrollToPosition(3000*picList.size)
        binding.autoPicture.start()

    }


    fun randomTranslation(): FloatArray {
        val num = (6..9).random()+1
        val float = FloatArray(num)
        float.set(0, 0f)
        for (i in 1..num-2 ) {
            if (i%2==1)
                float.set(i, Random().nextFloat() * 150 *-1)
            else
                float.set(i, 0f)
        }
        float.set(num-1, 0f)
        return float
    }

    var dogXList : FloatArray = randomTranslation()
    var dogYList : FloatArray = randomTranslation()
    var ganbateXList : FloatArray = randomTranslation()
    var ganbateYList : FloatArray = randomTranslation()
    var mouseXList : FloatArray = randomTranslation()
    var mouseYList : FloatArray = randomTranslation()

    private fun dogAnimation(){
        mouseAnimation
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                dogXList = randomTranslation()
                dogYList = randomTranslation()
                dogAnimator.start()
            },{

            })
    }

    private fun mouseAnimation(){
        mouseAnimation
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mouseXList = randomTranslation()
                mouseYList = randomTranslation()
                mouseAnimator.start()
            },{

            })
    }

    private fun ganbateAnimation(){
        ganbateAnimation
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                ganbateXList = randomTranslation()
                ganbateYList = randomTranslation()
                ganbateAnimator.start()
            },{

            })
    }

    fun setDogAnim() {
        val dogY = ObjectAnimator.ofFloat(
            binding.ivDog,
            "translationY",
            *dogYList
        )

        val dogX = ObjectAnimator.ofFloat(
            binding.ivDog,
            "translationX",
            *dogXList
        )

        dogAnimator.play(dogX)
        dogAnimator.duration = 3000
        dogAnimator.interpolator = LinearInterpolator()
    }

    fun setMouseAnim() {
        val mouseY = ObjectAnimator.ofFloat(
            binding.ivMouse,
            "translationY",
            *mouseYList
        )

        val mouseX = ObjectAnimator.ofFloat(
            binding.ivMouse,
            "translationX",
            *mouseXList
        )

        mouseAnimator.play(mouseY)
        mouseAnimator.duration = 2500
        mouseAnimator.interpolator = LinearInterpolator()
    }

    fun setGanbateAnim(){
        val ganbateY = ObjectAnimator.ofFloat(
            binding.ivGanbate,
            "translationY",
            *ganbateYList
        )

        val ganbateX = ObjectAnimator.ofFloat(
            binding.ivGanbate,
            "translationX",
            *ganbateXList
        )

        ganbateAnimator.play(ganbateY)
        ganbateAnimator.duration = 3000
        ganbateAnimator.interpolator = LinearInterpolator()
    }

    override fun onPause() {
        WuBaiMediaPlayer.stopMediaPlayer()
        super.onPause()
        mouseAnimator.start()
        ganbateAnimator.start()
        dogAnimator.start()
        dogAnimation?.unsubscribeOn(AndroidSchedulers.mainThread())
        mouseAnimation?.unsubscribeOn(AndroidSchedulers.mainThread())
        ganbateAnimation?.unsubscribeOn(AndroidSchedulers.mainThread())
    }

    override fun onResume() {
        super.onResume()
        WuBaiMediaPlayer.startMediaPlayer()
        mouseAnimator.resume()
        ganbateAnimator.resume()
        dogAnimator.resume()
        dogAnimation()
        mouseAnimation()
        ganbateAnimation()
    }

}