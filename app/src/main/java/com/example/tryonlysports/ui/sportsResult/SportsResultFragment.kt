package com.example.tryonlysports.ui.sportsResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tryonlysports.MainActivity
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentResultBinding

/**
 * This is the Fragment for the displaying sports results summary function.
 *
 * @author Ye Ziyuan
 */
class SportsResultFragment: Fragment() {

    /**
     * Creates the fragment's portion of the view hierarchy and initializes ViewModelFactory and then ViewModel.
     *
     * @param inflater converts the xml file fragment_sports_results into View objects.
     * @param container a special view to contain other views.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a View to display on the Sports Result page.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentResultBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_result,
            container, false
        )
        val arguments = SportsResultFragmentArgs.fromBundle(requireArguments())
        val activity: MainActivity = requireActivity() as MainActivity
        val viewModelFactory = SportsResultViewModelFactory(
            arguments.type,
            arguments.timePassed,
            arguments.totalDistance.toDouble(),
            activity.db,
            activity.emailId
        )
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SportsResultViewModel::class.java)
        // Create the observer which updates the UI.

        when(arguments.type) {
            "jogging" -> binding.imageView4.setImageResource(R.drawable.jogging)
            "walking" -> binding.imageView4.setImageResource(R.drawable.walking)
            "cycling" -> binding.imageView4.setImageResource(R.drawable.cycling)
        }

        binding.sportsResultViewModel = viewModel
        viewModel.weight.observe(viewLifecycleOwner, {
            viewModel.calculateCalories()
            binding.caloriesText.text = viewModel.calories.value
            binding.imageLoading.visibility = View.GONE
            binding.caloriesText.visibility = View.VISIBLE
            binding.avgSpeedText.visibility = View.VISIBLE
            binding.distanceText.visibility = View.VISIBLE
            binding.timeDurationText.visibility = View.VISIBLE
            binding.textCongratulation.visibility = View.VISIBLE
            binding.imageView4.visibility = View.VISIBLE
            binding.textView10.visibility = View.VISIBLE
            binding.textView11.visibility = View.VISIBLE
            binding.textView5.visibility = View.VISIBLE
            binding.textView6.visibility = View.VISIBLE
            binding.textView7.visibility = View.VISIBLE
            binding.textView9.visibility = View.VISIBLE
            viewModel.saveToFirebase()
        })

        return binding.root
    }
}