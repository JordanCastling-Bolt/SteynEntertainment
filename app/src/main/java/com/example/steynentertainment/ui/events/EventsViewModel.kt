package com.example.steynentertainment.ui.events

import android.content.ContentValues
import androidx.lifecycle.ViewModel
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.io.File

class EventsViewModel : ViewModel() {

    val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    val dbRef = FirebaseFirestore.getInstance()

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

    fun getEventDescription(event: String, onCategoryReceived: (String?) -> Unit) {
        val eventsCollection = dbRef.collection("Events")

        val query = eventsCollection
            .whereEqualTo("category", event)

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
