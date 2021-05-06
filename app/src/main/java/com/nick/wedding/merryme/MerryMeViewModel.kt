package com.nick.wedding.merryme

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nick.wedding.base.BaseViewModel
import com.nick.wedding.data.DateSign
import com.nick.wedding.database.WeddingOpenHelper
import com.nick.wedding.surpport.DatePictureHelper
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MerryMeViewModel(application: Application) : AndroidViewModel(application) {

    val stringData = MutableLiveData<String>("LiveData")
    val changeDate = MutableLiveData<Boolean>(false)
    val mDate = MutableLiveData<List<DateSign>>()
    val calendarTime = Calendar.getInstance()
    var cDay: Int = 1
    var cYear: Int = 2021
    var cMonth: Int = 1
    var cWMonth: Int = 1
    var cDWeek: Int = 0
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
    var today: String = ""

    init {
        cDay = calendarTime.get(Calendar.DAY_OF_MONTH)
        cYear = calendarTime.get(Calendar.YEAR)
        cMonth = calendarTime.get(Calendar.MONTH)+1
        cWMonth = calendarTime.get(Calendar.WEEK_OF_MONTH)
        cDWeek = calendarTime.get(Calendar.DAY_OF_WEEK)
        today = dateFormat.format(calendarTime.time)
        Timber.tag("hlcDebug").d(" today: $today")
    }

    /** 讀取舊資料 */
    fun initDate(){

    }

    fun setDate(){
        val list = mutableListOf<DateSign>()
        for (i in 0..30){
            list.add(DateSign( year = 2021, month = 5, week = i%7, date = i+1, picture = DatePictureHelper.DataChoice.values()[i].value, signed = ( (i+1)<cDay) ) )
        }
        mDate.value = list
        changeDate.value = true
    }

    val sqlHelper = WeddingOpenHelper(application)


    /** 今日簽到 */
    fun signToday(){

        /**
         * 1. 搜尋 DB
         * 2. 插入 DB
         * */
        val selectSql = sqlHelper.readableDatabase
        val writeSql = sqlHelper.writableDatabase
        val value = ContentValues()
        val dateTable = "dateTable"
        value.put("date", today)
        value.put("sign", false)

        writeSql.insertOrThrow(dateTable,null, value)

        val cursor = selectSql.rawQuery("select * from $dateTable", null)
        Timber.tag("hlcDebug").d(" cursor count: ${cursor.count}")
        if(cursor != null) {
            cursor.moveToFirst()
            for (i in 0..cursor.count-1) {
                Timber.tag("hlcDebug").d(" i: $i")
                Timber.tag("hlcDebug")
                    .d("cursor : ${cursor.getString(cursor.getColumnIndex("date"))} / ${cursor.getInt(cursor.getColumnIndex("sign"))}")
                cursor.moveToNext()
            }
            cursor.close()
        } else
            Timber.tag("hlcDebug").d(" selectSql is null")

        //SELECT * FROM dateTable
        for (i in mDate.value!!.toList().indices){

            if (mDate.value!![i].year ==  calendarTime.get(Calendar.YEAR) && mDate.value!![i].month ==  calendarTime.get(Calendar.MONTH)+1 && mDate.value!![i].date == calendarTime.get(Calendar.DAY_OF_MONTH)) {
                mDate.value!![i].signed = true
                Timber.tag("hlcDebug").d("date : ${mDate.value!![i].date}")
                changeDate.value = true
            }
        }

    }
}