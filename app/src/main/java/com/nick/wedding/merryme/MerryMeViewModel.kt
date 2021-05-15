package com.nick.wedding.merryme

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nick.wedding.data.DateSign
import com.nick.wedding.data.ExchangeItem
import com.nick.wedding.data.ExchangeRecord
import com.nick.wedding.database.WeddingOpenHelper
import com.nick.wedding.surpport.ExchangeManager
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
    val exchangeTable = "exchangeTable"
    val tableDate = "date"
    val sign = "sign"
    val seq = "seq"
    val category = "category"
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
        initExchangeList()
    }

    val _exchangeList = mutableListOf<ExchangeItem>()
    val exchangeList = MutableLiveData<List<ExchangeItem>>(_exchangeList)
    var exchangePage = 1

    var exchangeItemTitle = ""
    var exchangePrice = 0
    val exchangeSuccess = MutableLiveData<Boolean>(null)

    val _exchangeRecordList = mutableListOf<ExchangeRecord>()
    val exchangeRecordList = MutableLiveData<List<ExchangeRecord>>(_exchangeRecordList)

    fun initExchangeList(){
        selectSql.rawQuery("select 1 from $exchangeTable",null)?.let {
            /** 預設 3區，各10項 */
            if(it.count > 0 && it.count == 30)
                return
            else{
                val category = arrayOf<String>("伙食區","生活區","羞羞區")
                for (item in category.indices) {
                    val mType = category[item]
                    for (seq in 1..10) {
                        val pair = ExchangeManager.getExchangeDefaultList("item_$seq", item+1)?:Pair("",0)
                        val value = ContentValues()
                        value.put("category", mType)
                        value.put("seq", seq)
                        value.put("title", pair.first)
                        value.put("price", pair.second)
                        value.put("visibility", !pair.first.equals("") && !pair.first.equals("--"))
                        writeSql.insertOrThrow(exchangeTable, null, value)
                    }
                }
            }
        }

    }

    fun getExchangePage(mType:String = "伙食區"){
        _exchangeList.clear()
        selectSql.rawQuery("select * from $exchangeTable where $category = '$mType' order by $seq asc",null)?.let {
            it.moveToFirst()
            for (i in 0 .. (it.count-1)){
                _exchangeList.add(ExchangeItem(
                    category = it.getString(it.getColumnIndex("category")),
                    seq = it.getInt(it.getColumnIndex("seq")),
                    title = it.getString(it.getColumnIndex("title")),
                    price = it.getInt(it.getColumnIndex("price")),
                    visibility = it.getInt(it.getColumnIndex("visibility")) == 1
                ))
                it.moveToNext()
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
            totalCookie = it.count
            totalCookie = 200
        }
    }

    fun selectCurrentSign(year:Int, month:Int): Int{
        var tempCookie = 0
        selectSql.rawQuery("select * from dateTable where date like '$year-${month.toString().padStart(2, '0')}%' and sign != 0 ", null)?.let {
            tempCookie = it.count
        }
        return tempCookie
    }

    fun selectDate(){
        selectSql.rawQuery("select $tableDate from $dateTable", null)?.let {
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

    /**
     * @param Int 種類
     * @param Int 第幾項
     * @param String 項目名稱
     * @param Int 項目價碼
     * */
    fun insertExchangeItem(category:String, seq: Int, title: String, oldTitle: String, price: Int){

        val value = ContentValues()
        value.put("category", category)
        value.put("title", title)
        value.put("price", price)
        value.put("seq", seq)
        value.put("visibility", 1)

        selectSql.rawQuery("select 1 from $exchangeTable where category = '$category' and seq ='$seq' ", null)?.let {
            if(it.count > 0) {
                writeSql.update(exchangeTable,value,"category = ? and seq = ?", arrayOf(category,"$seq"))
            }
        }

    }

}
