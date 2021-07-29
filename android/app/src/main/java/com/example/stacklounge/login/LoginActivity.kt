package com.example.stacklounge.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stacklounge.MainActivity
import com.example.stacklounge.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title ="LoginActivity"

        btnGithublogin.setOnClickListener {
            startActivity(
                Intent(this, GithubAuthActivity::class.java)
            )
            finish()
        }
    }
}