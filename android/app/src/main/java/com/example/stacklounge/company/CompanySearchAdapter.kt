package com.example.stacklounge.company

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stacklounge.GetCompanyByNameQuery
import com.example.stacklounge.R

class CompanySearchAdapter(
    private val query : GetCompanyByNameQuery.CompanyByName,
) : RecyclerView.Adapter<CompanySearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.frame_company, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val company = query.companyName
        val techStack = query.techStack
        val logo = query.companyLogo
        if(logo == ""){
            holder.companyTextImage.setImageResource(R.drawable.stackloungeicon)
        }
        else {
            Glide.with(holder.itemView.rootView).load(logo)
                .into(holder.companyTextImage)
        }
        holder.companyTextView.text = company.toString() ?: ""
        holder.companyTechStackView.text = "확인된 기술 도구: ${techStack?.size}개"?: ""
    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val companyTextImage : ImageView = view.findViewById(R.id.companyTextImage)
        val companyTextView : TextView = view.findViewById(R.id.companyTextView)
        val companyTechStackView : TextView = view.findViewById(R.id.techStackTextView)
    }
}