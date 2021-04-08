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

/**
 * This is the Fragment for the displaying schedule history function.
 *
 */
class ScheduleHistoryFragment: Fragment() {
    /**
     * Creates the fragment's portion of the view hierarchy and initializes viewModelFactory, viewModel and adapter.
     *
     * @param inflater converts the xml file fragment_schedule_history into View objects.
     * @param container a special view to contain other views.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a View to display on the Schedule History page.
     */
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