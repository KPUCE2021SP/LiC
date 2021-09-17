package com.example.stacklounge.board

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stacklounge.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class AdapterCommunityBoard(val context: Context?, val BoardData: ArrayList<BoardData>, val itemClick: (BoardData) -> Unit) :

    RecyclerView.Adapter<AdapterCommunityBoard.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_community_board_recycleview, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //onCreateViewHolder에서 만든 view와 실제 입력되는 각각의 데이터를 연결한다.
        //ViewHolder에서 데이터 묶는 함수가 실행되는 곳
        holder.bind(BoardData[position], context)
    }

    override fun getItemCount(): Int {

        return BoardData.size
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tvboardTitle = itemView?.findViewById<TextView>(R.id.tvboardTitle)
        val tvboardContent = itemView?.findViewById<TextView>(R.id.tvboardContent)
        val tvboardTime = itemView?.findViewById<TextView>(R.id.tvboardTime)
        val tvuserID = itemView?.findViewById<TextView>(R.id.tvuserID)



        fun bind (BoardData: BoardData, context: Context?) {

            /* TextView와 String 데이터를 연결한다. */

            tvboardTitle?.text = BoardData.title
            tvboardContent?.text = BoardData.contents
            tvboardTime?.text = BoardData.feedTime
            tvuserID?.text = "|" + BoardData.userId


            itemView.setOnClickListener { itemClick(BoardData) } // 아이템(게시글)을 클릭했을 때
        }
    }


}