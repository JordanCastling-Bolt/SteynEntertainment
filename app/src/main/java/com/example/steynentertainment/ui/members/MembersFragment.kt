package com.example.steynentertainment.ui.members

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentMembersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MembersFragment : Fragment() {

    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!

    // Settings Global variables of the various components in the fragment
    private lateinit var usernameView: TextView
    private lateinit var daisyInformation: TextView
    private lateinit var daisyOne: ImageView
    private lateinit var daisyTwo: ImageView
    private lateinit var daisyThree: ImageView
    private lateinit var daisyFour: ImageView
    private lateinit var daisyFive: ImageView
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var eventAdapter: MemberEventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setting the global variables to the various components
        usernameView = root.findViewById(R.id.txtwelcometwo)
        daisyInformation = root.findViewById(R.id.txtsecondwelcomeone)
        daisyOne = root.findViewById(R.id.daisy1)
        daisyTwo = root.findViewById(R.id.daisy2)
        daisyThree = root.findViewById(R.id.daisy3)
        daisyFour = root.findViewById(R.id.daisy4)
        daisyFive = root.findViewById(R.id.daisy5)

        // Gets the logged-in user for FirebaseAuth
        val user = FirebaseAuth.getInstance().currentUser

        // Checks if the user is null
        if (user != null) {
            val userId = user.uid
            val firestore = FirebaseFirestore.getInstance()
            val userDocument = firestore.collection("Users").document(userId)

            userDocument.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userData = documentSnapshot.toObject(Users::class.java)

                        // Sets username to relevant profile name
                        usernameView.text = userData?.firstName

                        // If the user is not subscribed
                        if (userData?.subscribed == "no") {
                            val myPayments = userData?.yearlyPayments

                            // Then depending on their purchase, will get various daisies.
                            if (myPayments != null) {
                                if (myPayments in 0..700) {
                                    daisyThree.isVisible = true
                                    daisyInformation.text = "Right now, you have 1 daisy"
                                } else if (myPayments in 0..700) {
                                    daisyTwo.isVisible = true
                                    daisyThree.isVisible = true
                                    daisyInformation.text = "Right now, you have 2 daisies"
                                } else if (myPayments in 1401..2100) {
                                    daisyOne.isVisible = true
                                    daisyTwo.isVisible = true
                                    daisyThree.isVisible = true
                                    daisyInformation.text = "Right now, you have 3 daisies"
                                } else if (myPayments in 2101..2800) {
                                    daisyOne.isVisible = true
                                    daisyTwo.isVisible = true
                                    daisyThree.isVisible = true
                                    daisyFour.isVisible = true
                                    daisyInformation.text = "Right now, you have 4 daisies"
                                } else if (myPayments in 2801..3500) {
                                    daisyOne.isVisible = true
                                    daisyTwo.isVisible = true
                                    daisyThree.isVisible = true
                                    daisyFour.isVisible = true
                                    daisyFive.isVisible = true
                                    daisyInformation.text = "Right now, you have 5 daisies"
                                }
                            }
                        } else {
                            // Else if subscribed they will have 5 stars.
                            daisyOne.isVisible = true
                            daisyTwo.isVisible = true
                            daisyThree.isVisible = true
                            daisyFour.isVisible = true
                            daisyFive.isVisible = true
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Membership Fragment", "Firestore error: ${exception.message}")
                    Toast.makeText(requireContext(), "Unable to fetch profile.", Toast.LENGTH_SHORT).show()
                }
        }

        eventRecyclerView = root.findViewById(R.id.memberEventsRecyclerView)
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        eventAdapter = MemberEventsAdapter(emptyList())

        eventRecyclerView.adapter = eventAdapter

        fetchDataFromFirestore()

        return root
    }

    private fun fetchDataFromFirestore() {
        // Fetch data from Firestore and update the adapter with the data
        val firestore = FirebaseFirestore.getInstance()
        val eventsCollection = firestore.collection("Events")

        eventsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val eventsList = mutableListOf<MemberEvents>()
                for (document in querySnapshot.documents) {
                    val event = document.toObject(MemberEvents::class.java)
                    if (event != null) {
                        eventsList.add(event)
                    }
                }
                // Update the adapter with the retrieved data
                eventAdapter.updateData(eventsList)
            }
            .addOnFailureListener { exception ->
                Log.e("MembersFragment", "Firestore error: ${exception.message}")
                Toast.makeText(requireContext(), "Unable to fetch data.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
