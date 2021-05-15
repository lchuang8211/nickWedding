package com.nick.wedding.surpport

import android.content.Context
import android.widget.Toast

object ToastUntil{

    lateinit var context: Context

    fun setToast(context: Context){
        this.context = context
    }
    /**
     * @param String 字串
     * @param Int Toast.LENGTH_SHORT or Toast.LENGTH_Long
     * */
    operator fun invoke(text:String, time: Int = Toast.LENGTH_SHORT){
        Toast.makeText(context, text, time).show()
    }
}