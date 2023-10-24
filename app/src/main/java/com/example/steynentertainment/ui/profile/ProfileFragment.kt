import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.Log
import com.example.steynentertainment.R
import com.example.steynentertainment.databinding.FragmentProfileBinding
import com.example.steynentertainment.ui.profile.ProfileViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var userImageView: ImageView
    private lateinit var changeImageButton: MaterialButton
    private val binding get() = _binding!!
    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
       // val root: View = _binding?.root ?: inflater.inflate(R.layout.fragment_profile, container, false)

        val root: View = binding.root
        userImageView = _binding?.userImage ?: root.findViewById(R.id.userImage)
        changeImageButton = _binding?.changeImage ?: root.findViewById(R.id.changeImage)

        changeImageButton.setOnClickListener {
            val pickImageIntent = Intent(Intent.ACTION_GET_CONTENT)
            pickImageIntent.type = "image/*"
            startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST_CODE)
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