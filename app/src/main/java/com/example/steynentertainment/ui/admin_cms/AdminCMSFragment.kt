package com.example.steynentertainment.ui.admin_cms

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.steynentertainment.R

class AdminCMSFragment : Fragment() {

    companion object {
        fun newInstance() = AdminCMSFragment()
    }

    private lateinit var viewModel: AdminCMViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_c_m, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminCMViewModel::class.java)
        // TODO: Use the ViewModel
    }

}