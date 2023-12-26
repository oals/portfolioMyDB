package com.example.mydb.User

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mydb.R
import com.example.mydb.StartActivity

class LogoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("userId")
        editor.commit()

        val intent = Intent(this,StartActivity::class.java)
        startActivity(intent)

    }
}