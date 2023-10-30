package com.example.steynentertainment.ui.aboutUs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentAboutusBinding
import com.example.steynentertainment.ui.get_in_touch.GetInTouchFragment

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



       // binding.getInTouchBtn.setOnClickListener {
         //   findNavController().navigate(R.id.) // Assuming the ID for GetInTouchFragment in your navigation graph is "navigation_get_in_touch"
       // }






        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
