package com.example.stacklounge.board

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.stacklounge.R
import com.example.stacklounge.fragment.FragmentMainCommunity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_board_write_text.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

class BoardWriteFeed : AppCompatActivity() {

    var boardList = arrayListOf<BoardData>()

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun writingText(){
        //realtime db에 BoardData

        val writingTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) //작성시간

        // firebase auth
        val user = Firebase.auth.currentUser

        // db
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
        val userIdRef = database.getReference() // userId 불러오는 경로


        userIdRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val aUserId = snapshot.child("current-user/${user?.uid}").child("login").value
                val aUserphoto = snapshot.child("current-user/${user?.uid}").child("avatar_url").value.toString()

                var boardNumber = snapshot.child("board").child("boardNumber").child("boardNumber").value.toString().toInt()
                boardNumber++

                var duserId = writingTime +"+"+aUserId.toString()

                val addRef = database.getReference("board/$duserId") // 저장경로


                val aTitle = writingTitle.text.toString()
                val aContents = writingContent.text.toString()

                // uid는 auth부분에 있다.

                val aUserInfo = hashMapOf(
                    "userId" to aUserId,
                    "contents" to aContents,
                    "title" to aTitle,
                    "feedTime" to writingTime,
                    "userphoto" to aUserphoto
                )

                val aboardInfo = hashMapOf(
                    "boardNumber" to boardNumber.toString()
                )

                if(aTitle==""){
                    Toast.makeText(applicationContext,"제목을 입력해주세요.",Toast.LENGTH_SHORT).show()
                }
                else if(aContents==""){
                    Toast.makeText(applicationContext,"내용을 입력해주세요.",Toast.LENGTH_SHORT).show()
                }
                else{
                    boardList.add((BoardData(aTitle,aContents,writingTime,aUserId as String)))
                    userIdRef.child("board").child("boardNumber").setValue(aboardInfo)
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