package com.example.tryonlysports.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentProfileBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_profile,container,false)

        mainActivity = activity as MainActivity

        val viewModelFactory = ProfileViewModelFactory(mainActivity.db, mainActivity.emailId)

        // Get a reference to the ViewModel associated with this fragment.
        val profileViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(ProfileViewModel::class.java)

        binding.profileViewModel = profileViewModel

        profileViewModel.nearest_hhistory.observe(viewLifecycleOwner, Observer {
            binding.healthHistoryWeightText.text = it.value.toString()
            binding.healthHistoryText.text = it.recordTime?.toDate().toString()
        })

        profileViewModel.nearest_whistory.observe(viewLifecycleOwner, {
            binding.workoutHistoryCaloriesText.text = it.calories
            binding.workoutHistoryDurationText.text = it.duration
            binding.workoutHistoryTypeText.text = it.workoutType
            binding.workoutHistoryTimeText.text = it.date?.toDate().toString()
        })

        profileViewModel.nearest_shistory.observe(viewLifecycleOwner, {
            binding.scheduleHistoryDescriptionText.text = it.description
            binding.scheduleHistoryEndDateText.text = it.endDateTime?.toDate().toString()
            binding.scheduleHistoryStartDateText.text = it.startDateTime?.toDate().toString()
            binding.scheduleHistoryLocationText.text = it.location
        })

        binding.healthMore.setOnClickListener {
            this.findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToHealthInfoHistoryFragment())
        }

        binding.workoutMore.setOnClickListener {
            this.findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToWorkoutHistoryFragment())
        }

        binding.scheduleMore.setOnClickListener {
            this.findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToScheduleHistoryFragment())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "subscribeToService")
//        mainActivity.subscribeToService()
    }

}