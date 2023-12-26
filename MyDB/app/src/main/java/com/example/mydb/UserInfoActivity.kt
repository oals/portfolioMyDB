package com.example.mydb

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.example.mydb.DBHelper.DBHelper
import com.example.mydb.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {


    private val binding by lazy{
        ActivityUserInfoBinding.inflate(layoutInflater)
    }

    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)


        dbHelper = DBHelper(this, "mydb.db", null, 1)

        val userInfoCursor : Cursor =  dbHelper.selectUserInfo(prefs.getString("userId",null).toString())

        if(prefs.getString("userId",null) != null) {

            binding.myPageUserId.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(0))
            binding.myPageUserNm.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(1))
            binding.myPageUserPhone.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(3))
            binding.myPageUserEmail.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(4))
            binding.myPageUserDob.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(5))
            binding.myPageUserSex.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(6))
            binding.myPageUserSignUpDate.text = Editable.Factory.getInstance().newEditable(userInfoCursor.getString(7))

        }


        binding.backBtn.setOnClickListener {
            finish()
        }





    }
}