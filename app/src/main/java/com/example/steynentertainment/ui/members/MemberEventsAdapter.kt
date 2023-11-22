package com.example.steynentertainment.ui.members

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.steynentertainment.R
import com.squareup.picasso.Picasso

class MemberEventsAdapter(private var memberEvent: List<MemberEvents>):
RecyclerView.Adapter<MemberEventsAdapter.ViewHolder>(){


    // viewHolder Method Header
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtEventTitle)
        val dateTextView: TextView = itemView.findViewById(R.id.txtEventDate)
        val imageView: ImageView = itemView.findViewById(R.id.txtEventImage)
        val quicketButton: Button = itemView.findViewById(R.id.btnViewQuicket)
    }

    // onCreateViewHolder Method Header
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.member_event_recylcer_layout, parent, false)
        return ViewHolder(itemView)
    }

    // onBindViewHolder Method Header
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = memberEvent[position]
        holder.titleTextView.text = event.title
        holder.dateTextView.text = event.date

        // Load and display the image using Picasso
        Picasso.get().load(event.picture).into(holder.imageView)

        holder.quicketButton.setOnClickListener {
            val url = event.url
            if (url != null) {
                openUrlInBrowser(holder.itemView.context, url)
            }
        }
    }

    private fun openUrlInBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    fun updateData(newData: List<MemberEvents>) {
        memberEvent = newData
        notifyDataSetChanged()
    }

    // getItemCount Method Header
    override fun getItemCount(): Int {
        return memberEvent.size
    }
}
