package com.example.mydb.DBHelper

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.mydb.DTO.DBConnectInfoDTO
import com.example.mydb.DTO.UserInfoDTO

class DBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version){


    override fun onCreate(db: SQLiteDatabase?) {

        Log.d("테이블 생성","테이블 생성")

        var sql: String = "CREATE TABLE if not exists userInfo(" +
                "userId text primary key," +
                "userNm text," +
                "userPw text," +
                "userPhone text," +
                "userEmail text," +
                "userDob text," +
                "userSex text," +
                "userSignUpDate text);"

        db?.execSQL(sql)


        var sql2: String = "CREATE TABLE if not exists DBInfo(" +
                "DBName text," +
                "userId text," +
                "DBUrl text," +
                "DBPort text," +
                "DBSid text," +
                "DBId text," +
                "DBPw text," +
                "DBSignUpDate text);"

        db?.execSQL(sql2)


    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists userInfo"
        db.execSQL(sql)

        val sql2: String = "DROP TABLE if exists DBInfo"
        db.execSQL(sql2)
        onCreate(db)
    }

    fun InsertConnectInfoCheck(dbConnectInfoDTO : DBConnectInfoDTO):Boolean {
        val db = this.writableDatabase

        val userId = dbConnectInfoDTO.userId
        val dbUrl = dbConnectInfoDTO.DBUrl


        val cursor = db.rawQuery("SELECT * FROM dbInfo where userId = '$userId' and DBUrl = '$dbUrl'",null)

        return when{
            cursor.count == 0 -> false
            else -> true
        }

    }

        fun InsertConnectInfo(dbConnectInfoDTO : DBConnectInfoDTO){

        val db = this.writableDatabase

        val query = "INSERT INTO DBInfo VALUES(?, ?, ?, ?,?, ?, ?, ?)"
        db.execSQL(
            query,
            arrayOf<Any>(
                dbConnectInfoDTO.DBName,
                dbConnectInfoDTO.userId,
                dbConnectInfoDTO.DBUrl,
                dbConnectInfoDTO.DBPort,
                dbConnectInfoDTO.DBSid,
                dbConnectInfoDTO.DBId,
                dbConnectInfoDTO.DBPw,
                dbConnectInfoDTO.DBSignUpDate
            )
        )

    }

    fun DeleteConnectInfo(DBUrl : String){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM DBInfo where DBUrl = \"${DBUrl}\"")

    }


        fun SelectConnectInfo(userId: String): Cursor {

        Log.d("asd", "정보 가져오기")

        val db = this.writableDatabase

        return db.rawQuery("SELECT * from DBInfo where userId = '$userId'", null);
    }


    fun SelectConnectInfoUrl(dbUrl: String): Cursor {

        Log.d("asd", "정보 가져오기")
        Log.d("asfasf",dbUrl)

        val db = this.writableDatabase

        return db.rawQuery("SELECT * from DBInfo where DBUrl = '$dbUrl'", null);
    }



    fun userRegister(userInfoDTO : UserInfoDTO) {
        val db = this.writableDatabase

        val query = "INSERT INTO userInfo VALUES(?, ?, ?, ?, ?, ?, ?, ?)"
        db.execSQL(
            query,
            arrayOf<Any>(
                userInfoDTO.userId,
                userInfoDTO.userNm,
                userInfoDTO.userPw,
                userInfoDTO.userPhone,
                userInfoDTO.userEmail,
                userInfoDTO.userDob,
                userInfoDTO.userSex,
                userInfoDTO.userSignUpDate
            )
        )

    }

    fun userIdCheck(userId : String) : Boolean{
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM userInfo where userId = '$userId'",null)

        println(cursor.count)


        return when{
            cursor.count > 0 -> false
            else -> true
        }

    }

    fun selectUserInfo(userId : String) : Cursor {
        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM userInfo where userId = '$userId'",null)
        cursor.moveToFirst()

        return cursor
    }


    fun userLogin(userId:String, userPw:String) : Boolean{

        val db = this.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM userInfo where userId = '$userId' and userPw = '$userPw'",null)

        return when{
            cursor.count == 0 -> false
            else -> true
        }
    }





}