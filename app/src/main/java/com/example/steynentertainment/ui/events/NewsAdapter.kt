package com.example.steynentertainment.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steynentertainment.R

// NewsAdapter is a RecyclerView adapter for displaying a list of news articles.
class NewsAdapter(private val newsList: List<NewsArticle>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // NewsViewHolder is an inner class representing each item in the RecyclerView.
    // It holds references to the views in each news item.
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtNewsTitle)
        val timestampTextView: TextView = itemView.findViewById(R.id.txtNewsTimeStamp)
        val imageView: ImageView = itemView.findViewById(R.id.imgNews)
        val bodyTextView: TextView = itemView.findViewById(R.id.txtNewsBody)
    }

    // onCreateViewHolder is called by the RecyclerView to create new ViewHolder objects.
    // It inflates the layout for each news item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_recycler_view, parent, false)
        return NewsViewHolder(itemView)
    }

    // onBindViewHolder binds the data from the newsList to the views in the ViewHolder.
    // This method is called by RecyclerView to display the data at the specified position.
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsArticle = newsList[position]

        holder.titleTextView.text = newsArticle.title
        holder.timestampTextView.text = newsArticle.timestamp
        holder.bodyTextView.text = newsArticle.content

        Glide.with(holder.itemView.context)
            .load(newsArticle.imageUrl)
            .into(holder.imageView)
    }

    // getItemCount returns the total number of items in the news list.
    // This method is called by RecyclerView to get the size of the dataset.
    override fun getItemCount(): Int {
        return newsList.size
    }
}
