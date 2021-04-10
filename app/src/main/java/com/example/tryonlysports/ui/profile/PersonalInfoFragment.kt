package com.example.tryonlysports.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.LoginActivity
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentPersonalInfoBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * This is the Fragment for the displaying personal info function.
 *
 * @author Liu Zhixuan, Li Rui
 */
class PersonalInfoFragment: Fragment() {
    /**
     * Creates the fragment's portion of the view hierarchy and initializes viewModelFactory and viewModel.
     *
     * @param inflater converts the xml file fragment_personal_info into View objects.
     * @param container a special view to contain other views.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a View to display on the profile page.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentPersonalInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_info, container, false)

        val activity = activity as MainActivity
        val viewModelFactory = PersonalInfoViewModelFactory(activity.db, activity.emailId)

        // Get a reference to the ViewModel associated with this fragment.
        val personalInfoViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(PersonalInfoViewModel::class.java)

        personalInfoViewModel.personalInfo.observe(viewLifecycleOwner, {
            binding.emailText.text = it.userEmail
            binding.mobileText.text = it.phoneNumber
            binding.regionText.text = it.region
            binding.usernameText.text = "Welcome ${it.username}"
        })

        binding.viewHistoryButton.setOnClickListener {
            this.findNavController().navigate(PersonalInfoFragmentDirections.actionPersonalInfoFragmentToNavigationProfile())
        }

        binding.btnLogout.setOnClickListener {
            //Logout from app
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            //Give a success toast message to users
            Toast.makeText(
                context,
                "You were logged out successfully.",
                Toast.LENGTH_SHORT
            ).show()
        }

        return binding.root
    }
}
