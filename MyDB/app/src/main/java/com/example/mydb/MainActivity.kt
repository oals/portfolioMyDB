package com.example.mydb

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mydb.DBHelper.DBHelper
import com.example.mydb.DTO.DBConnectInfoDTO
import com.example.mydb.Fragment.MyDbFragment
import com.example.mydb.Fragment.MyInfoFragment
import com.example.mydb.User.LoginActivity
import com.example.mydb.User.LogoutActivity
import com.example.mydb.User.RegisterActivity


import com.example.mydb.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,BottomNavigationView.OnNavigationItemSelectedListener  {

    var queue: RequestQueue? = null
    lateinit var drawerLayout : DrawerLayout

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    private lateinit var dbHelper: DBHelper


    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu,menu)
        val search = menu?.findItem(androidx.appcompat.R.id.search_badge)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            android.R.id.home->{ // 메뉴 버튼

                drawerLayout.openDrawer(GravityCompat.START)
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)


//        dbHelper = DBHelper(this, "mydb.db", null, 1)
//
//        dbHelper.onCreate(dbHelper.writableDatabase)


        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topNav)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowCustomEnabled(true);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_list_24)  // 왼쪽 버튼 이미지 설정

        supportActionBar!!.title = "MyDB"


        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView : NavigationView = findViewById(R.id.nav)
        navigationView.setNavigationItemSelectedListener(this)



        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)


        val menu = navigationView.menu

        val headerView = navigationView.getHeaderView(0)
        if (prefs.getString("userId",null) != null) {

            headerView.findViewById<TextView>(R.id.menuUserId).text = prefs.getString("userId","null") + "님"
            menu.findItem(R.id.loginItem).isVisible = false
            menu.findItem(R.id.registerItem).isVisible = false
            menu.findItem(R.id.logoutItem).isVisible = true
        }else{

            headerView.findViewById<TextView>(R.id.menuUserId).text = "로그인이 필요합니다"
            menu.findItem(R.id.loginItem).isVisible = true
            menu.findItem(R.id.registerItem).isVisible = true
            menu.findItem(R.id.logoutItem).isVisible = false
        }



        supportFragmentManager.beginTransaction().replace(R.id.DBFragmentView, MyDbFragment())
            .commit()


        if (queue == null) {
            queue = Volley.newRequestQueue(this)
        }









    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        val nextIntent : Intent

        when(item.itemId){
            R.id.loginItem-> {
                nextIntent = Intent(this,LoginActivity::class.java)
                startActivity(nextIntent)
            }
            R.id.logoutItem-> {
                nextIntent = Intent(this,LogoutActivity::class.java)
                startActivity(nextIntent)
            }
            R.id.registerItem-> {
                nextIntent = Intent(this,RegisterActivity::class.java);
                startActivity(nextIntent)
            }

            R.id.MyDB ->{
                item.isChecked = true
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.DBFragmentView,MyDbFragment())
                transaction.commit()
            }

            R.id.plusDB -> {
                item.isChecked = true
                val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)


                val build = AlertDialog.Builder(this).setView(R.layout.activity_dbconnect)
                    val dialog = build.create()


                    dialog.setOnShowListener {


                        //창 닫기 버튼
                        dialog.findViewById<Button>(R.id.closeButton)?.setOnClickListener { v ->
                            dialog.dismiss()
                        }



                        dialog.findViewById<Spinner>(R.id.DBSpinner).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                when (position) {
                                    1 -> {
                                        // 첫 번째 항목이 선택되었을 때 실행할 코드를 여기에 작성합니다.
                                        dialog.findViewById<EditText>(R.id.inputSID).visibility = View.GONE
                                    }
                                    2 -> {
                                        // 두 번째 항목이 선택되었을 때 실행할 코드를 여기에 작성합니다.
                                        dialog.findViewById<EditText>(R.id.inputSID).visibility = View.VISIBLE
                                    }
                                    // 필요한 만큼 case를 추가할 수 있습니다.
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // 아무것도 선택되지 않았을 때 실행할 코드를 여기에 작성합니다.
                            }
                        }


                        //db 연결 버튼
                        dialog.findViewById<Button>(R.id.connectButton)?.setOnClickListener { v ->

                            //입력내용 받아오기
                            val dbName: String = dialog.findViewById<Spinner>(R.id.DBSpinner).selectedItem.toString()
                            val url: String = dialog.findViewById<EditText>(R.id.inputUrl).text.toString()
                            val port: String = dialog.findViewById<EditText>(R.id.inputPort).text.toString()
                            val sid: String = dialog.findViewById<EditText>(R.id.inputSID).text.toString()
                            val id: String = dialog.findViewById<EditText>(R.id.inputId).text.toString()
                            val pw: String = dialog.findViewById<EditText>(R.id.inputPw).text.toString()

                            val sdfNow = SimpleDateFormat("yyyy년 MM월 dd일")
                            val time = sdfNow.format(Date(System.currentTimeMillis()))

                            var dbConnectInfo: DBConnectInfoDTO = DBConnectInfoDTO(dbName,prefs.getString("userId","null").toString(), url, port,sid,id, pw, time.toString())


                            //db 중복 연결 검사
                            dbHelper = DBHelper(this, "mydb.db", null, 1)
                            var dbConnectInfoChk = dbHelper.InsertConnectInfoCheck(dbConnectInfo)

                            if(dbConnectInfoChk){
                                val build = AlertDialog.Builder(this)
                                build.setTitle("연결 실패")
                                build.setMessage(
                                    "이미 연결된 데이터베이스 입니다."
                                )
                                build.setPositiveButton("확인",
                                    DialogInterface.OnClickListener{ dialog, id ->
                                    })

                                build.show()

                            }else{
                                //위 정보를 통해 api 연결
                                httpConnection(dialog, dbConnectInfo)

                                //연결 성공 시 접속 정보 저장
                            }

                        }


                    }


                    val lp = WindowManager.LayoutParams()
                    lp.copyFrom(dialog.window?.attributes)
                    lp.width = 1450
                    lp.height = 2000
                    dialog.window?.attributes = lp

                    dialog.show()


            }

            R.id.myPage -> {
                item.isChecked = true
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.DBFragmentView,MyInfoFragment())
                transaction.commit()

            }
        }
        drawerLayout.closeDrawers()
        return false

    }



    private fun httpConnection(view: AlertDialog, dbConnectInfo : DBConnectInfoDTO) {

        var dbUrl = ""
        if(dbConnectInfo.DBName.equals("MariaDB")){
            dbUrl = "jdbc:mariadb://" + dbConnectInfo.DBUrl + ":" + dbConnectInfo.DBPort + "/";
        }else{
            dbUrl = "jdbc:oracle:thin:@" + dbConnectInfo.DBUrl + ":" + dbConnectInfo.DBPort + ":" + dbConnectInfo.DBSid;
        }

        val url: String = "http://ec2-43-201-18-102.ap-northeast-2.compute.amazonaws.com:8100/connectDB" +
                "?url=${dbUrl}" +
                "&id=${dbConnectInfo.DBId}" +
                "&pswd=${dbConnectInfo.DBPw}";

        Log.d("url테스트",url)



        val jsonRequest: JsonObjectRequest =
            JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, { response ->
                parseData(response, view, dbConnectInfo)

            }, { error ->

                val build = AlertDialog.Builder(this)
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


    private fun parseData(jsonObject: JSONObject, view: AlertDialog,dbConnectInfo : DBConnectInfoDTO) {


        try {

            if(jsonObject.getString("connectState").toString().equals("true")){


                //db 저장 작업
                dbHelper = DBHelper(this, "mydb.db", null, 1)
                dbHelper.InsertConnectInfo(dbConnectInfo)


                val build = AlertDialog.Builder(this)
                build.setTitle("연결 완료")
                build.setMessage(
                    "해당 데이터베이스 접속 정보가 저장 됩니다."
                )
                build.setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, id ->
                        view.dismiss()


                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.DBFragmentView,MyDbFragment())
                        transaction.commit()

                        drawerLayout.closeDrawers()

                    })

                build.show()




            }else{



                val build = AlertDialog.Builder(this)
                build.setTitle("연결 실패")
                build.setMessage(
                    "입력 정보를 확인해주세요."
                )
                build.setPositiveButton("확인",
                    DialogInterface.OnClickListener{ dialog, id ->
                    })

                build.show()

            }




        } catch (e: JSONException) {
            e.printStackTrace()
        }




    }


}









