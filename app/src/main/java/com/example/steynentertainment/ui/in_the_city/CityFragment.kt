package com.example.steynentertainment.ui.in_the_city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.steynentertainment.R
import com.google.android.material.button.MaterialButton

class CityFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_city, container, false)
        val getInTouchButton = view.findViewById<MaterialButton>(R.id.getInTouchBtn)

        // Set click listener on the button
        getInTouchButton.setOnClickListener {
            findNavController().navigate(R.id.action_city_to_getInTouch)
        }
        return view
    }
}
