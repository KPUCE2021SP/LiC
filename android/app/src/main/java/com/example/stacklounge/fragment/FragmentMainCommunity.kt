package com.example.stacklounge.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.stacklounge.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main_community.view.*


class FragmentMainCommunity : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_main_community, null)

        val userId : String = "bjo6300"
        val uid : String = "123456"
        var boardNumber : Int = 0
        var boardContents : String = "hello firebase"

        // 게시판 hashmap
        val itemMap = hashMapOf(
            "user" to userId,
            "uid" to uid,
            "number" to boardNumber,
            "contents" to boardContents
        )

//        //데이터 가져오기
//        val database = Firebase.database
//        val itemsRef = database.getReference("userlist")
//        itemsRef.child("123").child("name")

        view.btnBoardCreate.setOnClickListener {
            val database = Firebase.database
            val itemsRef = database.getReference("userlist")
            val itemRef = itemsRef.push()
            itemRef.setValue(itemMap)
        }

        view.btnBoardDelete.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("message")

            myRef.setValue("Hello, World!")
        }

        view.btnBoardUpdate.setOnClickListener {

        }

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