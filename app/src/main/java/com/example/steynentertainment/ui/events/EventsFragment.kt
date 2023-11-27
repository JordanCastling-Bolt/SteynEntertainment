package com.example.steynentertainment.ui.events

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentEventsBinding

// EventsFragment is a Fragment class responsible for displaying information about various events.
class EventsFragment : Fragment() {

    // A non-null getter for the binding which is valid between onCreateView and onDestroyView.
    private var _binding: FragmentEventsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // onCreateView is called to create and return the view hierarchy associated with the fragment.
    // This method initializes UI components and sets up event-specific UI elements and data.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("EventsFragment", "onCreateView called")
        val eventsViewModel =
            ViewModelProvider(this).get(EventsViewModel::class.java)

        _binding = FragmentEventsBinding.inflate(inflater, container, false)

        val navController = findNavController()

        // ------------------Loading the Rocking The Daisies logo and description--------------------

        val imgRTD = binding.imgRTD
        imgRTD.setImageResource(R.drawable.rocking_daisies)

        val txtRTD = binding.txtRTDPreview
        txtRTD.text = "You know the pure, simplistic joy of finding a forgotten loose R100 in the pocket of your old jeans while doing your laundry? " +
            "Yeah, that feeling is Daisies; but with a lot less detergent, plenty of sun and the best live soundtrack you have ever heard."

        val viewRTD = binding.btnViewRTD

        viewRTD.setOnClickListener {
            val eventInfo = EventInfo.newInstance("RTD")
            navController.navigate(R.id.navigation_eventInfo, eventInfo.arguments)
        }
        // ------------------------------------------------------------------------------------------

        // -----------------------Loading the In The City logo and description-----------------------

        val imgITC = binding.imgITC
        imgITC.setImageResource(R.drawable.in_the_city)

        val txtITC = binding.txtITCPreview
        txtITC.text = "Welcome to ‘In the City’, where the heartbeat of South Africa comes alive through the power of music. No matter how diverse our " +
            "backgrounds may be, there is a rhythmic thread that binds us together."

        val viewITC = binding.btnViewITC

        viewITC.setOnClickListener {
            val eventInfo = EventInfo.newInstance("ITC")
            navController.navigate(R.id.navigation_eventInfo, eventInfo.arguments)
        }
        // ------------------------------------------------------------------------------------------

        // -----------------------Loading the Events & Touring logo and description------------------

        val imgEventsTouring = binding.imgEventsTour
        imgEventsTouring.setImageResource(R.drawable.steynent_solid_logo)

        val txtEventsTouring = binding.txtEventsTourPreview
        txtEventsTouring.text = "Steyn Entertainment plays host to various events. Click view the dets to view them all."

        val viewEventsTouring = binding.btnViewEventsTour

        viewEventsTouring.setOnClickListener {
            val eventInfo = EventInfo.newInstance("E&T")
            navController.navigate(R.id.navigation_eventInfo, eventInfo.arguments)
        }

        // ------------------------------------------------------------------------------------------

        return binding.root
    }

    // onDestroyView is called when the view hierarchy associated with the fragment is being removed.
    // It's used here to clean up the binding reference.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
