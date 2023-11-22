package com.example.steynentertainment.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ProfileViewModel : ViewModel() {

    private var photoReference : String  = ""
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text



    fun updateProfile(firstName: String, lastName: String, newPhoto: Boolean, newPhotoRef: String, context: Context) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()
        val userDocument = firestore.collection("Users").document(userId.toString())
        val updates = HashMap<String, Any>()

        if (newPhoto) {
            Log.e("Update User Details", "New Photo ref: $newPhotoRef")
            updates["firstName"] = firstName
            updates["lastName"] = lastName
            updates["profilePicture"] = newPhotoRef
        } else {
            Log.e("Update User Details", "Old Photo loaded")
            updates["firstName"] = firstName
            updates["lastName"] = lastName
        }

        userDocument.update(updates)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Successfully updated user profile",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Unable to update profile in Firestore: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // Used to upload the selected photo to Firebase Storage
    fun uploadPhoto(photo: Any, context: Context, onPhotoUploaded: (String) -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val photoName = "user/image/${System.currentTimeMillis()}.jpg"
        val photoRef = storageRef.child(photoName)

        val uploadTask = when (photo) {
            is Uri -> photoRef.putFile(photo)
            is Bitmap -> {
                val baos = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                photoRef.putBytes(data)
            }
            else -> {
                Toast.makeText(context, "Unsupported photo type", Toast.LENGTH_SHORT).show()
                return
            }
        }

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Fetching the download URL of the uploaded photo
            photoRef.downloadUrl.addOnSuccessListener { uri ->
                // Notify user that the photo has been uploaded successfully
                Toast.makeText(context, "Photo uploaded successfully!", Toast.LENGTH_SHORT).show()

                // Pass the download URL to the callback
                onPhotoUploaded(uri.toString())
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context, "Error occurred: $exception", Toast.LENGTH_SHORT).show()
        }
    }
}