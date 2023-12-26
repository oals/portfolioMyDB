package com.example.mydb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mydb.User.LoginActivity
import com.example.mydb.User.RegisterActivity
import com.example.mydb.databinding.ActivityRegisterBinding
import com.example.mydb.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        if(!prefs.getString("userId",null).isNullOrEmpty()){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }



        binding.registerButton.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }


        binding.loginButton.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }








    }
}