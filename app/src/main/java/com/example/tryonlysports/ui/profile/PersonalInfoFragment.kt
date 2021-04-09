package com.example.tryonlysports.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentPersonalInfoBinding

<<<<<<< Updated upstream
=======
/**
 * This is the Fragment for the displaying personal info function.
 *
 * @author Liu Zhixuan, Li Rui
 */
>>>>>>> Stashed changes
class PersonalInfoFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentPersonalInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_info, container, false)

        val activity = activity as MainActivity
        val viewModelFactory = PersonalInfoViewModelFactory(activity.db, activity.emailId)

        // Get a reference to the ViewModel associated with this fragment.
        val personalInfoViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(PersonalInfoViewModel::class.java)

        personalInfoViewModel.personalInfo.observe(viewLifecycleOwner, {
            binding.birthdayText.text = it.birthday
            binding.emailText.text = it.userEmail
            binding.mobileText.text = it.phoneNumber
            binding.regionText.text = it.region
            binding.usernameText.text = "Welcome ${it.username}"
        })

        binding.viewHistoryButton.setOnClickListener {
            this.findNavController().navigate(PersonalInfoFragmentDirections.actionPersonalInfoFragmentToNavigationProfile())
        }

        return binding.root
    }
}