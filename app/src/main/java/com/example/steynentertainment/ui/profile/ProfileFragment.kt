package com.example.steynentertainment.ui.profile

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentProfileBinding
import com.example.steynentertainment.ui.members.Users
import com.example.steynentertainment.ui.ui.login.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val CAMERA_PERMISSION_REQUEST = 100
    private var IMAGE_PICKER_REQUEST = false
    private var CAMERA_REQUEST = false

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var updatedPhotoReference: String = ""
    private var newImageLoaded: Boolean = false

    private lateinit var progressBar: ProgressBar
    private lateinit var userImageView: ImageView
    private lateinit var changeImageButton: TextView
    private lateinit var email: TextView
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var changePassword: MaterialButton
    private lateinit var update: MaterialButton
    private lateinit var logout: MaterialButton

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        val navController = findNavController()

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        progressBar = binding.progressBar

        // Used to get the user to add a photo to the entry
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (IMAGE_PICKER_REQUEST) {
                    val imageUri = data?.data
                    if (imageUri != null) {
                        progressBar.visibility = View.VISIBLE
                        viewModel.uploadPhoto(imageUri, requireContext()) { photoRef ->
                            // Update the photo reference here
                            updatedPhotoReference = photoRef
                            progressBar.visibility = View.GONE
                            newImageLoaded = true
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error selecting image", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                    IMAGE_PICKER_REQUEST = false
                } else if (CAMERA_REQUEST) {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    if (imageBitmap != null) {
                        progressBar.visibility = View.VISIBLE
                        viewModel.uploadPhoto(imageBitmap, requireContext()) { photoRef ->
                            // Update the photo reference here
                            updatedPhotoReference = photoRef
                            progressBar.visibility = View.GONE
                            newImageLoaded = true
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error capturing image", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                    CAMERA_REQUEST = false
                }
            }
        }

        userImageView = _binding?.userImage ?: root.findViewById(R.id.userImage)
        changeImageButton = _binding?.changeImage ?: root.findViewById(R.id.changeImage)
        email = _binding?.txtEmail ?: root.findViewById(R.id.txtEmail)
        firstName = _binding?.txtFirstName ?: root.findViewById(R.id.txtFirstName)
        lastName = _binding?.txtLastName ?: root.findViewById(R.id.txtLastName)
        update = _binding?.updateInfo ?: root.findViewById(R.id.updateInfo)
        changePassword = _binding?.changePassword ?: root.findViewById(R.id.changePassword)
        logout = _binding?.logout ?: root.findViewById(R.id.logout)

        // Gets the logged in user
        val user = FirebaseAuth.getInstance().currentUser

        // If there is a logged in user
        if (user != null) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val firestore = FirebaseFirestore.getInstance()
            val userDocument = firestore.collection("Users").document(userId.toString())

            userDocument.get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot? ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val userData = documentSnapshot.toObject(Users::class.java)

                        Log.d("ProfileFragment", "UID: $userId")
                        Log.d("ProfileFragment", "UserData: $userData")

                        // Set user attributes to EditText views

                        // Use Picasso, Glide, or any other image loading library to load the image into the ImageView
                        Picasso.get()
                            .load(userData?.profilePicture)
                            .into(userImageView)

                        email.setText(userData?.email)
                        firstName.setText(userData?.firstName)
                        lastName.setText(userData?.lastName)
                    }
                }
                .addOnFailureListener { exception: Exception ->
                    Log.e("ProfileFragment", "Firestore error: ${exception.message}")
                    Toast.makeText(context, "Unable to fetch profile.", Toast.LENGTH_SHORT).show()
                }
        }

        changeImageButton.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), changeImageButton)
            popupMenu.menuInflater.inflate(R.menu.menu_photo_options, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_select_photo -> {
                        IMAGE_PICKER_REQUEST = true
                        // Launch the image picker
                        val intent = Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        imagePickerLauncher.launch(intent)
                        true
                    }
                    R.id.menu_take_photo -> {
                        // Check if the camera permission is granted
                        val hasCameraPermission = ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                        // Request the camera permission if not granted
                        if (!hasCameraPermission) {
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.CAMERA),
                                CAMERA_PERMISSION_REQUEST
                            )
                            return@setOnMenuItemClickListener true // Return true to indicate the action was handled
                        }

                        // Launch the camera activity if permission is granted
                        CAMERA_REQUEST = true
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        imagePickerLauncher.launch(intent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        update.setOnClickListener {
            if (progressBar.visibility == View.VISIBLE) {
                Toast.makeText(requireContext(), "Please wait for the photo to upload", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // checks if textboxes aren't empty
            if (!(firstName.text.isNotEmpty() && lastName.text.isNotEmpty())) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in the first name and last name fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (this.newImageLoaded) {
                    viewModel.updateProfile(
                        firstName.text.toString(),
                        lastName.text.toString(),
                        this.newImageLoaded,
                        updatedPhotoReference,
                        requireContext()
                    )

                    newImageLoaded = false
                } else {
                    viewModel.updateProfile(
                        firstName.text.toString(),
                        lastName.text.toString(),
                        this.newImageLoaded,
                        "",
                        requireContext()
                    )

                    Log.e("Update User Details", "New Photo boolean: $newImageLoaded")
                }
            }
        }

        changePassword.setOnClickListener() {
            showPopupPassword(requireContext()) // calls pop up
        }

        logout.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            Toast.makeText(context, "Logged out Successfully.", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

<<<<<<< Updated upstream
        reward.setOnClickListener() {
            navController.navigate(R.id.navigation_members)
        }

=======
>>>>>>> Stashed changes
        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Popup for changing password
    fun showPopupPassword(context: Context) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.popup_changepassword_layout, null)

        builder.setView(view)
        val dialog = builder.create()

        val cancelpasswordButton = view.findViewById<Button>(R.id.btnCancelPassword)
        val confirmButton = view.findViewById<Button>(R.id.btnConfirmPassword)
        val password = view.findViewById<EditText>(R.id.txtNewPassword)
        val retypepassword = view.findViewById<EditText>(R.id.txtConfirmPassword)

        cancelpasswordButton.setOnClickListener {
            dialog.dismiss()
        }

        confirmButton.setOnClickListener {
            if (password.text.isEmpty() || retypepassword.text.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.text.toString() != retypepassword.text.toString()) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.text.contains(" ") || retypepassword.text.contains(" ")) {
                Toast.makeText(context, "No spaces allowed in password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Change the user's password
            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                val newPassword = password.text.toString()

                user.updatePassword(newPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(context, "Failed to update password: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "User is not signed in", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
