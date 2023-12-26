package com.example.mydb.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mydb.Adapter.DBListAdapter
import com.example.mydb.Adapter.MyDBListAdapter
import com.example.mydb.Adapter.MyTableListAdapter
import com.example.mydb.Adapter.SwipeHelper
import com.example.mydb.DBHelper.DBHelper
import com.example.mydb.DTO.DBConnectInfoDTO
import com.example.mydb.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MyDbFragment : Fragment() {



    private lateinit var dbHelper: DBHelper


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View = inflater.inflate(R.layout.fragment_my_db, container, false)

        val prefs : SharedPreferences = this.requireContext().getSharedPreferences("Prefs", Context.MODE_PRIVATE)



        val dbList = ArrayList<DBConnectInfoDTO>()


        dbHelper = DBHelper(this.requireContext(), "mydb.db", null, 1)

        val cursor : Cursor =  dbHelper.SelectConnectInfo(prefs.getString("userId","null").toString())


        while(cursor.moveToNext()){

            var dbConnectInfoDTO : DBConnectInfoDTO = DBConnectInfoDTO(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
            )
            dbList.add(dbConnectInfoDTO)
        }


        view.findViewById<RecyclerView>(R.id.dbList).layoutManager = LinearLayoutManager(this.requireContext())//수평, 수직 스크롤
        view.findViewById<RecyclerView>(R.id.dbList).adapter = DBListAdapter(dbList)
        view.findViewById<RecyclerView>(R.id.dbList).addItemDecoration(DividerItemDecoration(
            this.requireContext(), LinearLayoutManager.VERTICAL))


        val swipeHelper = SwipeHelper()
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(view.findViewById<RecyclerView>(R.id.dbList))



        return view
    }

}