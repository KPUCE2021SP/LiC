package com.example.stacklounge.board

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.stacklounge.R
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

        }

    }


}