package com.example.steynentertainment.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// HomeViewModel is a ViewModel class designed for managing UI-related data in the HomeFragment.
class HomeViewModel : ViewModel() {

    // _text is a MutableLiveData that holds a string value for the home fragment.
    // It's private because it's not exposed to the outside, LiveData is used for that.
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    // _navigateToLogin is a MutableLiveData that manages the navigation to the LoginActivity.
    // It's nullable to represent the three states: true (navigate), false (don't navigate), and null (reset state).
    private val _navigateToLogin = MutableLiveData<Boolean?>()
    val navigateToLogin: MutableLiveData<Boolean?> get() = _navigateToLogin

    // onLoginClicked is called when the login button is clicked.
    // It updates _navigateToLogin to trigger navigation.
    fun onLoginClicked() {
        _navigateToLogin.value = true
    }

    // onLoginNavigated is called after navigation to reset the navigation LiveData.
    // This is necessary to prevent unwanted navigation events on configuration changes like screen rotations.
    fun onLoginNavigated() {
        _navigateToLogin.value = null
    }
}
