package com.example.steynentertainment.ui.eventsTouring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.steynentertainment.R
import com.google.android.material.button.MaterialButton

// EventsAndTouringFragment is a Fragment class responsible for displaying information about events and touring.
class EventsAndTouringFragment : Fragment() {

    // onCreateView is called to create and return the view hierarchy associated with the fragment.
    // This method initializes UI components and sets up event-specific UI elements and data.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_events_and_touring, container, false)
        val getInTouchButton = view.findViewById<MaterialButton>(R.id.getInTouchBtn)

        // Set click listener on the button
        getInTouchButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_getInTouch)
        }
        return view
    }
}
