package com.example.steynentertainment.ui.aboutUs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentAboutusBinding

class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // onCreateView is called to create the view hierarchy associated with the fragment.
    // In this method, we inflate the fragment's layout, find and initialize buttons, and set their click listeners.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_aboutus, container, false)

        // Get reference to the daisies button
        val daisiesButton = view.findViewById<Button>(R.id.daisiesBtn)
        val inTheCityButton = view.findViewById<Button>(R.id.inTheCityBtn)
        val getInTouchButton = view.findViewById<AppCompatButton>(R.id.getInTouchBtn)
        val eventToursButton = view.findViewById<Button>(R.id.eventsAndTouringBtn)

        // Set click listener on the button
        daisiesButton.setOnClickListener {
            // Navigate to RockingTheDiasiesFragment
            findNavController().navigate(R.id.action_aboutUs_to_rockingTheDaisies)
        }

        // Set click listener in the city button
        inTheCityButton.setOnClickListener {
            // Navigate to InTheCityFragment
            findNavController().navigate(R.id.action_aboutUs_to_inTheCity)
        }

        // Set click listener in the EventsAndTours button
        eventToursButton.setOnClickListener {
            // Navigate to EventsAndTouringFragment
            findNavController().navigate(R.id.action_aboutUs_to_eventsAndTouring)
        }

        // Set click listener on get in touch button
        getInTouchButton.setOnClickListener {
            // Navigate to GetInTouchFragment
            findNavController().navigate(R.id.action_aboutUs_to_getInTouch)
        }
        return view
    }

    // onDestroyView is called when the view hierarchy associated with the fragment is being removed.
    // Here, we clean up resources by setting the binding object back to null.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
