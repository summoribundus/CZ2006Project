package com.example.tryonlysports.ui.schedule
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button
import android.widget.TextView;
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tryonlysports.R
import java.util.ArrayList;

class ActivityListAdapter(a: LiveData<MutableList<Activity>>, context:Context)
    : BaseAdapter() {
    private var activities: MutableList<Activity> =a.value!!
    private var context=context


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view=View.inflate(context, R.layout.activity_row,null)
        var descriptionTextView = view.findViewById(R.id.description) as TextView
        var locationTextView = view.findViewById(R.id.location) as TextView
        var startTextView = view.findViewById(R.id.startTime) as TextView
        var endTextView = view.findViewById(R.id.endTime) as TextView
        var outdoorTextView = view.findViewById(R.id.outdoor) as TextView
        var editButton=view.findViewById(R.id.editButton) as Button


        val activity=getItem(position) as Activity
        descriptionTextView.text=activity.description
        locationTextView.text=activity.location
        startTextView.text=activity.start.toString()
        endTextView.text=activity.end.toString()
        if(activity.isOutdoor) outdoorTextView.text="Outdoor"
        else outdoorTextView.text="Indoor"
        return view

    }

    override fun getItem(position: Int): Any {
        return activities.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return activities.size
    }


}