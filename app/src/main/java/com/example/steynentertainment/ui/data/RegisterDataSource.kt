package com.example.steynentertainment.ui.data

import com.example.steynentertainment.ui.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles the data operations of the registration.
 */
class RegisterDataSource {

    fun register(user: LoggedInUser) {
        // TODO: Connect to a real authentication service
        // For now, just add the user to a fake "database"

        if (fakeDatabase.containsKey(user.userId)) {
            throw IOException("User already exists!")
        }

        fakeDatabase[user.userId] = user
    }

    companion object {
        val fakeDatabase = hashMapOf<String, LoggedInUser>()
    }
}
