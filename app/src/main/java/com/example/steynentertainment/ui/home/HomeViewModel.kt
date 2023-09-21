package com.example.steynentertainment.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    // LiveData for navigation to LoginActivity
    private val _navigateToLogin = MutableLiveData<Boolean?>()
    val navigateToLogin: MutableLiveData<Boolean?> get() = _navigateToLogin

    fun onLoginClicked() {
        _navigateToLogin.value = true
    }

    // Call this once navigation is done to reset the LiveData
    fun onLoginNavigated() {
        _navigateToLogin.value = null
    }
}
