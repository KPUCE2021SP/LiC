package com.example.stacklounge.board

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.stacklounge.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_board_write_text.*

class BoardWriteFeed : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write_text)

        //앱바 숨김
        val actionBarBoardWriteText: ActionBar? = supportActionBar
        actionBarBoardWriteText?.hide()

        // 뒤로가기
        btnBackText.setOnClickListener{
            finish()
        }

        // 글 작성
        btnWriteComplete.setOnClickListener {
            writingText()

        }

    }

    fun writingText(){
        //realtime db에 BoardData

        // firebase auth
        val user = Firebase.auth.currentUser
        val aUid = user?.uid

        // db
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
        val userIdRef = database.getReference("current-user/${user?.uid}") // userId 불러오는 경로


        userIdRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val aUserId = snapshot.child("login").value

                val addRef = database.getReference("board/$aUserId") // 저장경로

                var aTitle = writingTitle.text.toString()
                val aContents = writingContent.text.toString()
                val aNumber = 0
                // uid는 auth부분에 있다.

                val aUserInfo = hashMapOf(
                    "user" to aUserId,
                    "uid" to aUid,
                    "number" to aNumber,
                    "contents" to aContents,
                    "title" to aTitle
                )

                if(aTitle==""){
                    Toast.makeText(applicationContext,"제목을 입력해주세요.",Toast.LENGTH_SHORT).show()
                }
                else if(aContents==""){
                    Toast.makeText(applicationContext,"내용을 입력해주세요.",Toast.LENGTH_SHORT).show()
                }
                else{
                    addRef.setValue(aUserInfo)

                    finish()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                //실패할 때
                Toast.makeText(applicationContext,"DB 에러",Toast.LENGTH_SHORT).show()
            }

        })
    }


}