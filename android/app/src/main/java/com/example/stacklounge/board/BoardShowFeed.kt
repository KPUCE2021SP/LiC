package com.example.stacklounge.board

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stacklounge.MainActivity
import com.example.stacklounge.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_board_show_feed.*
import kotlinx.android.synthetic.main.activity_board_write_text.*
import kotlinx.android.synthetic.main.fragment_community_board_recycleview.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BoardShowFeed : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_show_feed)

        //게시글 정보 받아와서 적용
        showBoard()


        // db에서 댓글 인덱스 값으로 끌어와야한다.
        val commentData = arrayListOf<BoardCommentData>(
            BoardCommentData("KKodiac", "oh good", "03:05", "stackloungeicon"),
        )

        // 댓글 recyclerview 연결
        val mAdapter = AdapterComment(this, commentData)
        commentRecyclerView.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        commentRecyclerView.layoutManager = lm
        commentRecyclerView.setHasFixedSize(true)

        //db 찾기용 변수
        val gfeedTime = intent.getStringExtra("feedTime").toString()
        val guserId = intent.getStringExtra("userId").toString()

        // 댓글 작성 버튼
        imgWriteComment.setOnClickListener{
            val createText = edtCreateText.text.toString() // 댓글 내용
            
            if(createText==""){
                Toast.makeText(this,"댓글을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else{
                ///db에 저장

                // firebase auth
                val user = Firebase.auth.currentUser

                val cwritingTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) //댓글 작성시간


                // db
                val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
                val userIdRef = database.getReference() // userId 불러오는 경로

                userIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val cUserId = snapshot.child("current-user/${user?.uid}").child("login").value // 댓글 작성자
                        val cUserphoto = snapshot.child("current-user/${user?.uid}").child("avatar_url").value // 작성자 프사

                        //

                        val commentNumberPath = "$gfeedTime+$guserId"

                        val cPath = database.getReference("board/$commentNumberPath").child("commentNumber") // 저장경로

                        cPath.setValue("0")

                        //db에 commentNumber이 있을 때
                        if(snapshot.child("board").child(commentNumberPath).child("commentNumber").child("commentNumber").value==null){

                            //db에 commentNumber이 있을 때
                        }
                        else{
                            //db에 commentNumber이 없을 때
                        }
                        
                        //여기까지함

                        var boardNumber = snapshot.child("board").child("boardNumber").child("boardNumber").value.toString().toInt()


                        var cuserId = cwritingTime +"+"+cUserId.toString()



                        val aTitle = writingTitle.text.toString()
                        val aContents = writingContent.text.toString()

                        // uid는 auth부분에 있다.

                        val aUserInfo = hashMapOf(
                            //"userId" to aUserId,
                            "contents" to aContents,
                            "title" to aTitle,
                            "feedTime" to cwritingTime
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
                            userIdRef.child("board").child("boardNumber").setValue(aboardInfo)
                            //addRef.setValue(aUserInfo)

                            finish()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        //실패할 때
                        Toast.makeText(applicationContext,"DB 에러",Toast.LENGTH_SHORT).show()
                    }

                })


                ///recyclerview에 동기화
            }

        }

        //앱바 숨김
        val actionBarBoardWriteText: ActionBar? = supportActionBar
        actionBarBoardWriteText?.hide()

        //뒤로가기 이미지
        imgBackShow.setOnClickListener{
            finish()
        }

    }

    fun showBoard(){
        //게시글 정보 받아와서 적용
        val gtitle = intent.getStringExtra("title").toString()
        val gcontents = intent.getStringExtra("contents").toString()
        val gfeedTime = intent.getStringExtra("feedTime").toString()
        val guserId = intent.getStringExtra("userId").toString()

        BoardShowTitle.text = gtitle
        BoardShowContent.text = gcontents
        WritingTime.text = gfeedTime
        BoardUserId.text = guserId
    }

}