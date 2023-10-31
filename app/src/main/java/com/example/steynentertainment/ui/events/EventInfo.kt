package com.example.steynentertainment.ui.events

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.steynentertainment.R

class EventInfo : Fragment() {

    companion object {
        fun newInstance() = EventInfo()
    }

    private lateinit var viewModel: EventInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EventInfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}