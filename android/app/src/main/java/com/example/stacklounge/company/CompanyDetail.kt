package com.example.stacklounge.company

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import com.example.stacklounge.GetToolByNameQuery
import com.example.stacklounge.R
import com.example.stacklounge.query.apolloClient
import kotlinx.android.synthetic.main.activity_company_detail.*
import kotlinx.android.synthetic.main.fragment_main_search.*
import java.util.*


class CompanyDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_detail)
        supportActionBar?.hide()
        val companyTechList = intent.getStringArrayListExtra("CompanyTechList")
        val companyName = intent.getStringExtra("CompanyName")
        val companyLogo = intent.getStringExtra("CompanyImage")
        if (companyLogo != ""){
            Glide.with(this).load(companyLogo).into(companyLogoBlur)
        }
        companyNameSet.text = companyName
        val toolList : MutableList<GetToolByNameQuery.Data?> = arrayListOf()
        if (companyTechList != null) {
            lifecycleScope.launchWhenResumed {
                for(tool in companyTechList){
                    val response = try {
                        apolloClient.query(GetToolByNameQuery(name = tool)).await()
                    } catch (e: ApolloException) {
                        Log.d("ToolQuery", "Failure", e)
                        null
                    }
                    if(response?.data?.toolByName.toString() == "null") {
                        Log.d("ERROR", response?.data.toString())
                    } else {
                        toolList.add(response?.data)
                        val companyDetailAdapter = CompanyDetailAdapter(toolList)
                        techListRecyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                        val snapHelper: SnapHelper = PagerSnapHelper()
                        techListRecyclerView.onFlingListener = null;
                        snapHelper.attachToRecyclerView(techListRecyclerView)
                        techListRecyclerView.adapter = companyDetailAdapter
                    }
                }
            }
        }
    }
}