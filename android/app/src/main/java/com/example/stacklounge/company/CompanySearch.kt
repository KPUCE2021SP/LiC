package com.example.stacklounge.company

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.stacklounge.GetCompanyByNameQuery
import com.example.stacklounge.R
import com.example.stacklounge.query.apolloClient


class CompanySearch : AppCompatActivity() {
    lateinit var companyNameView : TextView
    lateinit var techStackView : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_search)
        title = ""
        companyNameView = findViewById(R.id.companyNameTextView)
        techStackView = findViewById(R.id.techStackTextView)
    }

    //appbar 메뉴
    override fun onCreateOptionsMenu(menu: Menu) :Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.company_search_menu_inflated, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search_inflated).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                // SearchView 에서 텍스트 입력 시
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
                // SearchView 에서 텍스트 쿼리 입력 시
                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.d("onQueryTextSubmit", "$query")
                    lifecycleScope.launchWhenResumed {
                        val response = try {
                            apolloClient.query(GetCompanyByNameQuery(name = query)).await()
                        } catch (e: ApolloException) {
                            Log.d("ApolloQuery", "Failure", e)
                            null
                        }

                        val company = response?.data?.companyByName
                        if(company != null && !response.hasErrors()) {
                            Handler(Looper.getMainLooper()).post(Runnable {
                                companyNameView.text = company.companyName
                                techStackView.text = company.techStack.toString()
                            })
                        }
                    }
                    return true
                }
            })
        }
        return true
    }

    //appbar 메뉴 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_inflated -> {
                true
            }
            R.id.backBton -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}