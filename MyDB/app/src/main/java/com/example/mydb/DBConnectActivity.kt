package com.example.mydb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.mydb.databinding.ActivityDbconnectBinding

class DBConnectActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityDbconnectBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val spinner: Spinner = binding.DBSpinner

        ArrayAdapter.createFromResource(
            this,

            // R.array.gender_array 는 2번에서 설정한 string-array 태그의 name 입니다.
            R.array.DB_array,

            // android.R.layout.simple_spinner_dropdown_item 은 android 에서 기본 제공
            // 되는 layout 입니다. 이 부분은 "선택된 item" 부분의 layout을 결정합니다.
            android.R.layout.simple_spinner_dropdown_item

        ).also { adapter ->

            // android.R.layout.simple_spinner_dropdown_item 도 android 에서 기본 제공
            // 되는 layout 입니다. 이 부분은 "선택할 item 목록" 부분의 layout을 결정합니다.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }







    }
}