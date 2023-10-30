package com.example.steynentertainment.ui.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentProfileBinding
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var userImageView: ImageView
    private lateinit var changeImageButton: MaterialButton
    private lateinit var email: EditText
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var password: EditText
    private lateinit var reward: MaterialButton
    private lateinit var update: MaterialButton
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 1
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


        userImageView = _binding?.userImage ?: root.findViewById(R.id.userImage)
        changeImageButton = _binding?.changeImage ?: root.findViewById(R.id.changeImage)
        email = _binding?.txtEmail ?: root.findViewById(R.id.txtEmail)
        firstName = _binding?.txtFirstName ?: root.findViewById(R.id.txtFirstName)
        lastName = _binding?.txtLastName ?: root.findViewById(R.id.txtLastName)
        password = _binding?.textPassword ?: root.findViewById(R.id.textPassword)
        reward = _binding?.viewRewardsBtn ?: root.findViewById(R.id.viewRewardsBtn)
        update = _binding?.updateInfo ?: root.findViewById(R.id.updateInfo)

        changeImageButton.setOnClickListener {
            val pickImageIntent = Intent(Intent.ACTION_GET_CONTENT)
            pickImageIntent.type = "image/*"
            startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST_CODE)
        }

        update.setOnClickListener {
            Toast.makeText(context, "User updated successfully!", Toast.LENGTH_SHORT).show()
        }
        return root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            val context = requireContext()

            val imageStream = imageUri?.let { context.contentResolver.openInputStream(it) }
            val selectedImage = imageStream?.let { Drawable.createFromStream(it, imageUri.toString()) }
            selectedImage?.let { userImageView.setImageDrawable(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}