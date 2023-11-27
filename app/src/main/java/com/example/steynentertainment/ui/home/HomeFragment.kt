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
import com.google.firebase.auth.FirebaseAuth

// HomeFragment is a Fragment class responsible for displaying the home screen of the application.
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // onCreateView is called to create and return the view hierarchy associated with the fragment.
    // This method initializes UI components and sets up user authentication status checks and login functionality.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Check Firebase Authentication status
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is logged in, make the login button invisible
            binding.btnLogin.visibility = View.GONE
        } else {
            // User is not logged in, set up login button
            binding.btnLogin.visibility = View.VISIBLE
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
        }

        return root
    }

    // onDestroyView is called when the view hierarchy associated with the fragment is being removed.
    // It's used here to clean up the binding reference.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
