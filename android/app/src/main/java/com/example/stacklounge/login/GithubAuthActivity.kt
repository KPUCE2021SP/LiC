package com.example.stacklounge.login

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stacklounge.MainActivity
import com.example.stacklounge.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_github_auth.*


class GithubAuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_auth)
        title = "GithubAuth"

        //var mAuth = FirebaseAuth.getInstance()


        btnLogin.setOnClickListener {
            var Email = edtEmail.getText().toString()

            doLogin(Email)
        }

    }

    //로그인 정보 확인되면 MainActivity로 이동
    private fun githubLoginClear() {

        startActivity(
            Intent(this, MainActivity::class.java)
        )
        finish()
    }

    private fun doLogin(email: String){
        var mAuth = FirebaseAuth.getInstance()

        val provider = OAuthProvider.newBuilder("github.com")
        // edtEmail에 입력한 이메일로 github 연동
        provider.addCustomParameter("login", email)

        val scopes: ArrayList<String?> = object : ArrayList<String?>() {
            init {
                add("user:email")
            }
        }
        provider.scopes = scopes

        val pendingResultTask: Task<AuthResult>? = mAuth.getPendingAuthResult()
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener(
                    OnSuccessListener<AuthResult?> {
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // authResult.getCredential().getAccessToken().
                        // 로그인 성공 시 MainActivity로 이동
                        githubLoginClear()
                    })
                .addOnFailureListener(
                    OnFailureListener {
                        // Handle failure.
                        // 로그인 실패
                        Toast.makeText(applicationContext,"Login failure",Toast.LENGTH_SHORT).show()
                    })
        } else {
            mAuth
                .startActivityForSignInWithProvider( /* activity= */this, provider.build())
                .addOnSuccessListener(
                    OnSuccessListener<AuthResult?> {
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // authResult.getCredential().getAccessToken().
                        // 로그인 성공 시 MainActivity로 이동
                        githubLoginClear()
                    })
                .addOnFailureListener(
                    OnFailureListener {
                        // Handle failure.
                        // 로그인 실패
                        Toast.makeText(applicationContext,"Login failure",Toast.LENGTH_SHORT).show()
                    })
        }

    }
}