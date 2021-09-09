package com.example.stacklounge.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.stacklounge.R
import com.example.stacklounge.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_board_write_text.*
import kotlinx.android.synthetic.main.fragment_main_user.*
import kotlinx.android.synthetic.main.fragment_main_user.view.*


class FragmentMainUser : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val view = inflater.inflate(R.layout.fragment_main_user, null)

        // github ID 불러오기
        getUserId()

        //로그아웃 버튼
        view.btnlogout.setOnClickListener {
            Firebase.auth.signOut()

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)

        }

        return view

    }

    fun getUserId(){
        val user = Firebase.auth.currentUser

        // db
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
        val userIdRef = database.getReference("current-user/${user?.uid}") // userId 불러오는 경로

        userIdRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                val aUserId = snapshot.child("login").value

                tvUserIDinUser.text = aUserId.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                //실패할 때
                Toast.makeText(activity,"DB 에러", Toast.LENGTH_SHORT).show()
            }

        })
    }

}