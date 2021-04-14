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

/**
 * This is the custom adapter class for displaying a listView of activities.
 *
 * @property activities the activities that are to be displayed.
 * @property context context under which the rows of activities will be displayed.
 * @author Wang Qiaochu
 */
class ActivityListAdapter(a: LiveData<MutableList<Activity>>, context:Context)
    : BaseAdapter() {
    private var activities: MutableList<Activity> =a.value!!
    private var context=context

    /**
     * The function that returns a view that displays data at a location
     *
     * @param position the position of the entry, and the view at position will be returned
     * @param convertView the old view to review
     * @param parent the viewGroup that the entry is going to be in.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view=View.inflate(context, R.layout.activity_row,null)
        var descriptionTextView = view.findViewById(R.id.description) as TextView
        var locationTextView = view.findViewById(R.id.location) as TextView
        var startTextView = view.findViewById(R.id.startTime) as TextView
        var endTextView = view.findViewById(R.id.endTime) as TextView
        var outdoorTextView = view.findViewById(R.id.outdoor) as TextView



        val activity=getItem(position) as Activity
        descriptionTextView.text=activity.description
        locationTextView.text=activity.location
        startTextView.text=activity.start.toDateString()+" "+activity.start.toTimeString()
        endTextView.text=activity.end.toDateString()+" "+activity.end.toTimeString()
        if(activity.isOutdoor) outdoorTextView.text="Outdoor"
        else outdoorTextView.text="Indoor"
        return view

    }

    /**
     * Get the data that is displayed at the entry indicated.
     *
     * @param position the position of the target entry
     * @return the activity displayed by the entry
     */
    override fun getItem(position: Int): Any {
        return activities.get(position)
    }

    /**
     * Get the id of activity in activities that is displayed at the entry indicated.
     *
     * @param position the position of the activity displayed in the entry in activities
     * @return the id of activity in activities displayed by the entry
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Get the number of entries in the list
     * @return size of activities
     */
    override fun getCount(): Int {
        return activities.size
    }


}