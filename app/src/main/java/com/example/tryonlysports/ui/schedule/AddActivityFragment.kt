package com.example.tryonlysports.ui.schedule

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.ActivityAddActivityBinding
import java.util.*
enum class State{EDIT,ADD}
class AddActivityFragment: Fragment(){
    private val activityData:ActivityList by activityViewModels()
    private lateinit var newActivity:Activity
    private var state=State.ADD
    lateinit var binding: ActivityAddActivityBinding


    override fun onCreateView(inflater: LayoutInflater,
                             container: ViewGroup?,
                             savedInstanceState: Bundle?): View?{
        binding= DataBindingUtil.inflate<ActivityAddActivityBinding>(
            inflater, R.layout.activity_add_activity,container,false
        )


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        state=State.ADD
        if(activityData.selected.value!=-1){
            state=State.EDIT
            newActivity=activityData.getSelected()
        }
        else newActivity=Activity("","", DateTime(0,0,0),
            DateTime(0,0,0),false)

        if(state==State.EDIT){
            binding.descriptionText.setText(newActivity.description)
            binding.locationText.setText(newActivity.location)
            binding.selectStartDateButton.setHint(newActivity.start.toDateString())
            binding.selectStartTimeButton.setHint(newActivity.start.toTimeString())
            binding.selectEndDateButton.setHint(newActivity.end.toDateString())
            binding.selectEndTimeButton.setHint(newActivity.end.toTimeString())
            binding.indoor.isChecked=newActivity.isOutdoor
        }

        binding.selectStartDateButton.setOnClickListener{
            val calendar = Calendar.getInstance()
            val datePickerDialog =
                DatePickerDialog(requireParentFragment().requireContext(),
                    { view: DatePicker, year:Int, month: Int, date: Int->
                        binding.selectStartDateButton.setHint(""+date+"/"+month+"/"+year)
                        newActivity.start.year=year
                        newActivity.start.month=month
                        newActivity.start.day=date
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        binding.selectEndDateButton.setOnClickListener{
            val calendar = Calendar.getInstance()
            val datePickerDialog =
                DatePickerDialog(requireParentFragment().requireContext(),
                    { view: DatePicker, year:Int, month: Int, date: Int->
                        binding.selectEndDateButton.setHint(""+date+"/"+month+"/"+year)
                        newActivity.end.year=year
                        newActivity.end.month=month
                        newActivity.end.day=date
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        binding.selectStartTimeButton.setOnClickListener{
            val calendar = Calendar.getInstance()
            val timePickerDialog =
                TimePickerDialog(requireParentFragment().requireContext(),
                    { view: TimePicker, hour:Int, minute: Int->
                        binding.selectStartTimeButton.setHint(""+hour+":"+minute)
                        newActivity.start.hour=hour
                        newActivity.start.minute=minute
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(requireActivity().applicationContext))
            timePickerDialog.show()
        }

        binding.selectEndTimeButton.setOnClickListener{
            val calendar = Calendar.getInstance()
            val timePickerDialog =
                TimePickerDialog(requireParentFragment().requireContext(),
                    { view: TimePicker, hour:Int, minute: Int->
                        binding.selectEndTimeButton.setHint(""+hour+":"+minute)
                        newActivity.end.hour=hour
                        newActivity.end.minute=minute
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(requireActivity().applicationContext))
            timePickerDialog.show()
        }

        binding.cancelButton.setOnClickListener{
           val directions=AddActivityFragmentDirections.toActivityListFragment()
            findNavController().navigate(directions)
        }

        binding.saveActivityButton.setOnClickListener{
            var valid=true;
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                if(binding.descriptionText.text.isBlank()&&valid){
                    builder.apply{
                        setMessage("Description is empty")
                        setPositiveButton("OK",
                            DialogInterface.OnClickListener { dialog, id ->

                            })
                        valid=false
                        builder.show()
                    }
                }
                if(binding.locationText.text.isBlank()&&valid){
                    builder.apply{
                        setMessage("Location is empty")
                        setPositiveButton("OK",
                            DialogInterface.OnClickListener { dialog, id ->

                            })
                    }
                    valid=false
                    builder.show()
                }
                if(newActivity.start.isEmpty()&&valid){
                    builder.apply{
                        setMessage("start datetime is empty")
                        setPositiveButton("OK",
                                DialogInterface.OnClickListener { dialog, id ->

                                })
                    }
                    valid=false
                    builder.show()
                }
                if(newActivity.end.isEmpty()&&valid){
                    builder.apply{
                        setMessage("end datetime is empty")
                        setPositiveButton("OK",
                                DialogInterface.OnClickListener { dialog, id ->

                                })
                    }
                    valid=false
                    builder.show()
                }
                if(newActivity.start.biggerThan(newActivity.end)&&valid){
                    builder.apply{
                        setMessage("end time is bigger than start time")
                        setPositiveButton("OK",
                                DialogInterface.OnClickListener { dialog, id ->

                                })
                    }
                    valid=false
                    builder.show()
                }
                builder.create()
            }
            if(valid){
                newActivity.description=binding.descriptionText.text.toString()
                newActivity.location=binding.locationText.text.toString()
                newActivity.isOutdoor=binding.indoor.isChecked
                activityData.updateActivities(activityData.selected.value!!,newActivity)
                val directions=AddActivityFragmentDirections.toActivityListFragment()
                findNavController().navigate(directions)
            }

        }
    }
}