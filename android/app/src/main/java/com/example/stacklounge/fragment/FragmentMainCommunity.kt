package com.example.stacklounge.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stacklounge.R
import com.example.stacklounge.board.AdapterCommunityBoard
import com.example.stacklounge.board.BoardData
import com.example.stacklounge.board.BoardShowFeed
import com.example.stacklounge.board.BoardWriteFeed
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main_community.view.*


class FragmentMainCommunity : Fragment() {
    lateinit var context_main : Context

    // recycleview에 연결되는 리스트
    var boardList = arrayListOf<BoardData>()


    private var fragmentCommunity : Context? = null

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
        Log.d("보드리스트", "${boardList.size}")

        context_main = this.requireActivity();
        //글쓰기 xml 들어가기
        fragmentCommunity = container?.getContext()



        //board recycleview를 community에 부착
        val rAdapter = AdapterCommunityBoard(context,boardList) { BoardData ->
            // 아이템 클릭했을 때 필요한 동작

            val commentintent = Intent(activity, BoardShowFeed::class.java)

            // index 값 BoardShowFeed에 넘겨준다.
            commentintent.putExtra("index",boardList.indexOf(BoardData))
            Log.d("index",boardList.indexOf(BoardData).toString())

            commentintent.putExtra("title",BoardData.title)
            commentintent.putExtra("contents",BoardData.contents)
            commentintent.putExtra("feedTime",BoardData.feedTime)
            commentintent.putExtra("userId",BoardData.userId)

            startActivity(commentintent)
        }


        // db에서 데이터를 가져와 community recycleview에 부착
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
        val userIdRef = database.getReference() // userId 불러오는 경로

        userIdRef.child("board").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                boardList.clear() // 기존list data + 기존 snapshot data + 추가된 snapshot data가 추가된다.. clear 필수
                for(postSnapshot in snapshot.children){

                    val get: BoardData? = postSnapshot.getValue(BoardData::class.java)
                    Log.d("get", get.toString())


                    val addtitle = get?.title.toString()
                    val addcontents = get?.contents.toString()
                    val adduserid  =  get?.userId.toString()
                    val addboardTime = get?.feedTime.toString()

                    if(adduserid!=""){
                        boardList.add((BoardData(addtitle,addcontents,addboardTime,adduserid)))
                        Log.d("BOARDLIST", "${boardList.size}")
                        rAdapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //실패할 때

            }

        })
        view.boardRecycleview.adapter = rAdapter

        val lm = LinearLayoutManager(context)
        view.boardRecycleview.layoutManager = lm
        view.boardRecycleview.setHasFixedSize(true)
        rAdapter.notifyDataSetChanged() //어댑터의 데이터가 변했다는 notify를 날린다

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
}
