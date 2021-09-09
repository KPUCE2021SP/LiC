package com.example.stacklounge.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stacklounge.R
import kotlinx.android.synthetic.main.activity_board_show_feed.*
import kotlinx.android.synthetic.main.fragment_community_board_recycleview.*

class BoardShowFeed : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_show_feed)

        //게시글 정보 받아와서 적용
        val gtitle = intent.getStringExtra("title").toString()
        val gcontents = intent.getStringExtra("contents").toString()
        val gfeedTime = intent.getStringExtra("feedTime").toString()
        val guserId = intent.getStringExtra("userId").toString()
        Log.d("title",gtitle)

        BoardShowTitle.text = gtitle
        BoardShowContent.text = gcontents
        WritingTime.text = gfeedTime
        BoardUserId.text = guserId


        // db에서 댓글 인덱스 값으로 끌어와야한다.
        var commentData = arrayListOf<BoardCommentData>(
            BoardCommentData("KKodiac", "oh good", "03:05", "stackloungeicon"),
        )

        val mAdapter = AdapterComment(this, commentData)
        commentRecyclerView.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        commentRecyclerView.layoutManager = lm
        commentRecyclerView.setHasFixedSize(true)



        //앱바 숨김
        val actionBarBoardWriteText: ActionBar? = supportActionBar
        actionBarBoardWriteText?.hide()

        //뒤로가기 이미지
        imgBackShow.setOnClickListener{
            finish()
        }

    }
}