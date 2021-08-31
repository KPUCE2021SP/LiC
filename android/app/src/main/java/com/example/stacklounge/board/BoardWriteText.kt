package com.example.stacklounge.board

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.stacklounge.R

class BoardWriteText : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write_text)


        val actionBarBoardWriteText: ActionBar? = supportActionBar
        actionBarBoardWriteText?.hide()

    }
}