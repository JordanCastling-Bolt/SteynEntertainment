package com.example.steynentertainment.ui.events

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.StorageReference
import java.io.File

class EventInfoViewModel : ViewModel() {

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
}
