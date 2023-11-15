package com.example.steynentertainment.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.steynentertainment.R
import com.bumptech.glide.Glide

class NewsAdapter(private val newsList: List<NewsArticle>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtNewsTitle)
        val timestampTextView: TextView = itemView.findViewById(R.id.txtNewsTimeStamp)
        val imageView: ImageView = itemView.findViewById(R.id.imgNews)
        val bodyTextView: TextView = itemView.findViewById(R.id.txtNewsBody)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_recycler_view, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsArticle = newsList[position]

        holder.titleTextView.text = newsArticle.title
        holder.timestampTextView.text = newsArticle.timestamp
        holder.bodyTextView.text = newsArticle.content

        // Load image using your preferred image-loading library (e.g., Glide, Picasso)
        // For example, using Glide:
        Glide.with(holder.itemView.context)
            .load(newsArticle.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}
