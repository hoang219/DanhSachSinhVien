package com.hoangnn.danhsachsinhvien.controller

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.hoangnn.danhsachsinhvien.model.Student
import java.text.SimpleDateFormat

class StudentCtr(context: Context) {
    companion object{
        private const val TBL_STUDENT = "tbl_student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val BIRTHDAY = "birthday"
        private const val PHONE_NUMBER = "phone_number"
        private const val SPECIALIZED = "specialized"
    }

    private val database = Database(context)
    private val format = SimpleDateFormat("yyyy-MM-dd")

    fun addStudent(student: Student): Int{
        val result = checkPhoneNumber(student.phonenumber)

        if (result == 0){
            val db = database.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(NAME, student.name)
            contentValues.put(BIRTHDAY, format.format(student.birthday))
            contentValues.put(PHONE_NUMBER, student.phonenumber)
            contentValues.put(SPECIALIZED, student.specialized)

            db.insert(TBL_STUDENT, null, contentValues).toInt()
            db.close()
        }
        return result
    }

    @SuppressLint("Range")
    fun searchStudent(data: String, specialized: String): ArrayList<Student>{
        val listStudent = ArrayList<Student>()
        val db = database.readableDatabase
        val cursor: Cursor?
        try{
            cursor = if(data.isEmpty()){
                val selection = "$SPECIALIZED LIKE ?"
                val selectionArgs: Array<String> = arrayOf("%$specialized%")
                db.query(TBL_STUDENT, null, selection, selectionArgs, null, null, null, null)
            }else {
                val selection = "$SPECIALIZED LIKE ? AND ($NAME LIKE ? OR $PHONE_NUMBER LIKE ? OR $ID LIKE ?)"
                val selectionArgs: Array<String> = arrayOf("%$specialized%", "%$data%", "%$data%", "%$data%")
                db.query(TBL_STUDENT, null, selection, selectionArgs, null, null, null, null)
            }
        }catch (e: Exception){
            e.printStackTrace()
            return ArrayList()
        }

        if (cursor.moveToNext()){
            do {
                val student = Student(cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(NAME)),
                    format.parse(cursor.getString(cursor.getColumnIndex(BIRTHDAY))),
                    cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(SPECIALIZED)))
                listStudent.add(student)
            }while (cursor.moveToNext())
        }
        db.close()
        return listStudent
    }

    fun deleteStudent(student: Student): Int{
        val db = database.writableDatabase
        val selection = "$ID = ?"
        val selectionArgs = arrayOf(student.id.toString())
        val result = db.delete(TBL_STUDENT,selection, selectionArgs)
        db.close()
        return result
    }

    fun updateStudent(student: Student): Int{
        var result = checkPhoneNumberForUpdate(student)

        if (result == 0){
            val db = database.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(NAME, student.name)
            contentValues.put(BIRTHDAY, format.format(student.birthday))
            contentValues.put(PHONE_NUMBER, student.phonenumber)
            contentValues.put(SPECIALIZED, student.specialized)

            val selection = "$ID = ?"
            val selectionArgs = arrayOf(student.id.toString())
            db.update(TBL_STUDENT, contentValues, selection, selectionArgs)
        }

        return result
    }

    private fun checkPhoneNumber(phoneNumber: String): Int{
        val db = database.readableDatabase
        val cursor: Cursor?
        val exist = 1
        val dnexist = 0
        val error = -1
        try{
            cursor = if(phoneNumber.isEmpty()){
                db.close()
                return error
            }else {
                val selection = "$PHONE_NUMBER = ?"
                val selectionArgs: Array<String> = arrayOf(phoneNumber)
                db.query(TBL_STUDENT, null, selection, selectionArgs, null, null, null, null)
            }
        }catch (e: Exception){
            e.printStackTrace()
            return error
        }
        if (cursor.moveToNext()) {
            db.close()
            return exist
        } else {
            db.close()
            return dnexist
        }
    }

    private fun checkPhoneNumberForUpdate(student: Student): Int{
        var result = -1;
        val db = database.readableDatabase
        if (!student.phonenumber.isEmpty()){
            val cursor: Cursor?
            try{
                val selection = "$PHONE_NUMBER = ?"
                val selectionArgs: Array<String> = arrayOf(student.phonenumber)
                cursor = db.query(TBL_STUDENT, null, selection, selectionArgs, null, null, null, null)
            }catch (e: Exception){
                e.printStackTrace()
                return result
            }
            if (cursor.moveToNext()){
                result = 0
                do {
                    if (cursor.getInt(0) != student.id)
                        result = 1
                }while (cursor.moveToNext())
            }
        }
        db.close()
        return result
    }

    fun sortStudent(listStudent: ArrayList<Student>, choosed: Int): ArrayList<Student>{
        when(choosed){
            0->{
                listStudent.sortWith(compareBy{
                    it.name.lowercase()
                })
            }
            1->{
                listStudent.sortWith(compareByDescending {
                    it.name.lowercase()
                })
            }
            2->{
                listStudent.sortWith(compareBy{
                    it.phonenumber
                })
            }
            3->{
                listStudent.sortWith(compareByDescending {
                    it.phonenumber
                })
            }
        }
        return listStudent
    }

}