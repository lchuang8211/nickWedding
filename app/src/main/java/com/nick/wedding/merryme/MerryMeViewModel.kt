package com.nick.wedding.merryme

import android.app.Application
import android.content.ContentValues
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nick.wedding.data.DateSign
import com.nick.wedding.data.ExchangeRecord
import com.nick.wedding.database.WeddingOpenHelper
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MerryMeViewModel(application: Application) : AndroidViewModel(application) {

    var totalDate = 0
    var totalCookie = 0

    val changeDate = MutableLiveData<Boolean>(false)
    val signYet = MutableLiveData<Int>(0)
    val exchangeClick = MutableLiveData<Boolean>(false)
    val initDateList = mutableListOf<DateSign>()
    val sqlHelper = WeddingOpenHelper(application)
    val selectSql = sqlHelper.readableDatabase
    val writeSql = sqlHelper.writableDatabase
    val dateTable = "dateTable"
    val exchangeRecordTable = "exchangeRecordTable"
    val tableDate = "date"
    val sign = "sign"
    val seq = "seq"
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
        selectSign()
        selectDate()
    }

    val _exchangeList = mutableListOf<String>()
    val exchangeList = MutableLiveData<List<String>>(_exchangeList)
    var exchangePage = 1

    var exchangeItemTitle = ""
    var exchangePrice = 0
    val exchangeSuccess = MutableLiveData<Boolean>(null)

    val _exchangeRecordList = mutableListOf<ExchangeRecord>()
    val exchangeRecordList = MutableLiveData<List<ExchangeRecord>>(_exchangeRecordList)

    fun getExchangePage(page:Int = 1){
        _exchangeList.clear()
        this.exchangePage = page

        // search db

        // insert db

        when(page){
            1 -> {
                _exchangeList.add("item_1")
                _exchangeList.add("item_2")
                _exchangeList.add("item_3")
                _exchangeList.add("item_4")
                _exchangeList.add("item_5")
                _exchangeList.add("item_6")
                _exchangeList.add("item_7")
            }
            2 -> {
                _exchangeList.add("item_1")
                _exchangeList.add("item_2")
                _exchangeList.add("item_3")
                _exchangeList.add("item_4")
                _exchangeList.add("item_5")
                _exchangeList.add("item_6")
                _exchangeList.add("item_7")
            }
            3 -> {
                _exchangeList.add("item_1")
                _exchangeList.add("item_2")
                _exchangeList.add("item_3")
                _exchangeList.add("item_4")
                _exchangeList.add("item_5")
                _exchangeList.add("item_6")
            }
        }

        exchangeList.value = _exchangeList

    }

    /** 取得當月天數(有算閏年) */
    fun getDays(year: Int, month: Int): Int = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        else -> if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0)) 29
                else 28
    }

    fun scrollOut(){
        changeDate.value = true
    }

    /** 讀取舊資料 */
    fun getHoldMonth(year: Int = cYear, month: Int = cMonth) : List<DateSign> {
        initDateList.clear()

        val nowCl = Calendar.getInstance()
        nowCl.time = dateFormat.parse("$year-$month-1")!!
        var nullWeek = nowCl.get(Calendar.DAY_OF_WEEK)-1
        for (i in 0..nullWeek-1) {
            initDateList.add(DateSign(
                year = -1,
                month = -1,
                date = -1,
                week = -1,
                picture = -1,
                signed = false
            ))
        }
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
//        mDate.value = initDateList
//        changeDate.value = true

        return initDateList
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
                val value = ContentValues()
                value.put("date", "$today")
                value.put("sign", 1)
                writeSql.insertOrThrow(dateTable,null, value)
                getHoldMonth(cYear, cMonth)
                selectSign()
                selectDate()
                changeDate.value = true
                signYet.value = 1
            } else
                signYet.value = -1
        }

    }

    fun selectSign(){
        selectSql.rawQuery("select $sign from $dateTable where $sign = 1 ", null)?.let {
            Timber.tag("hlcDebug").d("總 sign : ${it.count}")
            totalCookie = it.count

            totalCookie = 200
        }
    }

    fun selectCurrentSign(year:Int, month:Int): Int{
        var tempCookie = 0
        selectSql.rawQuery("select * from dateTable where date like '$year-${month.toString().padStart(2, '0')}%' and sign != 0 ", null)?.let {
            Timber.tag("hlcDebug").d("總 sign : ${it.count}")
            tempCookie = it.count
        }
        return tempCookie
    }

    fun selectDate(){
        selectSql.rawQuery("select $tableDate from $dateTable", null)?.let {
            Timber.tag("hlcDebug").d("總 date : ${it.count}")
            totalDate = it.count
        }
    }

    fun checkCookie(){
        /** 判斷是否 持有量 > 消費金額 */
        if (totalCookie > exchangePrice) {
            exchangeSuccess.value = true
            val value = ContentValues()
            value.put("date", today)
            value.put("title", exchangeItemTitle)
            value.put("price", exchangePrice?:0)
            writeSql.insertOrThrow(exchangeRecordTable,null, value)
            totalCookie -= exchangePrice
        } else {
            exchangeSuccess.value = false
        }
        changeDate.value = true

    }

    fun getExchangeRecord(){
        _exchangeRecordList.clear()
        selectSql.rawQuery("select * from exchangeRecordTable order by $seq", null).let {
            if(it.count>0){
                it.moveToFirst()
                for (i in 0 until it.count) {
                    _exchangeRecordList.add(
                        ExchangeRecord(
                            seq = it.getString(it.getColumnIndex("seq")).toInt(),
                            date = it.getString(it.getColumnIndex("date")),
                            title = it.getString(it.getColumnIndex("title")),
                            price = it.getString(it.getColumnIndex("price")).toInt()
                        )
                    )
                    it.moveToNext()
                }
                Timber.tag("hlcDebug").d("_exchangeRecordList : $_exchangeRecordList")
                exchangeRecordList.value = _exchangeRecordList
            }
        }
    }

}