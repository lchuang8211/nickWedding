package com.nick.wedding.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class WeddingOpenHelper(context: Context) : SQLiteOpenHelper(context, name, CursorFactory, version) {

    companion object {
        val name = "kkLove.db"
        val CursorFactory = null
        val version = 1
    }

    val dateTable = "dateTable"
    val date = "date"
    val sign = "sign"
    val table_date_sql = "CREATE TABLE if not exists $dateTable($date TEXT PRIMARY KEY NOT NULL, $sign BLOB NOT NULL)"

    //建置資料庫的前置動作
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(table_date_sql)

    }
    //刪除及更新表格動作
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(table_date_sql)
        onCreate(db)
    }
}