package com.example.steynentertainment.ui.admin_dashboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.steynentertainment.R

class AdminDashboardFragment : Fragment() {

    companion object {
        fun newInstance() = AdminDashboardFragment()
    }

    private lateinit var viewModel: AdminDashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminDashboardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}