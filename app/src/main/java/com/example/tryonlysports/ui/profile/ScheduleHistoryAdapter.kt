package com.example.tryonlysports.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tryonlysports.databinding.ListScheduleHistoryBinding

/**
 * This is the custom adapter class for displaying the schedule history.
 *
 * @author Li Rui, Ye Ziyuan, Liu Zhixuan
 */
class ScheduleHistoryAdapter: ListAdapter<ScheduleHistory, ScheduleHistoryAdapter.ViewHolder>(ScheduleHistoryDiffCallback()) {
    /**
     * Displays the data at the specified position
     *
     * @param holder a ViewHolder that describes an item view and metadata about its place within the RecyclerView.
     * @param position the specific position that the data will be displayed.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * Creates a new ViewHolder object whenever the RecyclerView needs a new one
     *
     * @param parent the ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType the type of the new View
     * @return a new ViewHolder that holds a View of the given view type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * This is the ViewHolder class that describes an item view and metadata about its place within the RecyclerView.
     *
     * @property binding the data binding of the ListScheduleHistory
     */
    class ViewHolder private constructor(val binding: ListScheduleHistoryBinding) : RecyclerView.ViewHolder(binding.root){
        /**
         * Binds the ScheduleHistory object to the recycler item.
         *
         * @param item the ScheduleHistory data object.
         */
        fun bind(item: ScheduleHistory) {
            binding.schedule= item
            binding.executePendingBindings()
        }

        /**
         * The companion object that inflates the view with ViewHolder.
         */
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListScheduleHistoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

/**
 * This is the callback class to facilitate the ScheduleHistory content recycler display by checking the difference.
 *
 */
class ScheduleHistoryDiffCallback : DiffUtil.ItemCallback<ScheduleHistory>() {
    /**
     * Checks if the item IDs are the same.
     *
     * @param oldItem the previous scheduleHistory bound to the recycler view.
     * @param newItem the new scheduleHistory to bind to the recycler view.
     * @return boolean value (true if the id of the oldItem is the same as the id of the newItem, false otherwise)
     */
    override fun areItemsTheSame(oldItem: ScheduleHistory, newItem: ScheduleHistory): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Checks if the contents of the items are the same.
     *
     * @param oldItem the previous scheduleHistory bound to the recycler view.
     * @param newItem the new scheduleHistory to bind to the recycler view.
     * @return boolean value (true if the oldItem has the same content as the newItem, false otherwise)
     */
    override fun areContentsTheSame(oldItem: ScheduleHistory, newItem: ScheduleHistory): Boolean {
        return oldItem == newItem
    }


}