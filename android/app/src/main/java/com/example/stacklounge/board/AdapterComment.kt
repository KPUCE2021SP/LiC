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

            /* userphoto의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
          이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
            if (BoardCommentData.userphoto != "") {
                val resourceId = context!!.resources.getIdentifier(BoardCommentData.userphoto, "drawable", context.packageName)
                imgCommentUser?.setImageResource(resourceId)
            } else {
                imgCommentUser?.setImageResource(R.mipmap.ic_launcher)
            }

            /* TextView와 String 데이터를 연결한다. */

            tvcommentUser?.text = BoardCommentData.userId
            tvboardcomment?.text = BoardCommentData.boardCommment
            tvcommentTime?.text = BoardCommentData.commentTime

            // db
            val database = FirebaseDatabase.getInstance("https://stacklounge-62ffd-default-rtdb.asia-southeast1.firebasedatabase.app/") // 프로젝트 주소
            val userIdRef = database.getReference() // userId 불러오는 경로

            // firebase auth
            val user = Firebase.auth.currentUser

            userIdRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    //commentData.clear()
                    val cUserphoto = snapshot.child("current-user/${user?.uid}").child("avatar_url").value.toString() // 작성자 프사
                    Log.d("USER", tvcommentUser.toString())
                    Glide.with(itemView.context).load(cUserphoto).into(imgCommentUser!!)

                }

                override fun onCancelled(error: DatabaseError) {
                    //실패할 때

                }

            })

        }
    }

}

