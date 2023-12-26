package com.example.mydb.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mydb.DTO.DBConnectInfoDTO
import com.example.mydb.R
import org.json.JSONArray

class MyDBListAdapter(val context: Context, val MyDBList : JSONArray) : BaseAdapter() {

    override fun getCount(): Int {
        return MyDBList.length()
    }

    override fun getItem(position: Int): Any {
        return MyDBList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {


        val view :View = LayoutInflater.from(context).inflate(R.layout.mydb_list_item,null)

        val mydbName = view.findViewById<TextView>(R.id.myDBName)


        val myDBList = MyDBList[position]


        mydbName.text = myDBList.toString()

        return view

    }

}