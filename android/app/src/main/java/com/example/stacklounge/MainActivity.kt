package com.example.stacklounge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stacklounge.databinding.ActivityMainBinding
import com.example.stacklounge.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        title = "MainActivity"

        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

        btnLogout.setOnClickListener{
            // 로그아웃하고 다시 LoginActivity로 이동
            Firebase.auth.signOut()
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

    }
}