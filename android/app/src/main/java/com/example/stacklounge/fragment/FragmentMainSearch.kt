package com.example.stacklounge.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.stacklounge.GetCompanyAllQuery
import com.example.stacklounge.R
import com.example.stacklounge.company.CompanyListAdapter
import com.example.stacklounge.company.CompanySearch
import com.example.stacklounge.query.apolloClient
import kotlinx.android.synthetic.main.fragment_main_search.*

class FragmentMainSearch : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //appbar랑 메뉴xml 연결
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.query(GetCompanyAllQuery()).await()
            } catch (e: ApolloException) {
                Log.d("CompanyList", "Failure", e)
                null
            }

            val companies = response?.data?.allCompanies?.edges
            if(companies != null && !response.hasErrors()) {
                val adapter = CompanyListAdapter(companies)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.isNestedScrollingEnabled = false
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_search, container, false)
    }

    //appbar 메뉴
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.company_search_menu, menu)
    }

    //appbar 메뉴 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                // navigate to settings screen
                val intent = Intent(activity, CompanySearch::class.java)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.fling_left_to_right,R.anim.fling_right_to_left_out)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}