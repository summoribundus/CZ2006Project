package com.example.tryonlysports.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.FragmentNotificationsBinding

/**
 * This is the Fragment to prompt user to choose one workout mode and then start workout.
 *
 * @author Ye Ziyuan
 */
class SportsNotificationsFragment : Fragment() {

    /**
     * Sports Prompt data binding.
     */
    private var _binding: FragmentNotificationsBinding? = null

    /**
     * The value of data binding.
     */
    private val binding get() = _binding!!

    /**
     * Creates the fragment's portion of the view hierarchy and initializes Radio Groups and Buttons.
     *
     * @param inflater converts the xml file fragment_notifications into View objects.
     * @param container a special view to contain other views.
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method of MainActivity.
     * @return a View to display on Sports Notification Page.
     */
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

        binding.tipOptions.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when(i) {
                R.id.jogging_opt -> binding.imageView2.setImageResource(R.drawable.jogging)
                R.id.walking_opt -> binding.imageView2.setImageResource(R.drawable.walking)
                R.id.cycling_opt -> binding.imageView2.setImageResource(R.drawable.cycling)
            }
        }

        Log.i("Notification", "binding finished")
        return binding.root
    }

    /**
     * Proceeds with the radio button choice of workout type.
     *
     * @return a string indicating the workout type.
     */
    private fun proceed(): String {
        return when(binding.tipOptions.checkedRadioButtonId) {
            R.id.jogging_opt -> "jogging"
            R.id.walking_opt -> "walking"
            else -> "cycling"
        }
    }


}