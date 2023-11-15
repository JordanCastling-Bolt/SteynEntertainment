package com.example.steynentertainment.ui.events

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steynentertainment.R

class EventDetailsAdapter(private val eventDetailsList: List<EventDetails>) :
    RecyclerView.Adapter<EventDetailsAdapter.EventDetailsViewHolder>() {

    inner class EventDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtEventTitle)
        val dateTextView: TextView = itemView.findViewById(R.id.txtEventDate)
        val imageView: ImageView = itemView.findViewById(R.id.imgEvent)
        val bodyTextView: TextView = itemView.findViewById(R.id.txtEventDescription)
        val viewWebsite: Button = itemView.findViewById(R.id.btnEventWebsite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailsViewHolder {
        // Create and return a new ViewHolder
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.events_recycler_view, parent, false)
        return EventDetailsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventDetailsViewHolder, position: Int) {
        val eventDetails = eventDetailsList[position]

        holder.titleTextView.text = eventDetails.title
        holder.dateTextView.text = eventDetails.date
        holder.bodyTextView.text = eventDetails.description

        Glide.with(holder.itemView.context)
            .load(eventDetails.picture)
            .into(holder.imageView)

        holder.viewWebsite.setOnClickListener {
            // Open the web page in a browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventDetails.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        // Return the size of the dataset
        return eventDetailsList.size
    }
}
