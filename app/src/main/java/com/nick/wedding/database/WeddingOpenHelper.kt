package com.nick.wedding.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import timber.log.Timber


class WeddingOpenHelper(context: Context) : SQLiteOpenHelper(context, name, CursorFactory, version) {

    companion object {
        val name = "kkLove.db"
        val CursorFactory = null
        val version = 2
    }

    val dateTable = "dateTable"
    val date = "date"
    val sign = "sign"
    private val table_date_sql = "CREATE TABLE if not exists $dateTable($date TEXT PRIMARY KEY NOT NULL, $sign BLOB NOT NULL)"

    val exchangeTable = "exchangeTable"
    val category="category"
    val title = "title"
    val price = "price"
    val seq = "seq"
    val visibility = "visibility"
    private val table_exchange_sql = "CREATE TABLE  if not exists $exchangeTable ( $category TEXT NOT NULL, $title TEXT NOT NULL, $price INTEGER NOT NULL, $seq INTEGER NOT NULL, $visibility INTEGER NOT NULL, PRIMARY KEY($category,$seq) )"

    //建置資料庫的前置動作
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(table_date_sql)
        db?.execSQL(table_exchange_sql)
    }

    //刪除及更新表格動作
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Timber.tag("hlcDebug").d(" Version: $oldVersion -> $newVersion")
        db?.execSQL(table_date_sql)
        db?.execSQL(table_exchange_sql)
        onCreate(db)
    }
}