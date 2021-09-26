package com.example.stacklounge.company

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
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
        holder.companyTechStackView.text = "확인된 기술 도구: ${company?.node?.techStack?.size}개"
        val companyTechList = company?.node?.techStack as Collection<String>
        val cover = arrayListOf<String>()
        cover.addAll(companyTechList)
        Log.d("COVER", cover.toString())
        holder.cardView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CompanyDetail::class.java).apply {
                putExtra("CompanyName", company?.node?.companyName)
                putExtra("CompanyTechList", cover)
                putExtra("CompanyImage", logo)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return companies.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val companyTextImage : ImageView = view.findViewById(R.id.companyTextImage)
        val companyTextView : TextView = view.findViewById(R.id.companyTextView)
        val companyTechStackView : TextView = view.findViewById(R.id.techStackTextView)
        val cardView : CardView = view.findViewById(R.id.companyCardView)
    }
}