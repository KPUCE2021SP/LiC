package com.example.stacklounge.board

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.stacklounge.R
import com.example.stacklounge.company.CompanySearch
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_board_show_comment.*
import kotlinx.android.synthetic.main.activity_board_show_feed.*
import kotlinx.android.synthetic.main.fragment_main_favorite.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BoardShowFeed : AppCompatActivity() {
    //댓글 리스트
    var commentData = arrayListOf<BoardCommentData>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_show_feed)
        title = ""
        //게시글 정보 받아와서 적용
        showBoard()

        // 댓글 recyclerview 연결
        val mAdapter = AdapterComment(this, commentData)
        commentRecyclerView.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        commentRecyclerView.layoutManager = lm
        commentRecyclerView.setHasFixedSize(true)

        //db 찾기용 변수
        val gfeedTime = intent.getStringExtra("feedTime").toString() // 글 작성 시간
        val guserId = intent.getStringExtra("userId").toString() // 글 작성자

        // db
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
        val userIdRef = database.getReference() // userId 불러오는 경로

        // firebase auth
        val user = Firebase.auth.currentUser

        val boardPath = "$gfeedTime+$guserId"

        // 게시글 이미지 세팅
        val database1 = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        database1.child("board")
            .child(boardPath)
            .child("userphoto")
            .get().addOnSuccessListener {
                val avatarImage = it.value as String
                Glide.with(applicationContext).load(avatarImage).into(imgBoardUser)
            }

        // 댓글 recyclerview
        userIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.child("board/$boardPath/comment").children){
                    //commentData.clear()
                    val cUserId = snapshot.child("current-user/${user?.uid}").child("login").value.toString() // 댓글 작성자

                    val key = postSnapshot.key.toString() // 게시글 들어갈때 댓글 경로 + commentNumber

                    val get: BoardCommentData? = postSnapshot.getValue(BoardCommentData::class.java)

                    if(key.contains("+")){
                        val adduserphoto = get?.userphoto.toString()
                        val addcomment = get?.boardCommment.toString()
                        val adduserid  =  get?.userId.toString()
                        val addcommentTime = get?.commentTime.toString()
                        Log.d("addcomment",addcomment)

                        commentData.add((BoardCommentData(adduserid,addcomment,addcommentTime,adduserphoto)))

//                        // 댓글 이미지 경로
//                        val database2 = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
//                        database2.child("board")
//                            .child(boardPath)
//                            .child("comment")
//                            .child(key)
//                            .child("userphoto")
//                            .get().addOnSuccessListener {
//                                val avatarImage = it.value as String
//                                Log.d("avatarImage",avatarImage)
//                                Glide.with(applicationContext).load(avatarImage).into(imgCommentUser)
//                            }

                        mAdapter.notifyDataSetChanged()
                    }
                    else{
                        continue
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                //실패할 때
                Toast.makeText(applicationContext,"DB 에러",Toast.LENGTH_SHORT).show()
            }

        })

        // 댓글 작성 버튼
        imgWriteComment.setOnClickListener{
            val createComment = edtCreateText.text.toString() // 댓글 내용

            if(createComment==""){
                Toast.makeText(this,"댓글을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else{
                ///db에 저장

                val cwritingTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) //댓글 작성시간

                userIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val commentpath = "$gfeedTime+$guserId"

                        val cUserId = snapshot.child("current-user/${user?.uid}").child("login").value.toString() // 댓글 작성자
                        val cUserphoto = snapshot.child("current-user/${user?.uid}").child("avatar_url").value.toString() // 댓글 작성자 프사

                        Glide.with(applicationContext)
                            .load(cUserphoto)
                            .into(imgCommentUser)

                        val cPath = database.getReference("board/$commentpath").child("comment")  // 저장경로

                        // 댓글 작성자 db
                        val cUserInfo = hashMapOf(
                            "userId" to cUserId,
                            "boardCommment" to createComment,
                            "commentTime" to cwritingTime,
                            "userphoto" to cUserphoto
                        )

                        // db에 댓글 작성자 정보 저장
                        cPath.child("$cwritingTime+$cUserId").setValue(cUserInfo)

                        //db에 commentNumber 저장
                        if(snapshot.child("board").child(commentpath).child("comment").child("commentNumber").child("commentNumber").exists()){

                            //db에 commentNumber이 있을 때
                            var commentNumber = snapshot.child("board/$commentpath").child("comment").child("commentNumber").child("commentNumber").value.toString().toInt()
                            commentNumber++
                            cPath.child("commentNumber").child("commentNumber").setValue(commentNumber)
                        }
                        else{
                            //db에 commentNumber이 없을 때
                            cPath.child("commentNumber").child("commentNumber").setValue("1")
                        }

                        edtCreateText.setText("")

                        commentData.add((BoardCommentData(cUserId,createComment,cwritingTime,cUserphoto)))

                        mAdapter.notifyDataSetChanged()

                    }

                    override fun onCancelled(error: DatabaseError) {
                        //실패할 때
                        Toast.makeText(applicationContext,"DB 에러",Toast.LENGTH_SHORT).show()
                    }

                })

            }

        }

    }
    //appbar 메뉴
    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.company_search_menu_inflated, menu)
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