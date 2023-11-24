package com.example.steynentertainment.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steynentertainment.R

// NewsArticle is a data class representing the details of a news article.
// As a data class in Kotlin, it primarily serves to hold data and automatically provides functionality like equals, hashCode, and toString.
class VisualsAdapter(private val visualList: List<String>) :
    RecyclerView.Adapter<VisualsAdapter.VisualViewHolder>() {

    class VisualViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgVisual)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisualViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.visuals_recycler_view, parent, false)
        return VisualViewHolder(view)
    }

    override fun onBindViewHolder(holder: VisualViewHolder, position: Int) {
        // Assuming visualList contains URLs of the images
        Glide.with(holder.itemView.context)
            .load(visualList[position])
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return visualList.size
    }
}
