package com.example.stacklounge.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stacklounge.R
import com.example.stacklounge.board.AdapterCommunityBoard
import com.example.stacklounge.board.BoardData
import com.example.stacklounge.board.BoardShowFeed
import com.example.stacklounge.board.BoardWriteFeed
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main_community.*
import kotlinx.android.synthetic.main.fragment_main_community.view.*


class FragmentMainCommunity : Fragment() {
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
        fragmentCommunity = container?.context
        val rAdapter = AdapterCommunityBoard(context,boardList) { BoardData ->
            val commentintent = Intent(activity, BoardShowFeed::class.java)
            commentintent.putExtra("index",boardList.indexOf(BoardData))

            Log.d("index",boardList.indexOf(BoardData).toString())

            commentintent.putExtra("title",BoardData.title)
            commentintent.putExtra("contents",BoardData.contents)
            commentintent.putExtra("feedTime",BoardData.feedTime)
            commentintent.putExtra("userId",BoardData.userId)
            startActivity(commentintent)
        }

        //당겨서 새로고침
        view.srl_main.setOnRefreshListener {
            // 사용자가 아래로 드래그 했다가 놓았을 때 호출
            // 이때 새로고침 화살표가 계속 돌아감 게시판 새로고침
            val ft = requireFragmentManager().beginTransaction()
            ft.detach(this).attach(this).commit()
            rAdapter.notifyDataSetChanged()
            srl_main.isRefreshing = false
        }

        // db에서 데이터를 가져와 community recycleview에 부착
        val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
        val userIdRef = database.reference // userId 불러오는 경로
        userIdRef.child("board").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                boardList.clear() // clear 안하면 기존list data + 기존 snapshot data + 추가된 snapshot data가 추가된다.. clear 필수
                for(postSnapshot in snapshot.children){
                    val get: BoardData? = postSnapshot.getValue(BoardData::class.java)

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
        rAdapter.notifyDataSetChanged()
        lm.reverseLayout=true // recyclerview에 최신 글부터 보여준다.
        lm.stackFromEnd = true // recyclerview를 맨위로 올려준다.

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

            R.id.menuBoardRefresh -> {
                // navigate to settings screen
                val lm = LinearLayoutManager(context)
                view?.boardRecycleview?.layoutManager = lm
                val ft = requireFragmentManager().beginTransaction()
                ft.detach(this).attach(this).commit()
                lm.stackFromEnd = false // recyclerview를 맨위로 올려준다.
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
