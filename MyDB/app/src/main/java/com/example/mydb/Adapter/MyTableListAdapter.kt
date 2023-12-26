package com.example.mydb.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.mydb.R
import org.json.JSONArray

class MyTableListAdapter(val context: Context, val MyTableList : JSONArray) : BaseAdapter() {
    override fun getCount(): Int {
        return MyTableList.length()
    }

    override fun getItem(position: Int): Any {
        return MyTableList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {

        val view : View = LayoutInflater.from(context).inflate(R.layout.mytable_list_item,null)

        val myTableName = view.findViewById<TextView>(R.id.myTableName)

        val myTableList = MyTableList[position]


        myTableName.text = myTableList.toString()

        return view
    }


}