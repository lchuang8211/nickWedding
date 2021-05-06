package com.nick.wedding.merryme

import android.app.Application
import android.content.ContentValues
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nick.wedding.data.DateSign
import com.nick.wedding.database.WeddingOpenHelper
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MerryMeViewModel(application: Application) : AndroidViewModel(application) {

    val changeDate = MutableLiveData<Boolean>(false)
    val signYet = MutableLiveData<Boolean>(false)
    val mDate = MutableLiveData<MutableList<DateSign>>()
    val initDateList = mutableListOf<DateSign>()
    val sqlHelper = WeddingOpenHelper(application)
    val selectSql = sqlHelper.readableDatabase
    val dateTable = "dateTable"
    val tableDate = "date"
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
        cMonth = calendarTime.get(Calendar.MONTH) + 1
        cWMonth = calendarTime.get(Calendar.WEEK_OF_MONTH)
        cDWeek = calendarTime.get(Calendar.DAY_OF_WEEK)
        today = dateFormat.format(calendarTime.time)
        Timber.tag("hlcDebug").d(" today: $today")
    }

    /** 取得當月天數(有算閏年) */
    fun getDays(year: Int, month: Int): Int = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        else -> if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0)) 29
                else 28
    }

    /** 讀取舊資料 */
    fun getHoldMonth(year: Int = cYear, month: Int = cMonth){
        initDateList.clear()

        val nowCl = Calendar.getInstance()
        nowCl.time = dateFormat.parse("$year-$month-1")!!
        var nullWeek = nowCl.get(Calendar.DAY_OF_WEEK)-1

        val cursor = selectSql.rawQuery("select * from $dateTable where $tableDate like '$year-${month.toString().padStart(2, '0')}%' order by $tableDate ASC", null)
        cursor.moveToFirst()
        for (i in 0..getDays(year, month)-1) {
            if (cursor.count > 0 && cursor.position < cursor.count) {
                val year = cursor.getString(cursor.getColumnIndex("date")).split("-")[0].toInt()
                val month = cursor.getString(cursor.getColumnIndex("date")).split("-")[1].toInt()
                val day = cursor.getString(cursor.getColumnIndex("date")).split("-")[2].toInt()
                if(i+1 == day) {
                    val nowCl = Calendar.getInstance()
                    val date = dateFormat.parse(cursor.getString(cursor.getColumnIndex("date")))
                    nowCl.time = date!!
                    val dWeek = nowCl.get(Calendar.DAY_OF_WEEK)-1
                    Timber.tag("hlcDebug")
                        .d("cursor : ${cursor.getString(cursor.getColumnIndex("date"))} / ${cursor.getInt(cursor.getColumnIndex("sign"))}")
                    initDateList.add(
                        DateSign(
                            year = year,
                            month = month,
                            date = day,
                            week = dWeek,
                            picture = 0,
                            signed = cursor.getInt(cursor.getColumnIndex("sign")) == 1
                        )
                    )

                    cursor.moveToNext()
                } else {
                    initDateList.add(
                        DateSign(
                            year = -1,
                            month = -1,
                            date = i+1,
                            week = -1,
                            picture = -1,
                            signed = false
                        )
                    )
                }
            } else {
                initDateList.add(
                    DateSign(
                        year = -1,
                        month = -1,
                        date = i+1,
                        week = nullWeek%7,
                        picture = -1,
                        signed = false
                    )
                )
                nullWeek += 1
            }
        }
        Timber.tag("hlcDebug").d("initDateList : $initDateList")
        mDate.value = initDateList
        changeDate.value = true
    }

    /** 今日簽到 */
    fun signToday() {

        /**
         * 1. 搜尋 DB
         * 2. 插入 DB
         * */

        //SELECT * FROM dateTable

        selectSql.rawQuery("select sign from $dateTable where $tableDate =?", arrayOf("$today"))?.let {
            if(it.count == 0) {
                val writeSql = sqlHelper.writableDatabase
                val value = ContentValues()
                value.put("date", "$today")
                value.put("sign", true)
                writeSql.insertOrThrow(dateTable,null, value)
                getHoldMonth(cYear, cMonth)
                signYet.value = true
            } else {
                signYet.value = false
            }

        }

    }

}