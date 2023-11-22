package com.example.steynentertainment.ui.members

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentMembersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class MembersFragment : Fragment() {

    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!

    // Settings Global variables of the various components in the fragment
    private lateinit var usernameView: TextView
    private lateinit var daisyInformation: TextView
    private lateinit var discount: TextView
    private lateinit var daisyOne: ImageView
    private lateinit var daisyTwo: ImageView
    private lateinit var daisyThree: ImageView
    private lateinit var daisyFour: ImageView
    private lateinit var daisyFive: ImageView
    private lateinit var infoButton: Button
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var eventAdapter: MemberEventsAdapter
    private lateinit var viewModel: MembersViewModel
    private lateinit var profileImage: ImageView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembersBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //Setting the global variables to the various components
        usernameView = root.findViewById(R.id.txtwelcomeone)
        daisyInformation = root.findViewById(R.id.txtsecondwelcomeone)
        daisyOne = root.findViewById(R.id.daisy1)
        daisyTwo = root.findViewById(R.id.daisy2)
        daisyThree = root.findViewById(R.id.daisy3)
        daisyFour = root.findViewById(R.id.daisy4)
        daisyFive = root.findViewById(R.id.daisy5)
        discount = root.findViewById(R.id.txtDiscount)
        profileImage = root.findViewById(R.id.memberPhoto)
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

                        // Load and display the image using Picasso
                        Picasso.get().load(userData?.profilePicture).into(profileImage)
                        //Sets username to relevant profile name
                        usernameView.text = "Welcome, " + userData?.firstName


                        // If the user is not subscribed
                        if (userData?.subscribed == "no") {
                            val myPayments = userData?.yearlyPayments

                            // Then depending on their purchase, will get various daisies.
                            if (myPayments != null) {
                                if (myPayments in 0..700) {
                                    daisyThree.isVisible = true
                                    daisyInformation.text = "You have 1 daisy"
                                    discount.text = "You get 2.5% discount for all events."
                                } else if (myPayments in 701..1400) {  // Fix this range
                                    daisyTwo.isVisible = true
                                    daisyThree.isVisible = true
                                    daisyInformation.text = "You have 2 daisies."
                                    discount.text = "You get 5% discount for all events."
                                } else if (myPayments in 1401..2100) {
                                    daisyOne.isVisible = true
                                    daisyTwo.isVisible = true
                                    daisyThree.isVisible = true
                                    daisyInformation.text = "You have 3 daisies."
                                    discount.text = "You get 7.5% discount for all events."
                                } else if (myPayments in 2101..2800) {
                                    daisyOne.isVisible = true
                                    daisyTwo.isVisible = true
                                    daisyThree.isVisible = true
                                    daisyFour.isVisible = true
                                    daisyInformation.text = "You have 4 daisies."
                                    discount.text = "You get 10% discount for all events."
                                } else if (myPayments in 2801..3500) {
                                    daisyOne.isVisible = true
                                    daisyTwo.isVisible = true
                                    daisyThree.isVisible = true
                                    daisyFour.isVisible = true
                                    daisyFive.isVisible = true
                                    daisyInformation.text = "You have 5 daisies."
                                    discount.text = "You get 12.5% discount for all events."
                                }
                            }
                        } else {
                            // Else if subscribed they will have 5 stars.
                            daisyOne.isVisible = true
                            daisyTwo.isVisible = true
                            daisyThree.isVisible = true
                            daisyFour.isVisible = true
                            daisyFive.isVisible = true
                            daisyInformation.text = "You have 5 daisies."
                            discount.text = "You get 12.5% discount for all events."
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Membership Fragment", "Firestore error: ${exception.message}")
                    Toast.makeText(requireContext(), "Unable to fetch profile.", Toast.LENGTH_SHORT)
                        .show()
                }
        }

        eventRecyclerView = root.findViewById(R.id.memberEventsRecyclerView)
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        eventAdapter = MemberEventsAdapter(emptyList())

        eventRecyclerView.adapter = eventAdapter

        fetchDataFromFirestore()

        infoButton = root.findViewById(R.id.btnMemberInfo)


        infoButton.setOnClickListener {
            showPopupInfo(requireContext())//calls pop up
        }

        return root
    }

    private fun fetchDataFromFirestore(){
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

        @RequiresApi(Build.VERSION_CODES.O)
        fun showPopupInfo(context: Context) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.memberinfo_layout)

            // Set layout parameters to match the original layout
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setLayout(layoutParams.width, layoutParams.height)


            var myCancelButton = dialog.findViewById<Button>(R.id.btnPayMembershipCancel)
            var myPayButton = dialog.findViewById<Button>(R.id.btnPayMembership)


            myCancelButton?.setOnClickListener {
                dialog.dismiss()
            }

            myPayButton?.setOnClickListener {
                // Inflate the custom layout
                val inflater = LayoutInflater.from(requireContext())
                val view = inflater.inflate(R.layout.mockpayment_portal, null)

                // Find EditText within the inflated view
                val editExpiryMonth = view.findViewById<EditText>(R.id.editExpiryMonth)
                val editCardNumber = view.findViewById<EditText>(R.id.editCardNumber)


                val minMonth = 1
                val maxMonth = 12
                val monthInputFilter = InputFilter { source, start, end, dest, dstart, dend ->
                    try {
                        val input = Integer.parseInt(dest.toString() + source.toString())
                        if (input in minMonth..maxMonth && input >= minMonth && input <= maxMonth) {
                            null
                        } else {
                            ""
                        }
                    } catch (e: NumberFormatException) {
                        ""
                    }
                }
                editExpiryMonth.filters = arrayOf(monthInputFilter)


                // Create an AlertDialog with the custom layout
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Membership Payment Portal")
                    .setView(view)
                    .setPositiveButton("Confirm Payment") { dialog, _ ->
                        // Retrieve values from the input fields
                        val cardNumber =
                            view.findViewById<EditText>(R.id.editCardNumber).text.toString()
                        val cardHolderName =
                            view.findViewById<EditText>(R.id.editCardHolderName).text.toString()
                        val cvv = view.findViewById<EditText>(R.id.editCVV).text.toString()
                        val month =
                            view.findViewById<EditText>(R.id.editExpiryMonth).text.toString()
                        val year = view.findViewById<EditText>(R.id.editExpiryYear).text.toString()

                        if (cardNumber.isEmpty() || cardHolderName.isEmpty() || cvv.isEmpty() || month.isEmpty() || year.isEmpty()) {
                            Toast.makeText(
                                requireContext(),
                                "Must enter all fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val cardType = getCardType(cardNumber)

                            // Handle the "Confirm Payment" button click based on the card type
                            when (cardType) {
                                "Visa" -> {
                                    updatePaymentStatusInFirestore()
                                    Toast.makeText(
                                        requireContext(),
                                        "Paid R300 with Visa",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val fragmentManager = requireActivity().supportFragmentManager
                                    val transaction = fragmentManager.beginTransaction()
                                    transaction.replace(
                                        R.id.fragment_container_member,
                                        MembersFragment()
                                    )
                                    transaction.addToBackStack(null)
                                    transaction.commit()

                                }

                                "MasterCard" -> {
                                    updatePaymentStatusInFirestore()
                                    Toast.makeText(
                                        requireContext(),
                                        "Paid R300 with Mastercard",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val fragmentManager = requireActivity().supportFragmentManager
                                    val transaction = fragmentManager.beginTransaction()
                                    transaction.replace(
                                        R.id.fragment_container_member,
                                        MembersFragment()
                                    )
                                    transaction.addToBackStack(null)
                                    transaction.commit()
                                }

                                "American Express" -> {
                                    updatePaymentStatusInFirestore()
                                    Toast.makeText(
                                        requireContext(),
                                        "Paid R300 with American Express",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val fragmentManager = requireActivity().supportFragmentManager
                                    val transaction = fragmentManager.beginTransaction()
                                    transaction.replace(
                                        R.id.fragment_container_member,
                                        MembersFragment()
                                    )
                                    transaction.addToBackStack(null)
                                    transaction.commit()
                                }

                                "Unknown" -> {
                                    Toast.makeText(
                                        requireContext(),
                                        "Invalid card number",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            // Dismiss the dialog only if the fields are not empty
                            dialog.dismiss()
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        // Handle the "Cancel" button click if needed
                        dialog.dismiss()
                    }
                    .create()

                // Show the AlertDialog
                alertDialog.show()
            }
            // Show the Dialog
            dialog.show()
        }

        fun getCardType(cardNumber: String): String {
            // Remove any spaces or non-digit characters
            val cleanCardNumber = cardNumber.replace("\\s+".toRegex(), "")

            // Check the card type based on known patterns
            return when {
                cleanCardNumber.matches("^4[0-9]{12}(?:[0-9]{3})?\$".toRegex()) -> "Visa"
                cleanCardNumber.matches("^5[1-5][0-9]{14}\$".toRegex()) -> "MasterCard"
                cleanCardNumber.matches("^3[47][0-9]{13}\$".toRegex()) -> "American Express"
                else -> "Unknown"
            }
        }
    }
        private fun updatePaymentStatusInFirestore() {
            val user = FirebaseAuth.getInstance().currentUser

            user?.let {
                val userId = user.uid
                val firestore = FirebaseFirestore.getInstance()
                val userDocument = firestore.collection("Users").document(userId)

                // Update the subscribed field to "yes"
                userDocument.update("subscribed", "yes")
                    .addOnSuccessListener {
                        Log.d("Firestore", "Subscribed status updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error updating subscribed status: ${e.message}")
                    }
            }
        }



    
