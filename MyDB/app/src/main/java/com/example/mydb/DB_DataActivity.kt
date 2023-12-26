package com.example.mydb

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import androidx.compose.ui.text.font.Typeface
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mydb.DBHelper.DBHelper
import com.example.mydb.DTO.DBConnectInfoDTO
import com.example.mydb.databinding.ActivityDbDataBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DB_DataActivity : AppCompatActivity() {

    var queue: RequestQueue? = null
    private lateinit var dbHelper: DBHelper


    private val binding by lazy{
        ActivityDbDataBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)


        if (queue == null) {
            queue = Volley.newRequestQueue(binding.root.context)
        }


        binding.selectTableName.text = intent.getStringExtra("tableName").toString()




        var columnNameList : ArrayList<String> = ArrayList()

        val dataList: ArrayList<ArrayList<String>>? = intent.getSerializableExtra("dataList") as? ArrayList<ArrayList<String>>


        if (dataList != null) {
            if (dataList.size > 1) {

                // 새 TextView 인스턴스를 생성합니다.
                val constraintSet = ConstraintSet()
                var prevId = View.NO_ID

                for(i in 0 until dataList.size) {

                    for (j in 0 until (dataList[i].size)) {

                        if (i != 0) {
                            val textView = TextView(this)
                            textView.id = View.generateViewId() // 각 View에 고유한 ID를 설정합니다.

                            var columnName = dataList.get(0).get(j) + " : "

                            columnNameList.add(dataList.get(0).get(j))

                            val spannableString = SpannableString(columnName + dataList.get(i).get(j))

                            spannableString.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0,columnName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            // TextView의 속성을 설정합니다.
                            textView.text = spannableString
                            textView.layoutParams = ConstraintLayout.LayoutParams(
                                ConstraintLayout.LayoutParams.MATCH_PARENT, // 너비
                                ConstraintLayout.LayoutParams.WRAP_CONTENT  // 높이
                            )

                            // 레이아웃에 TextView를 추가합니다.
                            binding.dataView.addView(textView)


                            // ConstraintSet에 제약 조건을 설정합니다.
                            constraintSet.clone(binding.dataView)

                            val leftMargin = 60 // 왼쪽 마진
                            constraintSet.connect(textView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, leftMargin)

                            val rightMargin = 60 // 오른쪽 마진
                            constraintSet.connect(textView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, rightMargin)

                            if (prevId == View.NO_ID) {
                                // 첫 번째 TextView는 부모에 연결합니다.
                                constraintSet.connect(textView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 60)
                            } else {
                                if(j == 0){
                                    constraintSet.connect(textView.id, ConstraintSet.TOP, prevId, ConstraintSet.BOTTOM,150)

                                }else{
                                    constraintSet.connect(textView.id, ConstraintSet.TOP, prevId, ConstraintSet.BOTTOM,15)
                                }
                            }
                            constraintSet.applyTo(binding.dataView)



                            prevId = textView.id
                        }


                        // 새 TextView 인스턴스를 생성합니다.

                    }


                }



            }else{
                binding.noDataMsg.text = "데이터 없음"
            }
        }





        binding.searchBtn.setOnClickListener {





            val build = AlertDialog.Builder(this).setView(R.layout.activity_dbsearch)
            val dialog = build.create()


            dialog.setOnShowListener {




                val spinner: Spinner = dialog.findViewById(R.id.searchSpinner)

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    columnNameList
                )

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter



                //창 닫기 버튼
                dialog.findViewById<Button>(R.id.closeButton)?.setOnClickListener { v ->
                    dialog.dismiss()
                }
                dialog.findViewById<Button>(R.id.searchDataBtn).setOnClickListener{

                    val searchDataView: ConstraintLayout? = dialog.findViewById(R.id.searchDataView)
                    searchDataView?.removeAllViews()


                    val searchData = dialog.findViewById<EditText>(R.id.searchData).text.toString()
                    val columnName: String = spinner.selectedItem.toString()
                    val tableName : String = intent.getStringExtra("tableName").toString()
                    val dbName : String = intent.getStringExtra("dbName").toString()




                    dbHelper = DBHelper(this, "mydb.db", null, 1)
                    val cursor : Cursor =  dbHelper.SelectConnectInfoUrl(intent.getStringExtra("dbUrl").toString())

                    cursor.moveToNext()

                        var dbConnectInfoDTO : DBConnectInfoDTO  = DBConnectInfoDTO(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                        )


                    //검색 시작
                    Log.d("검색 데이터",searchData)
                    Log.d("칼럼 명",columnName)
                    Log.d("테이블 명",tableName)
                    Log.d("데이버 베이스 명",dbName)

                    httpConnection(dialog,dbConnectInfoDTO,"selectData",dbName,searchData,
                        columnName,tableName)


                }


            }


            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window?.attributes)
            lp.width = 1450
            lp.height = 1200
            dialog.window?.attributes = lp

            dialog.show()


        }



    }


    private fun httpConnection(view: AlertDialog, dbConnectInfo : DBConnectInfoDTO, apiName : String,
                               dbName:String,
                               searchData : String, columnName : String, tableName: String) {

        var dbUrl = ""
        if(dbConnectInfo.DBName.equals("MariaDB")){
            dbUrl = "jdbc:mariadb://" + dbConnectInfo.DBUrl + ":" + dbConnectInfo.DBPort + "/";
        }else{
            dbUrl = "jdbc:oracle:thin:@" + dbConnectInfo.DBUrl + ":" + dbConnectInfo.DBPort + ":" + dbConnectInfo.DBSid;
        }

        val url: String = "http://ec2-43-201-18-102.ap-northeast-2.compute.amazonaws.com:8100/${apiName}" +
                "?url=${dbUrl}" +
                "&id=${dbConnectInfo.DBId}" +
                "&pswd=${dbConnectInfo.DBPw}" +
                "&tableName=${tableName}" +
                "&dbName=${dbName}" +
                "&searchData=${searchData}" +
                "&columnName=${columnName}";


        Log.d("url테스트",url)


        val jsonRequest: JsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->
                    parseData3(response, view)
            }, { error ->

                val build = AlertDialog.Builder(view.context)
                build.setTitle("연결 실패")
                build.setMessage(
                    "잠시 후에 다시 시도해주세요."
                )
                build.setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, id ->
                    })

                build.show()

            }
            )


        queue?.add(jsonRequest)


    }




    private fun parseData3(jsonObject: JSONObject, alertDialog: AlertDialog) {

        try {
            val myTableList = jsonObject.getJSONArray("tableList")

            var dataList : ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()

            //칼럼명 얻기
            val columnArray = myTableList[0] as JSONArray
            var columnlist : ArrayList<String> = ArrayList<String>()
            for(i in 0 until columnArray.length()) {
                columnlist.add(columnArray.getString(i))
            }
            dataList.add(columnlist)

            if(myTableList.length() > 1){

                for(i in 1 until myTableList.length()){
                    //데이터 얻기
                    val dataArray = myTableList[i] as JSONArray

                    var list : ArrayList<String> = ArrayList<String>()
                    for(i in 0 until dataArray.length()) {
                        list.add(dataArray.getString(i))
                    }
                    dataList.add(list)
                }


            }else{
                Log.d("데이터 메시지","데이터 없음")
            }




            if (dataList != null) {
                if (dataList.size > 1) {

                    // 새 TextView 인스턴스를 생성합니다.
                    val constraintSet = ConstraintSet()
                    var prevId = View.NO_ID

                    for(i in 0 until dataList.size) {

                        for (j in 0 until (dataList[i].size)) {

                            if (i != 0) {
                                val textView = TextView(this)
                                textView.id = View.generateViewId() // 각 View에 고유한 ID를 설정합니다.

                                var text = dataList.get(0).get(j) + " : "
                                val spannableString = SpannableString(text + dataList.get(i).get(j))

                                spannableString.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0,text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                                // TextView의 속성을 설정합니다.
                                textView.text = spannableString
                                textView.layoutParams = ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.MATCH_PARENT, // 너비
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT  // 높이
                                )

                                // 레이아웃에 TextView를 추가합니다.
                                alertDialog.findViewById<ConstraintLayout>(R.id.searchDataView).addView(textView)


                                // ConstraintSet에 제약 조건을 설정합니다.
                                constraintSet.clone(alertDialog.findViewById<ConstraintLayout>(R.id.searchDataView))

                                val leftMargin = 60 // 왼쪽 마진
                                constraintSet.connect(textView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, leftMargin)

                                val rightMargin = 60 // 오른쪽 마진
                                constraintSet.connect(textView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, rightMargin)

                                if (prevId == View.NO_ID) {
                                    // 첫 번째 TextView는 부모에 연결합니다.
                                    constraintSet.connect(textView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 60)
                                } else {
                                    if(j == 0){
                                        constraintSet.connect(textView.id, ConstraintSet.TOP, prevId, ConstraintSet.BOTTOM,150)

                                    }else{
                                        constraintSet.connect(textView.id, ConstraintSet.TOP, prevId, ConstraintSet.BOTTOM,15)
                                    }
                                }
                                constraintSet.applyTo(alertDialog.findViewById<ConstraintLayout>(R.id.searchDataView))



                                prevId = textView.id
                            }


                            // 새 TextView 인스턴스를 생성합니다.

                        }


                    }



                }else{

                    val build = AlertDialog.Builder(alertDialog.context)
                    build.setTitle("검색 실패")
                    build.setMessage(
                        "검색 결과가 없습니다."
                    )
                    build.setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, id ->
                        })

                    build.show()



                }
            }





        } catch (e: JSONException) {
            e.printStackTrace()
        }



    }


}