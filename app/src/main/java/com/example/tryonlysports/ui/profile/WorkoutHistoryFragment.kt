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
import com.example.tryonlysports.databinding.FragmentWorkoutHistoryBinding
import com.example.tryonlysports.ui.profile.WorkoutHistoryAdapter

<<<<<<< Updated upstream
=======
/**
 * This is the Fragment for the displaying workout history function.
 *
 * @author Liu Zhixuan, Li Rui
 */
>>>>>>> Stashed changes
class WorkoutHistoryFragment:Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentWorkoutHistoryBinding>(inflater,
                R.layout.fragment_workout_history,container,false)

        val adapter = WorkoutHistoryAdapter()
        binding.workoutList.adapter = adapter

        val activity: MainActivity = requireActivity() as MainActivity

        val viewModelFactory = WorkoutHistoryViewModelFactory(activity.db, activity.emailId)

        // Get a reference to the ViewModel associated with this fragment.
        val workoutHistoryViewModel =
                ViewModelProvider(this, viewModelFactory).get(WorkoutHistoryViewModel::class.java)

        workoutHistoryViewModel.whistory.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.setLifecycleOwner(this)
        return binding.root
    }
}