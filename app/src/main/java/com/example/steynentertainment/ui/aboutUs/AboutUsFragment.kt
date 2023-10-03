package com.example.steynentertainment.ui.aboutUs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.steynentertainment.databinding.FragmentAboutusBinding

class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the ViewModel
        val aboutUsViewModel = ViewModelProvider(this).get(AboutUsViewModel::class.java)

        // Inflate the layout using data binding
        _binding = FragmentAboutusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // No need to set text programmatically as it should be already in XML layout
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
