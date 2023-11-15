package com.example.steynentertainment.ui.events

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.steynentertainment.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventDetailsAdapter(private val eventDetailsList: List<EventDetails>) :
    RecyclerView.Adapter<EventDetailsAdapter.EventDetailsViewHolder>() {

    inner class EventDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtEventTitle)
        val dateTextView: TextView = itemView.findViewById(R.id.txtEventDate)
        val imageView: ImageView = itemView.findViewById(R.id.imgEvent)
        val bodyTextView: TextView = itemView.findViewById(R.id.txtEventDescription)
        val viewWebsite: Button = itemView.findViewById(R.id.btnEventWebsite)
        val purchaseTickets: Button = itemView.findViewById(R.id.btnPurchaseTicket)
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

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(eventDetails.picture)
            .into(holder.imageView)

        // Open the web page in a browser when the "View Website" button is clicked
        holder.viewWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventDetails.url))
            holder.itemView.context.startActivity(intent)
        }

        // Check if the event date is smaller (earlier) than the current date
        val currentDate = getCurrentDate()
        val isEventDatePast = eventDetails.date < currentDate

        // Show or hide the "Purchase Tickets" button based on the date comparison
        if (isEventDatePast) {
            // Event date is in the past, hide the button
            holder.purchaseTickets.visibility = View.GONE
        } else {
            // Event date is in the future, show the button
            holder.purchaseTickets.visibility = View.VISIBLE
        }

        // Set a click listener for the "Purchase Tickets" button
        holder.purchaseTickets.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventDetails.ticketUrl))
            holder.itemView.context.startActivity(intent)
        }

    }

    // Helper function to get the current date in the same format as event dates
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }


    override fun getItemCount(): Int {
        // Return the size of the dataset
        return eventDetailsList.size
    }
}
