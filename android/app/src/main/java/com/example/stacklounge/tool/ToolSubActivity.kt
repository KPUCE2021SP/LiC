package com.example.stacklounge.tool

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.stacklounge.R
import kotlinx.android.synthetic.main.activity_tool_sub.*
import kotlinx.android.synthetic.main.frame_tool.toolImage
import kotlinx.android.synthetic.main.frame_tool.toolName

class ToolSubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool_sub)
        title = ""
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
        val imageUrl = intent.getStringExtra("imageUrl")
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val title = intent.getStringExtra("title")
        Glide.with(applicationContext).load(imageUrl).into(toolImage)
        toolName.text = name
        toolTitle.text = title
        toolDesc.text = description
    }
}