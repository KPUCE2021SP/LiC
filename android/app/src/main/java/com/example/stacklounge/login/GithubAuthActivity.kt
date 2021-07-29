package com.example.stacklounge.login

import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_github_auth.*


class GithubAuthActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_auth)
        title = "GithubAuth"

        var mAuth = FirebaseAuth.getInstance()


        btnLogin.setOnClickListener {
            var Email = edtEmail.getText().toString()

            // QAuthProvider 인스턴스 생성
            val provider = OAuthProvider.newBuilder("github.com")
            // Target specific email with login hint.
            provider.addCustomParameter("login", Email)

            // Target specific email with login hint.


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
                            githubLoginClear()
                        })
                    .addOnFailureListener(
                        OnFailureListener {
                            // Handle failure.
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
                            githubLoginClear()
                        })
                    .addOnFailureListener(
                        OnFailureListener {
                            // Handle failure.
                            Toast.makeText(applicationContext,"Login failure $Email",Toast.LENGTH_SHORT).show()
                        })
            }

        }

    }

    private fun githubLoginClear() {

        startActivity(

            Intent(this, MainActivity::class.java)

        )
        finish()
    }
}