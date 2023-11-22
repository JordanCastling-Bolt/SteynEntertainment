package com.example.steynentertainment.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.steynentertainment.databinding.FragmentHomeBinding
import com.example.steynentertainment.ui.ui.login.LoginActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up login button with ViewModel
        binding.btnLogin.setOnClickListener {
            homeViewModel.onLoginClicked()
        }

        // Observe navigation event
        homeViewModel.navigateToLogin.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                homeViewModel.onLoginNavigated() // Reset the LiveData
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
