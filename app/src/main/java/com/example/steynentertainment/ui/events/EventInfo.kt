package com.example.steynentertainment.ui.events

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentEventInfoBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EventInfo : Fragment() {

    private lateinit var visualAdapter: VisualsAdapter
    private var _binding: FragmentEventInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var popupRecyclerView: RecyclerView
    private lateinit var txtEvent: TextView
    private lateinit var newsAdapter: NewsAdapter

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEventInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val eventLogo = binding.imgEventLogo
        val eventVisual = binding.imgEventVisual
        val eventSnippet = binding.txtEventSnippet
        val btnUpcomingEvents = binding.btnUpcomingEvents
        val btnPreviousEvents = binding.btnPreviousEvents
        val btnNews = binding.btnEventNews
        val btnVisuals = binding.btnEventVisuals

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
                eventLogo.setImageResource(R.drawable.in_the_city)
                storageReference = storageReference.child("visuals/H5Pm9v6RcRh8EjqUna7N/inTheCity")
            }
            "E&T" -> {
                eventLogo.setImageResource(R.drawable.steynent_solid_logo)
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

        btnNews.setOnClickListener(){

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
                }.addOnFailureListener {
                    // Handle failure to get download URL
                }
            }
        }.addOnFailureListener {
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

        txtEvent = popupView.findViewById(R.id.txtPopupTitle)
        popupRecyclerView = popupView.findViewById(R.id.popupRecyclerView)

        // Create a PopupWindow
        val popup = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )

        // Set background color for the visuals_popup
        popup.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        // Display the visuals_popup
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
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }

    private fun updateNewsRecyclerView(newsList: List<NewsArticle>) {
        newsAdapter = NewsAdapter(newsList)
        popupRecyclerView.layoutManager = LinearLayoutManager(context)
        popupRecyclerView.adapter = newsAdapter
    }
}
