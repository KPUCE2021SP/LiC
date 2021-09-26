package com.example.stacklounge.tool

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.stacklounge.R
import kotlinx.android.synthetic.main.activity_tool_detail.*


class ToolDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool_detail)
        title = ""
        supportActionBar?.hide()
        val name = intent.getStringExtra("name")
        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val oss = intent.getStringExtra("oss")
        val canonical = intent.getStringExtra("canonical")
        val desc = intent.getStringExtra("description")
        val web = intent.getStringExtra("web")

        namet.text = name
        titlet.text = title
        desct.text = desc
        Glide.with(applicationContext).load(image).into(imaget)

            webUrl.setOnClickListener {
                if(web != null) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(web))
                startActivity(browserIntent)
                }else {
                    Toast.makeText(this, "링크가 준비되지 않았어요.\n개발자 깃허브에 이슈를 생성해주세요", Toast.LENGTH_LONG).show()
                }
            }


            ossUrl.setOnClickListener {
                if(oss != null) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(oss))
                    startActivity(browserIntent)
                }else {
                    Toast.makeText(this, "링크가 준비되지 않았어요.\n개발자 깃허브에 이슈를 생성해주세요", Toast.LENGTH_LONG).show()
                }
            }
            canonicalUrl.setOnClickListener {
                if(canonical != null) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(canonical))
                    startActivity(browserIntent)
                }else {
                    Toast.makeText(applicationContext, "링크가 준비되지 않았어요.\n개발자 깃허브에 이슈를 생성해주세요", android.widget.Toast.LENGTH_LONG).show()
                }
            }
    }
}