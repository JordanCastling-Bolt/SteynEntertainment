package com.example.steynentertainment.ui.events

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentEventInfoBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventInfo : Fragment() {

    private lateinit var visualAdapter: VisualsAdapter
    private var _binding: FragmentEventInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var popupRecyclerView: RecyclerView
    private lateinit var txtEvent: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var eventDetailsAdapter: EventDetailsAdapter

    private var storageReference = FirebaseStorage.getInstance().reference

    private lateinit var viewModel: EventInfoViewModel
    var event: String = ""

    companion object {
        fun newInstance(event: String): EventInfo {
            val fragment = EventInfo()
            val bundle = Bundle().apply {
                putString("event", event)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navController = findNavController()

        val eventLogo = binding.imgEventLogo
        val eventVisual = binding.imgEventVisual
        val eventSnippet = binding.txtEventSnippet
        val btnUpcomingEvents = binding.btnUpcomingEvents
        val btnPreviousEvents = binding.btnPreviousEvents
        val btnNews = binding.btnEventNews
        val btnVisuals = binding.btnEventVisuals
        val btnContactUs = binding.btnContactUs
        val btnMoreInfo = binding.btnMoreInfo

        // Extract event information from arguments
        event = arguments?.getString("event") ?: ""

        when (event) {
            "RTD" -> {
                eventLogo.setImageResource(R.drawable.rocking_daisies)
                eventVisual.setImageResource(R.drawable.daisies_event_visual)
                eventSnippet.text = "South Africa's biggest Music and Lifestyle experience is back in November 2023, " +
                    "brought to you by @steynent and @johnniewalkersa 🌼🇿🇦"
                storageReference = storageReference.child("visuals/vKsAOo87UEtGiDyGfvIf/rockingTheDaisies")
            }
            "ITC" -> {
                eventVisual.setImageResource(R.drawable.itc_event_visual)
                eventSnippet.text = "A series of boutique music and lifestyle events, each curated to tap into the diverse SA soundscape and beyond. " +
                    "Brought to you by @steynent 🇿🇦"
                eventLogo.setImageResource(R.drawable.in_the_city)
                storageReference = storageReference.child("visuals/H5Pm9v6RcRh8EjqUna7N/inTheCity")
            }
            "E&T" -> {
                eventLogo.setImageResource(R.drawable.steynent_solid_logo)
                eventVisual.setImageResource(R.drawable.events_touring_event_visual)
                eventSnippet.text = "Based in Johannesburg, Steyn Entertainment is a global multi-disciplinary organization committed to developing " +
                    "and connecting Africa to the rest of the world, in the arts and entertainment arenas.🇿🇦"
                storageReference = storageReference.child("visuals/pLsA5o87UFtGtDyJfkan/eventsAndTouring")
            }
            else -> Toast.makeText(context, "Error Fetching event info", Toast.LENGTH_SHORT).show()
        }

        btnVisuals.setOnClickListener {
            // Show the visuals popup
            showPopup()

            // Fetch image URLs from Firebase Storage
            fetchImageURLs()

            when (event) {
                "RTD" -> {
                    txtEvent.text = "Visuals for \nRocking the Daisies"
                }
                "ITC" -> {
                    txtEvent.text = "Visuals for \nIn The City"
                }
                "E&T" -> {
                    txtEvent.text = "Visuals for \nEvents & Touring"
                }
                else -> Toast.makeText(context, "Error Fetching event info", Toast.LENGTH_SHORT).show()
            }
        }

        btnNews.setOnClickListener() {
            showPopup()

            when (event) {
                "RTD" -> {
                    fetchNewsArticles("rockingTheDaisies")
                    txtEvent.text = "News Articles for \nRocking the Daisies"
                }
                "ITC" -> {
                    fetchNewsArticles("inTheCity")
                    txtEvent.text = "News Articles for \nIn The City"
                }
                "E&T" -> {
                    fetchNewsArticles("eventsAndTouring")
                    txtEvent.text = "News Articles for \nEvents & Touring"
                }
                else -> Toast.makeText(context, "Error Fetching event info", Toast.LENGTH_SHORT).show()
            }
        }

        btnPreviousEvents.setOnClickListener() {
            showPopup()

            when (event) {
                "RTD" -> {
                    fetchEventDetails("rockingTheDaisies", false)
                    txtEvent.text = "Previous Events for \nRocking the Daisies"
                }
                "ITC" -> {
                    fetchEventDetails("inTheCity", false)
                    txtEvent.text = "Previous Events for \nIn The City"
                }
                "E&T" -> {
                    fetchEventDetails("eventsAndTouring", false)
                    txtEvent.text = "Previous Events for \nEvents & Touring"
                }
                else -> Toast.makeText(context, "Error Fetching event info", Toast.LENGTH_SHORT).show()
            }
        }

        btnUpcomingEvents.setOnClickListener() {
            showPopup()

            when (event) {
                "RTD" -> {
                    fetchEventDetails("rockingTheDaisies", true)
                    txtEvent.text = "Upcoming Events for \nRocking the Daisies"
                }
                "ITC" -> {
                    fetchEventDetails("inTheCity", true)
                    txtEvent.text = "Upcoming Events for \nIn The City"
                }
                "E&T" -> {
                    fetchEventDetails("eventsAndTouring", true)
                    txtEvent.text = "Upcoming Events for \nEvents & Touring"
                }
                else -> Toast.makeText(context, "Error Fetching event info", Toast.LENGTH_SHORT).show()
            }
        }

        btnContactUs.setOnClickListener() {
            when (event) {
                "RTD" -> {
                    navController.navigate(R.id.navigation_getInTouch)
                }

                "ITC" -> {
                    navController.navigate(R.id.navigation_getInTouch)
                }

                "E&T" -> {
                    navController.navigate(R.id.navigation_getInTouch)
                }

                else -> Toast.makeText(context, "Error Fetching event info", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        btnMoreInfo.setOnClickListener() {
            when (event) {
                "RTD" -> {
                    navController.navigate(R.id.navigation_rockingTheDaisies)
                }

                "ITC" -> {
                    navController.navigate(R.id.navigation_inTheCity)
                }

                "E&T" -> {
                    navController.navigate(R.id.navigation_eventsAndTouring)
                }

                else -> Toast.makeText(context, "Error Fetching event info", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EventInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchImageURLs() {
        // Assuming you want to fetch URLs of all images in the specified folder
        storageReference.listAll().addOnSuccessListener { result ->
            val visualList = mutableListOf<String>()
            for (item in result.items) {
                // Get download URL for each image
                item.downloadUrl.addOnSuccessListener { uri ->
                    visualList.add(uri.toString())

                    // Update RecyclerView when all URLs are fetched
                    if (visualList.size == result.items.size) {
                        updateRecyclerView(visualList)
                    }
                    progressBar.visibility = View.GONE
                }.addOnFailureListener {
                    // Handle failure to get download URL
                }
            }
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            // Handle failure to list items in the specified folder
        }
    }

    private fun updateRecyclerView(visualList: List<String>) {
        visualAdapter = VisualsAdapter(visualList)
        popupRecyclerView.layoutManager = LinearLayoutManager(context)
        popupRecyclerView.adapter = visualAdapter
    }

    private fun showPopup() {
        // Inflate the popup_events layout
        val popupView = layoutInflater.inflate(R.layout.popup_events, null)

        // Initialize views from the popup layout
        txtEvent = popupView.findViewById(R.id.txtPopupTitle)
        popupRecyclerView = popupView.findViewById(R.id.popupRecyclerView)
        progressBar = popupView.findViewById(R.id.progressBar)

        // Make sure the progress bar is visible
        progressBar.visibility = View.VISIBLE

        // Create and display the PopupWindow
        val popup = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )
        popup.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        popup.showAtLocation(requireView(), Gravity.CENTER, 0, 0)
    }

    private fun fetchNewsArticles(event: String) {
        val firestore = FirebaseFirestore.getInstance()
        val newsCollection = firestore.collection("NewsArticles")

        newsCollection.whereEqualTo("category", event)
            .get()
            .addOnSuccessListener { documents ->
                val newsList = mutableListOf<NewsArticle>()
                for (document in documents) {
                    val category = document.getString("category") ?: ""
                    val content = document.getString("content") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val timestamp = document.getTimestamp("timestamp")?.toDate()?.toString() ?: ""
                    val title = document.getString("title") ?: ""

                    val newsArticle = NewsArticle(category, content, imageUrl, timestamp, title)
                    newsList.add(newsArticle)
                }

                updateNewsRecyclerView(newsList)
                progressBar.visibility = View.GONE // Hide progress bar after loading
            }
            .addOnFailureListener { exception ->
                // Handle failure
                progressBar.visibility = View.GONE // Hide progress bar on failure
            }
    }

    private fun updateNewsRecyclerView(newsList: List<NewsArticle>) {
        newsAdapter = NewsAdapter(newsList)
        popupRecyclerView.layoutManager = LinearLayoutManager(context)
        popupRecyclerView.adapter = newsAdapter
    }

    private fun fetchEventDetails(event: String, upcomingEvents: Boolean) {
        val firestore = FirebaseFirestore.getInstance()
        val eventsCollection = firestore.collection("Events")

        val currentDate = getCurrentDate()

        val query = if (upcomingEvents) {
            // Fetch events in the future or today
            eventsCollection
                .whereEqualTo("category", event)
                .whereGreaterThanOrEqualTo("date", currentDate)
        } else {
            // Fetch events in the past
            eventsCollection
                .whereEqualTo("category", event)
                .whereLessThan("date", currentDate)
        }

        query.get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Handle case where no events are found for the specified category
                    Toast.makeText(popupRecyclerView.context, "No events found", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    return@addOnSuccessListener
                }

                val eventDetailsList = mutableListOf<EventDetails>()
                for (document in documents) {
                    val title = document.getString("title") ?: ""
                    val date = document.getString("date") ?: ""
                    val description = document.getString("description") ?: ""
                    val picture = document.getString("picture") ?: ""
                    val url = document.getString("url") ?: ""
                    val ticketUrl = document.getString("ticketUrl") ?: ""

                    val eventDetails = EventDetails(title, date, description, picture, url, ticketUrl)
                    eventDetailsList.add(eventDetails)
                }

                updateEventDetailsRecyclerView(eventDetailsList)
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                // Log the failure reason
                Log.e("EventInfoFragment", "Failed to fetch events", exception)
                progressBar.visibility = View.GONE
            }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun updateEventDetailsRecyclerView(eventDetailsList: List<EventDetails>) {
        eventDetailsAdapter = EventDetailsAdapter(eventDetailsList)
        popupRecyclerView.layoutManager = LinearLayoutManager(context)
        popupRecyclerView.adapter = eventDetailsAdapter
    }
}
