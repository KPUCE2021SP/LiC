package com.example.stacklounge.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stacklounge.R
import kotlinx.android.synthetic.main.activity_board_show_feed.*

class BoardShowFeed : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_show_feed)



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