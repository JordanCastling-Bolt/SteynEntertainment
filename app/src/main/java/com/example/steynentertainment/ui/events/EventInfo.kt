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
import com.example.steynentertainment.VisualsAdapter
import com.example.steynentertainment.databinding.FragmentEventInfoBinding
import com.google.firebase.storage.FirebaseStorage

class EventInfo : Fragment() {

    private lateinit var visualAdapter: VisualsAdapter
    private var _binding: FragmentEventInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var visualRecyclerView: RecyclerView
    private lateinit var txtEventVisual: TextView

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
        val btnUpcomingEvents = binding.btnUpcomingEvents
        val btnPreviousEvents = binding.btnPreviousEvents
        val btnNews = binding.btnEventNews
        val btnVisuals = binding.btnEventVisuals

        // Extract event information from arguments
        event = arguments?.getString("event") ?: ""


        when (event) {
            "RTD" -> {
                eventLogo.setImageResource(R.drawable.rocking_daisies)
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
            showVisualsPopup()

            // Fetch image URLs from Firebase Storage
            fetchImageURLs()

            when (event) {
                "RTD" -> {
                    txtEventVisual.text = "Visuals for \nRocking the Daisies"
                }
                "ITC" -> {
                    txtEventVisual.text = "Visuals for \nIn The City"
                }
                "E&T" -> {
                    txtEventVisual.text = "Visuals for \nEvents & Touring"
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
        visualRecyclerView.layoutManager = LinearLayoutManager(context)
        visualRecyclerView.adapter = visualAdapter
    }

    private fun showVisualsPopup() {
        // Inflate the popup_events layout
        val visualsPopupView = layoutInflater.inflate(R.layout.popup_events, null)

        txtEventVisual = visualsPopupView.findViewById(R.id.txtPopupTitle)
        visualRecyclerView = visualsPopupView.findViewById(R.id.popupRecyclerView)

        // Create a PopupWindow
        val visualsPopup = PopupWindow(
            visualsPopupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            true
        )

        // Set background color for the visuals_popup
        visualsPopup.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        // Display the visuals_popup
        visualsPopup.showAtLocation(requireView(), Gravity.CENTER, 0, 0)
    }
}
