package com.example.stacklounge.login

import android.content.Intent
import android.net.Uri
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.oAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_main_user.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //상단 바 숨김
        val actionBar = supportActionBar
        actionBar!!.hide()
        functions = Firebase.functions
        //github 로그인
        btnLogin.setOnClickListener {
            doLogin()
        }

        //github 회원가입
        signup.setOnClickListener {
            doSignup()
        }

    }

    //github 로그인 함수
    private fun doLogin(){
        
        var mAuth = FirebaseAuth.getInstance()

        val provider = OAuthProvider.newBuilder("github.com")

        provider.addCustomParameter("login", "")

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

                        val user = Firebase.auth.currentUser

                        //var confirmedEmail = user?.email // 로그인 확인된 이메일 저장

                        // 로그인 성공 시 MainActivity로 이동
                        githubLoginClear()

                    })
                .addOnFailureListener(
                    OnFailureListener {
                        // Handle failure.
                        // 로그인 실패
                        Toast.makeText(applicationContext,"Login failure", Toast.LENGTH_SHORT).show()
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

                        val user = Firebase.auth.currentUser


                        val profile = it.additionalUserInfo?.profile
                        val v = profile?.values
                        val k = profile?.keys
                        Log.d("Profile", profile.toString())

                        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app").reference

                        database.root.child("current-user").child("${user?.uid}").child("avatar_url").setValue(profile?.get("avatar_url"))
                        database.root.child("current-user").child("${user?.uid}").child("html_url").setValue(profile?.get("html_url"))
                        database.root.child("current-user").child("${user?.uid}").child("login").setValue(profile?.get("login"))
                        database.root.child("current-user").child("${user?.uid}").child("name").setValue(profile?.get("name"))

                        val login = profile?.get("login").toString()

                        val userlist = database.root.child("userlist").addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val aUserId = snapshot.value.toString()
                                // 처음 온 사용자면 realtime-db에 새로등록해준다.
                                if (!aUserId.contains(login)) {
                                    addDatabase(login)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                println("Failed to read value")
                            }

                        })

                        // 로그인 성공 시 MainActivity로 이동
                        githubLoginClear()
                    })
                .addOnFailureListener(
                    OnFailureListener {
                        // Handle failure.
                        // 로그인 실패
                        Toast.makeText(applicationContext,"Login failure", Toast.LENGTH_SHORT).show()
                    })
        }

    }

    //로그인 정보 확인되면 MainActivity로 이동
    private fun githubLoginClear() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
        finish()
    }

    //github 회원가입
    private fun doSignup(){
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/signup?ref_cta=Sign+up&ref_loc=header+logged+out&ref_page=%2F&source=header-home"))
        startActivity(intent)
    }

    // functions
    // data에는 유저 이름을 받아오면 된다.
    private fun addDatabase(text: String): Task<String> {
        // Create the arguments to the callable function.

        return functions
            .getHttpsCallable("lic")
            .call(text)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
    }

}