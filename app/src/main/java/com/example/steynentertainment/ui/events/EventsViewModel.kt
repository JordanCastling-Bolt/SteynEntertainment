package com.example.steynentertainment.ui.events

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

// EventsViewModel is a ViewModel class designed for handling data and business logic associated with events.
class EventsViewModel : ViewModel() {

    // Storage reference for accessing Firebase Storage
    val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    // Firestore database reference
    val dbRef = FirebaseFirestore.getInstance()

    // downloadImage downloads an image from Firebase Storage.
    // It takes a StorageReference to the image and listeners for success and failure events.
    fun downloadImage(
        imageRef: StorageReference,
        onSuccessListener: OnSuccessListener<Bitmap>,
        onFailureListener: OnFailureListener
    ) {
        val localFile = File.createTempFile("images", "jpg")
        imageRef.getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                onSuccessListener.onSuccess(bitmap)
            }
            .addOnFailureListener { exception ->
                onFailureListener.onFailure(exception)
            }
    }

    // getEventDescription fetches the description of an event from Firestore.
    // It takes an event identifier and a callback function to handle the received data.
    fun getEventDescription(event: String, onCategoryReceived: (String?) -> Unit) {
        val eventsCollection = dbRef.collection("Subsidiaries")

        val query = eventsCollection
            .whereEqualTo("subsidiary", event)

        query.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val description = document.getString("description")
                    // Invoke the callback with the category value
                    onCategoryReceived(description)
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
                Log.e("FirebaseFirestore", "Error getting documents: ${exception.message}")
                // Invoke the callback with null if there was an error
                onCategoryReceived(null)
            }
    }
}
