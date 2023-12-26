package com.example.mydb.Fragment

import android.app.DownloadManager.Request
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mydb.R
import com.example.mydb.UserInfoActivity
import org.json.JSONException
import org.json.JSONObject


class MyInfoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view: View = inflater.inflate(R.layout.fragment_my_info, container, false)

        view.findViewById<Button>(R.id.userInfoBtn).setOnClickListener {

            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivity(intent)

        }





        return view
    }



}