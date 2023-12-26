package com.example.mydb

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.mydb.databinding.ActivityDbsearchBinding


class DBSearchActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityDbsearchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



    }


}