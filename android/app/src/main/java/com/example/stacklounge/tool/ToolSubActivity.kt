package com.example.stacklounge.tool

import android.content.Intent
import android.os.Bundle
import android.view.Window
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
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tool_sub)
        title = ""
        val imageUrl = intent.getStringExtra("imageUrl")
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("desc")
        val title = intent.getStringExtra("title")
        Glide.with(applicationContext).load(imageUrl).into(toolImage)
        toolName.text = name
        toolTitle.text = title
        toolDesc.text = description
        toolDetailButton.setOnClickListener {
            val intent = Intent(this, ToolDetail::class.java).apply{
                putExtra("name", name)
                putExtra("title", title)
                putExtra("image", imageUrl)
                putExtra("oss", intent.getStringExtra("oss"))
                putExtra("description", description)
                putExtra("web", intent.getStringExtra("web"))
                putExtra("canonical",intent.getStringExtra("canonical"))
            }
            startActivity(intent)
        }
    }
}