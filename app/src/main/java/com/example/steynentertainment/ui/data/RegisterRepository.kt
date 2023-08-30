package com.example.steynentertainment.ui.data

import com.example.steynentertainment.ui.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles registration operations with login data source.
 */
class RegisterRepository(private val dataSource: RegisterDataSource) {

    // Registration function that returns a Result
    fun register(username: String, password: String, confirmPassword: String): Result<LoggedInUser> {
        // Handle registration
        return try {
            // TODO: Replace this with your actual registration logic
            // For example, validate the data and store it in the database

            if (password != confirmPassword) {
                return Result.Error(IOException("Password and Confirm Password must match"))
            }

            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username)
            dataSource.register(fakeUser)
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error registering", e))
        }
    }
}
