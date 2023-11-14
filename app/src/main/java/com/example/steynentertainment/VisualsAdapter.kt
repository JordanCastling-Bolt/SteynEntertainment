package com.example.steynentertainment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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
