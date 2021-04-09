package com.example.tryonlysports.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tryonlysports.databinding.ListWorkoutHistroyBinding

/**
 * This is the custom adapter class for displaying the workout history.
 *
 * @author Liu Zhixuan, Li Rui, Ye Ziyuan
 */
class WorkoutHistoryAdapter: ListAdapter<WorkoutHistory, WorkoutHistoryAdapter.ViewHolder>(WorkoutHistoryDiffCallback()) {
    /**
     * Creates a new ViewHolder object whenever the RecyclerView needs a new one
     *
     * @param parent the ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType the type of the new View
     * @return a new ViewHolder that holds a View of the given view type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * Displays the data at the specified position
     *
     * @param holder a ViewHolder that describes an item view and metadata about its place within the RecyclerView.
     * @param position the specific position that the data will be displayed.
     */
    override fun onBindViewHolder(holder: WorkoutHistoryAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * This is the ViewHolder class that describes an item view and metadata about its place within the RecyclerView.
     *
     * @property binding the data binding of the ListWorkoutHistory
     */
    class ViewHolder private constructor(val binding: ListWorkoutHistroyBinding): RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the WorkoutHistory object to the recycler item.
         *
         * @param item the WorkoutHistory data object.
         */
        fun bind(item: WorkoutHistory) {
            binding.workout = item
            binding.executePendingBindings()
        }

        /**
         * The companion object that inflates the view with ViewHolder.
         */
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListWorkoutHistroyBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

/**
 * This is the callback class to facilitate the WorkoutHistory content recycler display by checking the difference.
 *
 */
class  WorkoutHistoryDiffCallback : DiffUtil.ItemCallback<WorkoutHistory>() {
    /**
     * Checks if the item IDs are the same.
     *
     * @param oldItem the previous workoutHistory bound to the recycler view.
     * @param newItem the new workoutHistory to bind to the recycler view.
     * @return boolean value (true if the id of the oldItem is the same as the id of the newItem, false otherwise)
     */
    override fun areItemsTheSame(oldItem: WorkoutHistory, newItem: WorkoutHistory): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Checks if the contents of the items are the same.
     *
     * @param oldItem the previous workoutHistory bound to the recycler view.
     * @param newItem the new workoutHistory to bind to the recycler view.
     * @return boolean value (true if the oldItem has the same content as the newItem, false otherwise)
     */
    override fun areContentsTheSame(oldItem: WorkoutHistory, newItem: WorkoutHistory): Boolean {
        return oldItem == newItem
    }


}