package com.example.stacklounge.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.stacklounge.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main_favorite.*


class FragmentMainFavorite : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //appbar랑 메뉴xml 연결
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        database.root.child("current-user").child("${user?.uid}").child("avatar_url").get().addOnSuccessListener{
            var avatarImage = it.value as String
            Glide.with(requireContext()).load(avatarImage).into(githubImg);
        }
        database.root.child("current-user").child("${user?.uid}").child("name").get().addOnSuccessListener{
            githubID.text = it.value as String
        }
        database.root.child("current-user").child("${user?.uid}").child("login").get().addOnSuccessListener{
            githubProfile.text = it.value as String
        }
    }

    //appbar 메뉴
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_navi_menu, menu)
    }

    //appbar 메뉴 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tbmenu -> {
                //Toast.makeText(activity,"메뉴",Toast.LENGTH_SHORT).show()
                true
            }

            R.id.tbsearch -> {
                // navigate to settings screen
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}