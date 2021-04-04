package com.example.tryonlysports.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentScheduleHistoryBinding


class ScheduleHistoryFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentScheduleHistoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_schedule_history, container, false)
        val activity = activity as MainActivity
        val viewModelFactory = ScheduleHistoryViewModelFactory(activity.db, activity.emailId)

        // Get a reference to the ViewModel associated with this fragment.
        val scheduleHistoryViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(ScheduleHistoryViewModel::class.java)

        val adapter = ScheduleHistoryAdapter()
        binding.scheduleHistoryList.adapter = adapter

        scheduleHistoryViewModel.shistory.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)
        return binding.root
    }

}