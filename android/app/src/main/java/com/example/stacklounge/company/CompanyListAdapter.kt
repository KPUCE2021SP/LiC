package com.example.stacklounge.company

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stacklounge.GetCompanyAllQuery
import com.example.stacklounge.R

class CompanyListAdapter(
    private val companies : List<GetCompanyAllQuery.Edge?>
) : RecyclerView.Adapter<CompanyListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.frame_company, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val company = companies[position]
        val logo = company?.node?.companyLogo ?: ""
        if(logo == ""){
            holder.companyTextImage.setImageResource(R.drawable.stackloungeicon)
        }
        else {
            Glide.with(holder.itemView.context).load(logo)
                .into(holder.companyTextImage)
        }
        holder.companyTextView.text = company?.node?.companyName ?: ""
        holder.companyTechStackView.text = company?.node?.techStack?.toString() ?: ""
    }

    override fun getItemCount(): Int {
        return companies.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val companyTextImage : ImageView = view.findViewById(R.id.companyTextImage)
        val companyTextView : TextView = view.findViewById(R.id.companyTextView)
        val companyTechStackView : TextView = view.findViewById(R.id.techStackTextView)
    }
}