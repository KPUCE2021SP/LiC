package com.example.stacklounge.board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stacklounge.R

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

        }
    }

}

