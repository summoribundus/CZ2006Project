package com.example.tryonlysports.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tryonlysports.databinding.ListWorkoutHistroyBinding

class WorkoutHistoryAdapter: ListAdapter<WorkoutHistory, WorkoutHistoryAdapter.ViewHolder>(WorkoutHistoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WorkoutHistoryAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: ListWorkoutHistroyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WorkoutHistory) {
            binding.workout = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListWorkoutHistroyBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

class  WorkoutHistoryDiffCallback : DiffUtil.ItemCallback<WorkoutHistory>() {

    override fun areItemsTheSame(oldItem: WorkoutHistory, newItem: WorkoutHistory): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: WorkoutHistory, newItem: WorkoutHistory): Boolean {
        return oldItem == newItem
    }


}