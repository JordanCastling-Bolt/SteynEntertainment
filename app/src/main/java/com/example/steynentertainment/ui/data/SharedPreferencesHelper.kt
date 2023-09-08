package com.example.steynentertainment.ui.data

import android.content.Context
import android.util.Log

object SharedPreferencesHelper {
    private const val PREF_FILE = "your_pref_file_name"
    private const val TOKEN_KEY = "session_token"

    fun saveToken(context: Context, token: ByteArray) {
        val sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token.toString())
        editor.apply()
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN_KEY, null)

        // Log the retrieved token to Logcat
        if (token != null) {
            Log.d("GetToken", "Retrieved Token: $token")
        } else {
            Log.d("GetToken", "No token found")
        }

        return token
    }

}
