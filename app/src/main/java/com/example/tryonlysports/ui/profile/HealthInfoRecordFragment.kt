package com.example.tryonlysports.ui.profile

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentHealthInfoRecordBinding

class HealthInfoRecordFragment: Fragment() {

    lateinit var binding: FragmentHealthInfoRecordBinding
    lateinit var healthInfoRecordViewModel: HealthInfoRecordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentHealthInfoRecordBinding>(
            inflater,
            R.layout.fragment_health_info_record, container, false
        )

        val activity = activity as MainActivity
        val viewModelFactory = HealthInfoRecordViewModelFactory(activity.db, activity.emailId)

        // Get a reference to the ViewModel associated with this fragment.
        healthInfoRecordViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(HealthInfoRecordViewModel::class.java)

        binding.button2.setOnClickListener {
            saveWeight()
            hideKeyboard()
            this.findNavController().navigate(HealthInfoRecordFragmentDirections.actionHealthInfoRecordFragmentToHealthInfoHistoryFragment())
        }

        return binding.root
    }

    fun saveWeight() {
        healthInfoRecordViewModel.saveToFirestore(binding.recorder.text.toString().toInt())
    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = requireActivity().currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}