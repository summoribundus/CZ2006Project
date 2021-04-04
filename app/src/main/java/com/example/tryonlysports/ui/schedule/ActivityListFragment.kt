package com.example.tryonlysports.ui.schedule

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.tryonlysports.R
import com.example.tryonlysports.databinding.ActivityListBinding


class ActivityListFragment:Fragment(){
    private lateinit var listView: ListView
    private val activityData:ActivityList by activityViewModels()
    private lateinit var adapter: ActivityListAdapter
    private lateinit var binding: ActivityListBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        binding=DataBindingUtil.inflate<ActivityListBinding>(
            inflater, R.layout.activity_list,container,false
        )


        activityData.loadFinished.observe(
                viewLifecycleOwner,Observer<Boolean>{
            hasFinished->if(hasFinished) adapter.notifyDataSetChanged()
        })
        activityData.activities.observe(
                viewLifecycleOwner,Observer{
            adapter.notifyDataSetChanged()
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user=requireActivity().intent.extras?.getString("user_id")
        if(!user.isNullOrEmpty() && activityData.emptyUser()){
            activityData.setUser(user)
            activityData.retrieveActivities()
        }

        adapter = ActivityListAdapter(
                (activityData.activities)!!,
                requireActivity().applicationContext)
        listView = binding.activityList
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position) as Activity
            val popupMenu = PopupMenu(requireActivity().applicationContext, view)
            popupMenu.getMenuInflater().inflate(R.menu.edit_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener { item ->
                val itemId = item.itemId
                if (itemId == R.id.edit) {
                    activityData.select(position)
                    val directions=ActivityListFragmentDirections.toAddActivityFragment()
                    findNavController().navigate(directions)
                } else if (itemId == R.id.complete) {
                    activityData.completeActivity(position)
                    adapter.notifyDataSetChanged()
                } else {
                    val alertDialog: AlertDialog? = activity?.let {
                        val builder = AlertDialog.Builder(it)
                        builder.apply {
                            setPositiveButton(R.string.confirm,
                                DialogInterface.OnClickListener { dialog, id ->
                                    println(position)
                                    activityData.deleteActivity(position)
                                    adapter.notifyDataSetChanged()
                                })
                            setNegativeButton(R.string.cancel_button,
                                DialogInterface.OnClickListener { dialog, id ->

                                })
                            setMessage(R.string.delete_alert)
                        }

                        // Create the AlertDialog
                        builder.show()
                    }

                }
                true
            }
            popupMenu.show()
        }

        binding.button.setOnClickListener {
            activityData.select(-1)
            val directions=ActivityListFragmentDirections.toAddActivityFragment()
            findNavController().navigate(directions)
        }
    }


}