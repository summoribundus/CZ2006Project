package com.example.tryonlysports.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentProfileBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * This is the Fragment for displaying the profile page
 *
 * @author Li Rui, Liu Zhixuan
 */
class ProfileFragment : Fragment() {
    /**
     * The ProfileViewModel.
     */
    private lateinit var viewModel: ProfileViewModel

    /**
     * The Main Activity.
     */
    lateinit var mainActivity: MainActivity

    /**
     * Creates the fragment's portion of the view hierarchy and initializes viewModelFactory and viewModel.
     *
     * @param inflater converts the xml file fragment_profile into View objects.
     * @param container a special view to contain other views.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a View to display on the profile-View History Page
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile, container, false
        )

        mainActivity = activity as MainActivity

        val viewModelFactory = ProfileViewModelFactory(
            mainActivity.db,
            mainActivity.emailId,
            mainActivity.userId
        )

        // Get a reference to the ViewModel associated with this fragment.
        val profileViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(ProfileViewModel::class.java)

        binding.profileViewModel = profileViewModel

        profileViewModel.nearest_hhistory.observe(viewLifecycleOwner, Observer {
            binding.healthHistoryWeightText.text = it.value.toString()
            val date = it.recordTime?.seconds?.let { it1 -> Date(it1.toLong()*1000) }
            val sfd = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val date1 = sfd.format(date)
            Log.i("systemdateconverted", date1)

            binding.healthHistoryText.text = date1

        })



        profileViewModel.nearest_whistory.observe(viewLifecycleOwner, Observer {
            binding.workoutHistoryCaloriesText.text = it.calories
            binding.workoutHistoryDurationText.text = it.duration
            binding.workoutHistoryTypeText.text = it.workoutType

            val date = it.date?.seconds?.let { it1 -> Date(it1.toLong()*1000) }
            val sfd = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val date1 = sfd.format(date)

            binding.workoutHistoryTimeText.text = date1
        })

        profileViewModel.nearest_shistory.observe(viewLifecycleOwner, Observer {
            binding.scheduleHistoryDescriptionText.text = it.description

            val date = it.startDateTime?.seconds?.let { it1 -> Date(it1.toLong()*1000) }
            val sfd = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val date1 = sfd.format(date)

            binding.scheduleHistoryStartDateText.text = date1
            val date2 = it.endDateTime?.seconds?.let { it1 -> Date(it1.toLong()*1000) }
            val date3 = sfd.format(date2)

            binding.scheduleHistoryEndDateText.text = date3
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

    /**
     * Operates when the fragment starts.
     */
    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "subscribeToService")
//        mainActivity.subscribeToService()
    }

}
