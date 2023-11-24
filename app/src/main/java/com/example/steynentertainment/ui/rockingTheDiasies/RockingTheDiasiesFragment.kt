package com.example.steynentertainment.ui.rockingTheDiasies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.steynentertainment.R
import com.google.android.material.button.MaterialButton

// Daisies fragment
class RockingTheDiasiesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rocking_the_diasies, container, false)
        val getInTouchButton = view.findViewById<MaterialButton>(R.id.getInTouchBtn)

        // Set click listener on the button
        getInTouchButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_getInTouch)
        }
        return view
    }
}
