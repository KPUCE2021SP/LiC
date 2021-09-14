package com.example.stacklounge.board

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stacklounge.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class AdapterComment (val context: Context?, val BoardCommentData: ArrayList<BoardCommentData>) :
    RecyclerView.Adapter<AdapterComment.Holder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_board_show_comment, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(BoardCommentData[position], context)
    }

    override fun getItemCount(): Int {
        return BoardCommentData.size
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tvcommentUser = itemView?.findViewById<TextView>(R.id.tvcommentUser)
        val tvboardcomment = itemView?.findViewById<TextView>(R.id.tvboardcomment)
        val tvcommentTime = itemView?.findViewById<TextView>(R.id.tvcommentTime)
        val imgCommentUser = itemView?.findViewById<ImageView>(R.id.imgCommentUser)

        fun bind (BoardCommentData: BoardCommentData, context: Context?) {

            /* TextView와 String 데이터를 연결한다. */

            tvcommentUser?.text = BoardCommentData.userId
            tvboardcomment?.text = BoardCommentData.boardCommment
            tvcommentTime?.text = BoardCommentData.commentTime

            val boardpath = "${BoardCommentData.commentTime}+${BoardCommentData.userId}"
            val user = Firebase.auth.currentUser

             //게시글 이미지 세팅
        val database1 = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        database1.child("current-user")
            .child("${user?.uid}")
            .child("avatar_url")
            .get().addOnSuccessListener {
                val avatarImage = it.value as String
                Glide.with(itemView.context).load(avatarImage).into(imgCommentUser!!)
            }

        }
    }

}

