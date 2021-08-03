package com.example.stacklounge

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.substring
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.stacklounge.databinding.ActivityMainBinding
import com.example.stacklounge.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        isFirebaseConnected() // firebase 연결되있는지 확인

        btnLogout.setOnClickListener{
            // 로그아웃하고 다시 LoginActivity로 이동
            Firebase.auth.signOut()

            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

        val user = Firebase.auth.currentUser
        //Toast.makeText(applicationContext,"${user?.email}", Toast.LENGTH_SHORT).show()
        val userName = user?.email?.substring(0, user.email?.indexOf("@")!!)
        Log.d("username = $userName","userName")

        val githubTopic = githubTopics()
        githubTopic.start()


    }

    // jsoup를 이용해 사용자의 github topics 크롤링
    class githubTopics : Thread(){
        override fun run() {
            // github email에서 id만 추출
            val user = Firebase.auth.currentUser
            val userName = user?.email?.substring(0, user.email?.indexOf("@")!!)

            // userName에 따른 topic html 파싱
            val doc: Document = Jsoup.connect("https://github.com/stars/$userName/topics").get()

            val mElementDatas: Elements =
                doc.select("ul[class=\"repo-list list-style-none js-navigation-container js-active-navigation-container\"]")
                    .select("li[class=\"d-md-flex flex-justify-between py-4 border-bottom\"] a")

            var mDatas = mElementDatas.toString()

            val startString = "<a href=\"/topics/"
            val endString = "\" class"
            var topicList = arrayOf<String>()
            var selectedTopic : String = ""
            var i = 0

            while (true) {
                //Log.d("startString = ${mDatas.indexOf(startString)}", "startString")
                if(mDatas.indexOf(startString) == -1){
                    break
                }
                else{
                    selectedTopic = mDatas.substring(mDatas.indexOf(startString),
                        mDatas.indexOf(endString)) // <a href="/topics/rest-api
                    //Log.d("selectedTopic", "${selectedTopic}")
                    // 문자열 분리
                    var topic = selectedTopic.split("/")

                    // 재귀로 해서 데이터가 커져 느리고 오류가 난다...
                    //mDatas = mDatas.substring(mDatas.indexOf("class=\"d-flex flex-md-items-center flex-auto no-underline\""))

                    topicList = topicList.plus(topic[2])



                }
            }
            Log.d("topic", topicList[0])
//            Log.d("topic", topicList[1])
//            Log.d("topic", topicList[2])
        }
    }

    //firebase 연결됐는지 체크
    private fun isFirebaseConnected(){
        if (Firebase.auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }
    }
}