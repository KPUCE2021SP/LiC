package com.example.stacklounge.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main_community.view.*


class FragmentMainCommunity : Fragment() {

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

        //글쓰기 xml 들어가기
        fragmentCommunity = container?.getContext()


        // recycleview에 연결되는 리스트
        var boardList = arrayListOf<BoardData>()

        //board recycleview를 community에 부착
        val rAdapter = AdapterCommunityBoard(context,boardList) { BoardData ->
            // 아이템 클릭했을 때 필요한 동작
            // 게시글 클릭했을때 fragment로 이동시켜야함.
            Toast.makeText(fragmentCommunity, BoardData.feedTime, Toast.LENGTH_SHORT).show()
            val commentintent = Intent(activity, BoardShowFeed::class.java)
            startActivity(commentintent)
        }


        // db에서 데이터를 가져와 community recycleview에 부착
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
        val userIdRef = database.getReference() // userId 불러오는 경로

        userIdRef.child("board").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){

                    val get: BoardData? = postSnapshot.getValue(BoardData::class.java)
                    Log.d("get", get.toString())


                    var addtitle = get?.title.toString()
                    var addcontents = get?.contents.toString()
                    var adduserid  =  get?.userId.toString()
                    var addboardTime = get?.feedTime.toString()

                    if(adduserid!=""){
                        boardList.addAll(listOf(BoardData(addtitle,addcontents,addboardTime,adduserid)))
                        rAdapter.notifyDataSetChanged()
                    }
                    else{
                        continue
                    }

                }


            }
            override fun onCancelled(error: DatabaseError) {
                //실패할 때

            }

        })

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
    


}
