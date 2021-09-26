package com.example.stacklounge.tool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.stacklounge.R

class ToolInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool_information)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
    }
}