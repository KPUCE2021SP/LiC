package com.example.stacklounge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }
        val user = Firebase.auth.currentUser
        Toast.makeText(applicationContext,"${user?.email}", Toast.LENGTH_SHORT).show()

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