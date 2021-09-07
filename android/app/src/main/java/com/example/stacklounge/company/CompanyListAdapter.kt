package com.example.stacklounge.company

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textservice.TextInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        holder.imageView.text = company?.node?.companyName ?: ""
    }

    override fun getItemCount(): Int {
        return companies.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView : TextView
        init {
            // Define click listener for the ViewHolder's View.
            imageView = view.findViewById(R.id.companyImageView)
        }
    }
}