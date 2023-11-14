package com.example.steynentertainment.ui.events

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentEventsBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        val imgRTD = binding.imgRTD
        val rtdImg = eventsViewModel.storageRef.
        child("events/RockingTheDaisies/RockingTheDaisiesLogo.jpg")

        eventsViewModel.downloadImage(
            rtdImg,
            OnSuccessListener { bitmap ->
                imgRTD.setImageBitmap(bitmap)
            },
            OnFailureListener { exception ->
                Log.e("FirebaseStorage", "Error downloading image: ${exception.message}")
            }
        )

        val viewRTD = binding.btnViewRTD

        viewRTD.setOnClickListener {
            val eventInfo = EventInfo.newInstance("RTD")
            navController.navigate(R.id.navigation_eventInfo, eventInfo.arguments)
        }

        val imgITC = binding.imgITC
        val itcImg = eventsViewModel.storageRef.
        child("events/InTheCity/InTheCityLogo.jpg")

        eventsViewModel.downloadImage(
            itcImg,
            OnSuccessListener { bitmap ->
                imgITC.setImageBitmap(bitmap)
            },
            OnFailureListener { exception ->
                Log.e("FirebaseStorage", "Error downloading image: ${exception.message}")
            }
        )

        val viewITC = binding.btnViewITC

        viewITC.setOnClickListener {
            val eventInfo = EventInfo.newInstance("ITC")
            navController.navigate(R.id.navigation_eventInfo, eventInfo.arguments)
        }

        val imgEventsTouring = binding.imgEventsTour
        val eventsTouringImg = eventsViewModel.storageRef.
        child("events/EventsTouring/EventsTouringLogo.png")

        eventsViewModel.downloadImage(
            eventsTouringImg,
            OnSuccessListener { bitmap ->
                imgEventsTouring.setImageBitmap(bitmap)
            },
            OnFailureListener { exception ->
                Log.e("FirebaseStorage", "Error downloading image: ${exception.message}")
            }
        )

        val viewEventsTouring = binding.btnViewEventsTour

        viewEventsTouring.setOnClickListener {
            val eventInfo = EventInfo.newInstance("E&T")
            navController.navigate(R.id.navigation_eventInfo, eventInfo.arguments)
        }

        val txtRTD = binding.txtRTDPreview

        eventsViewModel.getEventDescription("rockingTheDaisies") { description ->
            if (description != null) {
                Log.d("EventDescription", "Rocking The Daisies: $description")
                txtRTD.text = description
            } else {
                Log.e("EventDescription", "Description is null for Rocking The Daisies")
            }
        }

        val txtITC = binding.txtITCPreview

        eventsViewModel.getEventDescription("inTheCity") { description ->
            if (description != null) {
                txtITC.text = description
            }
        }

        val txtEventsTouring = binding.txtEventsTourPreview

        eventsViewModel.getEventDescription("eventsAndTouring") { description ->
            if (description != null) {
                txtEventsTouring.text = description
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}