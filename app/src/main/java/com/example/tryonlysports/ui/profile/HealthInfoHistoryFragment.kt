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
 * This is the Fragment for the displaying health info history function.
 *
 * @author Ye Ziyuan, Li Rui, Liu Zhixuan
 */

class HealthInfoHistoryFragment : Fragment() {
    /**
     * Creates the fragment's portion of the view hierarchy and initializes viewModelFactory, viewModel and adapter.
     *
     * @param inflater converts the xml file fragment_weight_history into View objects.
     * @param container a special view to contain other views.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a View to display on the Health Info History page.
     */
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
                if(it.isEmpty()){
                    binding.weightHistoryList.visibility=View.GONE
                    binding.emptyView.visibility=View.VISIBLE
                }
                else{
                    binding.weightHistoryList.visibility=View.VISIBLE
                    binding.emptyView.visibility=View.GONE
                }
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
