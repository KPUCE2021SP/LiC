package com.example.stacklounge.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stacklounge.R
import com.example.stacklounge.board.AdapterCommunityBoard
import com.example.stacklounge.board.BoardData
import com.example.stacklounge.board.BoardShowFeed
import com.example.stacklounge.board.BoardWriteFeed
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main_community.view.*


class FragmentMainCommunity : Fragment() {

    private var fragmentCommunity : Context? = null

    val user = Firebase.auth.currentUser

    val userId : String = "bjo6300" // functions에서 받아올 예정
    val uid = user?.uid
    var boardNumber : Int = 0 // 글 번호
    var boardContents : String = "hello firebase" // 글 내용
    var title = "title" // 글 제목

    // 게시판 hashmap
    val userInfo = hashMapOf(
        "user" to userId,
        "uid" to uid,
        "number" to boardNumber,
        "contents" to boardContents,
        "title" to title
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //appbar랑 메뉴xml 연결
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_main_community, null)

        fragmentCommunity = container?.getContext()


        view.btnBoardCreate.setOnClickListener {
            //메인 쓰레드 말고 다른 쓰레드 사용
            object : Thread(){
                override fun run() {
                    // realtimeDB에 추가
                    adduserInfoinDB()
                    boardNumber++
                }
            }.start()
        }

        view.btnBoardDelete.setOnClickListener {
            object : Thread(){
                override fun run() {
                    // DB 삭제
                    deleteuserInfoinDB()
                }
            }.start()
        }

        view.btnBoardUpdate.setOnClickListener {

        }

        // 데이터를 db에서 가져와야한다.
        var boardList = arrayListOf<BoardData>(
            BoardData("firsttitle","firstcontents","12:01"),
            BoardData("second","secondcontentsasdlfkjweoirjozxlkfv;sdfdsflaksdjf;lkajsdfl;kaj;sldkfj;ladsjf;lkasjd;lfkja;lkdfj;aksdjf;aksdjf;ldskfjas;kldfj;aldskf","12:02"),
            BoardData("third","thirdcontents","12:03"),
            BoardData("fourth","fourthcontents","12:04")

        )

        //board recycleview를 community에 부착
        val rAdapter = AdapterCommunityBoard(context,boardList) { BoardData ->
            // 아이템 클릭했을 때 필요한 동작
            // 게시글 클릭했을때 fragment로 이동시켜야함.
            Toast.makeText(fragmentCommunity, "${BoardData.boardTime}", Toast.LENGTH_SHORT).show()
            val commentintent = Intent(activity, BoardShowFeed::class.java)
            startActivity(commentintent)
        }

        rAdapter.notifyDataSetChanged() //어댑터의 데이터가 변했다는 notify를 날린다
        view.boardRecycleview.adapter = rAdapter

        val lm = LinearLayoutManager(context)
        view.boardRecycleview.layoutManager = lm
        view.boardRecycleview.setHasFixedSize(true)

        return view
    }

    //appbar 메뉴
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.community_menu, menu)
    }

    //appbar 메뉴 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuBoardWrite -> {
                //Toast.makeText(activity,"메뉴",Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, BoardWriteFeed::class.java)
                startActivity(intent)
                true
            }

            R.id.menuBoardAddStar -> {
                // navigate to settings screen
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    
    fun adduserInfoinDB(){
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
        val userRef = database.getReference("board/$uid") // realtime db 경로
        userRef.setValue(userInfo)
    }

    fun deleteuserInfoinDB() {
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val userRef = database.getReference("board/$uid")
        if (uid != null) {
            userRef.removeValue()
        }
    }

}
