package com.example.stacklounge.company

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textservice.TextInfo
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stacklounge.GetCompanyAllQuery
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
        holder.companyTextView.text = company.toString() ?: ""
        holder.companyTechStackView.text = techStack.toString() ?: ""
    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val companyTextView : TextView = view.findViewById(R.id.companyTextView)
        val companyTechStackView : TextView = view.findViewById(R.id.techStackTextView)
    }
}