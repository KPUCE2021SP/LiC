package com.example.stacklounge.company

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import com.example.stacklounge.GetCompanyByNameQuery
import com.example.stacklounge.R
import com.example.stacklounge.query.apolloClient
import kotlinx.android.synthetic.main.activity_company_search.*


class CompanySearch : AppCompatActivity() {
    lateinit var companyNameView : TextView
    lateinit var techStackView : TextView
    lateinit var companyTextImage : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_search)
        cardView.visibility = View.INVISIBLE
        title = "궁금한 기업을 검색해보세요"
        companyTextImage = findViewById(R.id.companyTextImage)
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
            this.isIconified = false
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
                                cardView.visibility = View.VISIBLE
                                val logo = company.companyLogo
                                if(logo == ""){
                                    companyTextImage.setImageResource(R.drawable.stackloungeicon)
                                }
                                else {
                                    Glide.with(context).load(logo).into(companyTextImage)
                                }
                                companyNameView.text = company.companyName
                                cardView.setOnClickListener{
                                    val cover = arrayListOf<String>()
                                    val new_cover = arrayListOf<String>()
                                    cover.addAll(company.techStack as Collection<String>)
                                    for(c in cover){
                                        if(new_cover.contains(c.lowercase())){
                                            Log.d("Overlapped", "Duplicate")
                                        } else {
                                            new_cover.add(c.lowercase())
                                        }
                                    }
                                    techStackView.text = "확인된 기술 도구: ${new_cover.size}"
                                    val intent = Intent(applicationContext, CompanyDetail::class.java).apply{
                                        putExtra("CompanyTechList", new_cover)
                                        putExtra("CompanyName", company.companyName)
                                        putExtra("CompanyImage", company.companyLogo)
                                    }
                                    startActivity(intent)
                                }
                            })
                        } else {
                            Toast.makeText(applicationContext, "해당 기업을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
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
                this.overridePendingTransition(R.anim.fling_right_to_left,R.anim.fling_left_to_right_out)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}