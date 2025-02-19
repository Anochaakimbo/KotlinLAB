package com.example.lab11_kotlin.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION) {
    companion object {
        private val DB_NAME = "StudentDB"
        private val DB_VERSION = 1
        private val TABLE_NAME = "student"
        private val COLUMN_ID = "id"
        private val COLUMN_NAME = "name"
        private val COLUMN_GENDER = "gender"
        private val COLUMN_AGE = "age"
        private val sqliteHelper: DatabaseHelper? = null

        @Synchronized
        fun getInstance(c: Context): DatabaseHelper {
            if (sqliteHelper == null) {
                return DatabaseHelper(c.applicationContext)
            } else {
                return sqliteHelper
            }
        }
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($COLUMN_ID TXT PRIMARY KEY, " +
                "$COLUMN_NAME TEXT, " + "$COLUMN_GENDER TEXT, " + "$COLUMN_AGE INTEGER)"
        db?.execSQL(CREATE_TABLE)

        val sqlInsert = "INSERT INTO $TABLE_NAME VALUES('650000000-1','Alice','Female',20)"
        db?.execSQL(sqlInsert)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAllStudents():ArrayList<Student>{
        val studentList = ArrayList<Student>()
        val db = readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        }catch (e: SQLiteException){
            onCreate(db)
            return ArrayList()
        }

        var id :String
        var name :String
        var gender :String
        var age :Int

        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast){
                id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER))
                age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE))
                studentList.add(Student(id,name,gender,age))
                cursor.moveToNext()
            }
        }

        db.close()
        return studentList
    }

    @SuppressLint("Range")
    fun getStudent(std_id: String): Student? {
        var student = Student("","","",0)
        val db = readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(
                "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID LIKE '%' || ? || '%'",
                arrayOf(std_id)
            )
        }catch (e: SQLiteException){
            onCreate(db)
            return null
        }
        var id :String
        var name :String
        var gender :String
        var age :Int
        if(cursor!!.moveToFirst()){
                id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER))
                age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE))
                student = Student(id,name,gender,age)
            }
        db.close()
        return student
    }

    fun insertStudent(std:Student):Long{
        val db = writableDatabase
        val value = ContentValues()
        value.put(COLUMN_ID,std.std_id)
        value.put(COLUMN_NAME,std.std_name)
        value.put(COLUMN_GENDER,std.std_gender)
        value.put(COLUMN_AGE,std.std_age)
        val success = db.insert(TABLE_NAME,  null,value)
        db.close()
        return success
    }

    fun updateStudent(std:Student):Int{
        val db = writableDatabase
        val value = ContentValues()
        value.put(COLUMN_NAME,std.std_name)
        value.put(COLUMN_GENDER,std.std_gender)
        value.put(COLUMN_AGE,std.std_age)
        val success = db.update(TABLE_NAME,value,  "$COLUMN_ID = ?", arrayOf(std.std_id))
        db.close()
        return success
    }

    fun deleteStudent(std_id:String):Int{
        val db = writableDatabase
        val success = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(std_id))
        db.close()
        return success
    }




}




