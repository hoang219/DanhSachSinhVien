package com.hoangnn.danhsachsinhvien.controller

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    lateinit var db: SQLiteDatabase

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "sinhvien.db"
        private const val TBL_STUDENT = "tbl_student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val BIRTHDAY = "birthday"
        private const val PHONE_NUMBER = "phone_number"
        private const val SPECIALIZED = "specialized"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null){
            this.db = db
        }

        val createTblStudent = ("CREATE TABLE " + TBL_STUDENT + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT, "
                + BIRTHDAY + " DATE, "
                + PHONE_NUMBER + " TEXT, "
                + SPECIALIZED + " TEXT" + ")"
                )

        db?.execSQL(createTblStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_STUDENT")
        onCreate(db)
    }
}