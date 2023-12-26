package com.example.mydb.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mydb.DBHelper.DBHelper
import com.example.mydb.DB_DataActivity
import com.example.mydb.DTO.DBConnectInfoDTO
import com.example.mydb.Fragment.MyDbFragment
import com.example.mydb.R
import com.example.mydb.databinding.ActivityDbListItemBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class DBListAdapter(val DBList: ArrayList<DBConnectInfoDTO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var queue: RequestQueue? = null
    private lateinit var dbHelper: DBHelper

        class MyViewHolder(val binding: ActivityDbListItemBinding) :RecyclerView.ViewHolder(binding.root){
            private var isClamped = false

            fun setClamped(clampValue: Boolean) {
                isClamped = clampValue
            }

            fun getClamped(): Boolean {
                return isClamped
            }





        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return MyViewHolder(ActivityDbListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }



        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val binding_sub = (holder as MyViewHolder).binding

            binding_sub.dbName.text = DBList[position].DBName
            binding_sub.dbPort.text ="Port : " + DBList[position].DBPort
            binding_sub.dbUrl .text = "Url : " +DBList[position].DBUrl
            binding_sub.dbId.text = "Id : " +DBList[position].DBId
//
            if(binding_sub.dbName.text.equals("MariaDB")){
                binding_sub.dbIcon.setImageResource(R.mipmap.maria_db_icon_foreground)
            }else if(binding_sub.dbName.text.equals("OracleDB")){
                binding_sub.dbIcon.setImageResource(R.mipmap.oracle_db_icon_foreground)
            }else{
                binding_sub.dbIcon.setImageResource(R.mipmap.maria_db_icon_foreground) //DBNAME 변경 작업 후 삭제
            }


            if (queue == null) {
                queue = Volley.newRequestQueue(binding_sub.root.context)
            }

            val layout1 = binding_sub.eraseItemView
            val layout2 = binding_sub.swipeView

                layout1.setOnClickListener {
                        Log.d("1번 클릭","삭제부분" + DBList[position].DBUrl)

                    val build = AlertDialog.Builder(binding_sub.root.context)
                    build.setTitle("접속 정보 삭제")
                    build.setMessage(
                        "해당 데이터베이스 정보를 삭제하겠습니까?"
                    )
                    build.setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, id ->

                            dbHelper = DBHelper(binding_sub.root.context, "mydb.db", null, 1)
                            dbHelper.DeleteConnectInfo(DBList[position].DBUrl)
                            val fragmentActivity = binding_sub.root.context as FragmentActivity
                            val fragmentManager = fragmentActivity.supportFragmentManager

                            fragmentManager.beginTransaction().replace(R.id.DBFragmentView, MyDbFragment())
                                .commit()
                        })

                    build.setNegativeButton("취소",
                        DialogInterface.OnClickListener{ dialog, id ->


                        })
                    build.setCancelable(false);
                    build.show()
                }

                layout2.setOnClickListener {
                        Log.d("2번 클릭", "리스트부분" + position.toString())

                            val build = AlertDialog.Builder(binding_sub.root.context).setView(R.layout.mydb_list)
                            val dialog = build.create()


                            dialog.setOnShowListener {

                                var apiName = "";
                                if(DBList[position].DBName.equals("MariaDB")){
                                    apiName= "getDataBase";
                                }else{
                                    apiName= "getTable";
                                }


                                httpConnection(dialog, DBList[position],apiName,"","")

                            }

                            val lp = WindowManager.LayoutParams()
                            lp.copyFrom(dialog.window?.attributes)
                            lp.width = 1450
                            lp.height = 2000
                            dialog.window?.attributes = lp

                            dialog.show()

                        }
        }

        override fun getItemCount(): Int {
            return DBList.size
        }



        private fun httpConnection(view: AlertDialog, dbConnectInfo : DBConnectInfoDTO,apiName : String, dbName : String,tableName: String) {

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
                    "&dbName=${dbName}" +
                    "&tableName=${tableName}";

            Log.d("url테스트",url)


            val jsonRequest: JsonObjectRequest =
                JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, { response ->
                    if(apiName.equals("getDataBase")){
                        parseData(response, view,dbConnectInfo)
                    }else if(apiName.equals("getTable")){
                        parseData2(response, view,dbConnectInfo,dbName)
                    }else if(apiName.equals("getTableInfo")){
                        parseData3(response, view,dbConnectInfo,dbName,tableName)
                    }else{
                        Log.d("오류","오류")
                    }

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




        private fun parseData(jsonObject: JSONObject, alertDialog: AlertDialog, dbConnectInfo : DBConnectInfoDTO) {

            try {

                val myDBList = jsonObject.getJSONArray("DBList")


                val adapter = MyDBListAdapter(alertDialog.context, myDBList)
                alertDialog.findViewById<ListView>(R.id.myDBList).adapter = adapter


                alertDialog.findViewById<ListView>(R.id.myDBList).setOnItemClickListener{ adapterView, view, i, l ->
                    val clickDB = myDBList[i]


                    httpConnection(alertDialog, dbConnectInfo ,"getTable", clickDB.toString()," ")


                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }


        private fun parseData2(jsonObject: JSONObject, alertDialog: AlertDialog, dbConnectInfo: DBConnectInfoDTO, dbName: String) {

            try {

                val myTableList = jsonObject.getJSONArray("tableList")

                alertDialog.findViewById<TextView>(R.id.navText).text = "Table 선택"

                val adapter = MyTableListAdapter(alertDialog.context, myTableList)
                alertDialog.findViewById<ListView>(R.id.myDBList).adapter = adapter

                alertDialog.findViewById<ListView>(R.id.myDBList).setOnItemClickListener{ adapterView, view, i, l ->
                    httpConnection(alertDialog, dbConnectInfo ,"getTableInfo", dbName,myTableList[i].toString())
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }



        }




        private fun parseData3(jsonObject: JSONObject, alertDialog: AlertDialog, dbConnectInfo: DBConnectInfoDTO, dbName: String, tableName: String) {

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

                //새 엑티비티 생성 -> 프래그먼트 리스트뷰 통해서 데이터 뿌려주기



            val intent = Intent(alertDialog.context,DB_DataActivity::class.java)
            intent.putExtra("dataList",dataList)
            intent.putExtra("tableName",tableName)
            intent.putExtra("dbName",dbName)
                intent.putExtra("dbUrl",dbConnectInfo.DBUrl)
                intent.putExtra("id",dbConnectInfo.DBId)
                intent.putExtra("pswd",dbConnectInfo.DBPw)
            alertDialog.context.startActivity(intent)






            } catch (e: JSONException) {
                e.printStackTrace()
            }



        }


    }