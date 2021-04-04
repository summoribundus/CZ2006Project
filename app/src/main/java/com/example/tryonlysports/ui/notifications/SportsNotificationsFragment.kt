package com.example.tryonlysports.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentNotificationsBinding

class SportsNotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_notifications, container, false)

        binding.startButton.setOnClickListener{
            val str:String = proceed()
            this.findNavController().navigate(SportsNotificationsFragmentDirections.actionNavigationNotificationsToDoingActivity(str))
        }

        Log.i("Notification", "binding finished")
        return binding.root
    }


    private fun proceed(): String {
        return when(binding.tipOptions.checkedRadioButtonId) {
            R.id.jogging_opt -> "jogging"
            R.id.walking_opt -> "walking"
            else -> "cycling"
        }
    }


}