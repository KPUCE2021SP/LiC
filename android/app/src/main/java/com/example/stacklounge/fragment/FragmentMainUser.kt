package com.example.stacklounge.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.stacklounge.R
import com.example.stacklounge.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main_user.*
import kotlinx.android.synthetic.main.fragment_main_user.view.*


class FragmentMainUser : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main_user, null)

        //로그아웃 버튼
        view.btnlogout.setOnClickListener {
            Firebase.auth.signOut()

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)

        }
        return view

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