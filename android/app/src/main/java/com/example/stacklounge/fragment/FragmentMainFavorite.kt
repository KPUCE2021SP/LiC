package com.example.stacklounge.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import com.example.stacklounge.GetToolByNameQuery
import com.example.stacklounge.R
import com.example.stacklounge.query.apolloClient
import com.example.stacklounge.tool.ToolData
import com.example.stacklounge.tool.ToolListAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main_favorite.*
import kotlinx.coroutines.*


class FragmentMainFavorite : Fragment() {
    var toolList : MutableList<ToolData> = ArrayList()
    private val database =
        FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app").reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //appbar랑 메뉴xml 연결
        setHasOptionsMenu(true)
        val user = Firebase.auth.currentUser
        database.root.child("current-user")
            .child("${user?.uid}")
            .child("avatar_url")
            .get().addOnSuccessListener {
                var avatarImage = it.value as String
                Glide.with(requireContext()).load(avatarImage).into(githubImg)
            }
        database.root.child("current-user")
            .child("${user?.uid}")
            .child("name")
            .get().addOnSuccessListener {
                githubID.text = it.value as String
            }
        database.root.child("current-user")
            .child("${user?.uid}")
            .child("login")
            .get().addOnSuccessListener {
                githubProfile.text = it.value as String
                database.root.child("usertopics")
                    .child(it.value.toString())
                    .child("usertopics").get().addOnSuccessListener { its ->
                        lifecycleScope.launchWhenResumed {
                            withContext(Dispatchers.IO) {
                                for (item in its.value as List<String>) {
                                    loadData(item = item)
                                }
                            }
                        }
                    }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = ""
        return inflater.inflate(R.layout.fragment_main_favorite, container, false)
    }

    //appbar 메뉴
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    //appbar 메뉴 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun loadData(item: String) = CoroutineScope(Dispatchers.Main).launch {
        lateinit var tooldata: ToolData
        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.query(GetToolByNameQuery(name = item)).await()
            } catch (e: ApolloException) {
                Log.d("CompanyList", "Failure", e)
                null
            }
            if (response?.data?.toolByName?.name != null) {
                tooldata = ToolData(
                    name = response.data?.toolByName?.name,
                    imageUrl = response.data?.toolByName?.imageUrl,
                    description = response.data?.toolByName?.description,
                    title = response.data?.toolByName?.title,
                    id = response.data?.toolByName?.id.toString(),
                    slug = response.data?.toolByName?.slug,
                    ossRepo = response.data?.toolByName?.ossRepo,
                    canonicalUrl = response.data?.toolByName?.canonicalUrl,
                    websiteUrl = response.data?.toolByName?.websiteUrl,
                )
                toolList.add(tooldata)
                val mAdapter = ToolListAdapter(toolList)
                val gridLayoutManager = GridLayoutManager(view?.context, 2)
                topicRecyclerView.layoutManager = gridLayoutManager
                topicRecyclerView.adapter = mAdapter
            }
        }
    }
}
