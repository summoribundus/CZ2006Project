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
import com.example.tryonlysports.databinding.FragmentScheduleHistoryBinding
import com.example.tryonlysports.databinding.FragmentWeightHistoryBinding


/**
<<<<<<< Updated upstream
 * A simple [Fragment] subclass.
 * Use the [HealthInfoHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
=======
 * This is the Fragment for the displaying health info history function.
 *
 * @author Liu Zhixuan, Li Rui, Ye Ziyuan
>>>>>>> Stashed changes
 */
class HealthInfoHistoryFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentWeightHistoryBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_weight_history, container, false)
        val activity = activity as MainActivity
        val viewModelFactory = HealthInfoHistoryViewModelFactory(activity.db, activity.emailId)

        // Get a reference to the ViewModel associated with this fragment.
        val healthInfoHistoryViewModel =
                ViewModelProvider(
                        this, viewModelFactory).get(HealthInfoHistoryViewModel::class.java)

        val adapter = HealthInfoHistoryAdapter()
        binding.weightHistoryList.adapter = adapter

        healthInfoHistoryViewModel.healthhistory.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.i("InfohistoryFragment", "called??")
                adapter.submitList(it)
            }
        })

        binding.weightRecordButton.setOnClickListener {
            this.findNavController().navigate(HealthInfoHistoryFragmentDirections.actionHealthInfoHistoryFragmentToHealthInfoRecordFragment())
        }

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this
        return binding.root
    }
}