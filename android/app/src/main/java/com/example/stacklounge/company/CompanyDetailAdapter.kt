package com.example.stacklounge.company

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stacklounge.GetToolByNameQuery
import com.example.stacklounge.R
import com.example.stacklounge.tool.ToolDetail
import com.example.stacklounge.tool.ToolSubActivity
import org.w3c.dom.Text

class CompanyDetailAdapter(
    private val tools : MutableList<GetToolByNameQuery.Data?>
) : RecyclerView.Adapter<CompanyDetailAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompanyDetailAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_tool_sub_no_description, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompanyDetailAdapter.ViewHolder, position: Int) {
        val tool = tools[position]
        holder.toolName.text = tool?.toolByName?.name
        holder.toolTitle.text = tool?.toolByName?.title
        Glide.with(holder.itemView.context).load(tool?.toolByName?.imageUrl).into(holder.toolImage)
        holder.detailButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, ToolDetail::class.java)
            intent.putExtra("name", tool?.toolByName?.name)
            intent.putExtra("title", tool?.toolByName?.title)
            intent.putExtra("image", tool?.toolByName?.imageUrl)
            intent.putExtra("oss", tool?.toolByName?.ossRepo)
            intent.putExtra("canonical", tool?.toolByName?.canonicalUrl)
            intent.putExtra("description", tool?.toolByName?.description)
            intent.putExtra("web", tool?.toolByName?.websiteUrl)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return tools.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val toolImage : ImageView = view.findViewById(R.id.toolImage)
        val toolName : TextView = view.findViewById(R.id.toolName)
        val toolTitle : TextView = view.findViewById(R.id.toolTitle)
        val detailButton : Button = view.findViewById(R.id.toolDetailButton)
    }
}