package com.example.steynentertainment.ui.members

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MembersViewModel : ViewModel() {
    // Define the MutableLiveData to hold the reference to fragment_container
    val fragmentContainerId: MutableLiveData<Int> = MutableLiveData()
}