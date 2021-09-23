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

class AdapterComment(val context: Context?, val BoardCommentData: ArrayList<BoardCommentData>) :
    RecyclerView.Adapter<AdapterComment.Holder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.activity_board_show_comment, parent, false)
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

        fun bind(BoardCommentData: BoardCommentData, context: Context?) {

            /* TextView와 String 데이터를 연결한다. */

            tvcommentUser?.text = BoardCommentData.userId
            Log.d("commentuserid", BoardCommentData.userId)
            tvboardcomment?.text = BoardCommentData.boardCommment
            tvcommentTime?.text = BoardCommentData.commentTime

            val user = Firebase.auth.currentUser
            val database =
                FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
            val userIdRef = database.getReference()


            //게시글 이미지 세팅
            var commentTime = BoardCommentData.commentTime
            var commentuserID = BoardCommentData.userId
            var commentPath = "$commentTime+$commentuserID"
            Log.d("commentPath", commentPath)

            userIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.child("board").children) {

                        val key = postSnapshot.key.toString() // 게시글 들어갈때 댓글 경로 + commentNumber
                        Log.d("commentkey", key)

                        //val get: BoardCommentData? = postSnapshot.getValue(com.example.stacklounge.board.BoardCommentData::class.java)

                        if (key.contains("+")) {

                            var keysplitArray = key.split("+")
                            var commentID = keysplitArray[1]
                            Log.d("commentID", commentID)

                            if(commentID.equals(BoardCommentData.userId)) {
                                val database1 =
                                    FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
                                database1.child("board")
                                    .child(key)
                                    .child("userphoto")
                                    .get().addOnSuccessListener {
                                        val avatarImage = it.value as String
                                        Glide.with(itemView.context).load(avatarImage)
                                            .into(imgCommentUser!!)
                                    }
                            }
//                            if (commentID.equals(BoardCommentData.userId)) {
//                                userIdRef.addListenerForSingleValueEvent(object :
//                                    ValueEventListener {
//                                    override fun onDataChange(snapshot: DataSnapshot) {
//                                        for (postSnapshot2 in snapshot.child("board/$commentPath/comment").children) {
//
//                                            val commentphoto =
//                                                snapshot.child("current-user/${user?.uid}")
//                                                    .child("login").value.toString() // 댓글 작성자
//
//                                            val key =
//                                                postSnapshot.key.toString() // 게시글 들어갈때 댓글 경로 + commentNumber
//                                            Log.d("commentkey", key)
//
//                                            val get: BoardCommentData? =
//                                                postSnapshot.getValue(com.example.stacklounge.board.BoardCommentData::class.java)
//
//
//                                        }
//
//                                    }
//
//                                    override fun onCancelled(error: DatabaseError) {
//
//                                    }
//
//                                })
//                            }


                        } else {
                            continue
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

//            val database1 = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
//            database1.child("current-user")
//                .child("${user?.uid}")
//                .child("avatar_url")
//                .get().addOnSuccessListener {
//                    val avatarImage = it.value as String
//                    Glide.with(itemView.context).load(avatarImage).into(imgCommentUser!!)
//                }

        }
    }

}

