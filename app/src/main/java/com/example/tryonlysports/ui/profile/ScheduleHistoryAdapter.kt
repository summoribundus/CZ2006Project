package com.example.tryonlysports.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tryonlysports.databinding.ListScheduleHistoryBinding

class ScheduleHistoryAdapter: ListAdapter<ScheduleHistory, ScheduleHistoryAdapter.ViewHolder>(ScheduleHistoryDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListScheduleHistoryBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: ScheduleHistory) {
            binding.schedule= item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListScheduleHistoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class ScheduleHistoryDiffCallback : DiffUtil.ItemCallback<ScheduleHistory>() {

    override fun areItemsTheSame(oldItem: ScheduleHistory, newItem: ScheduleHistory): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: ScheduleHistory, newItem: ScheduleHistory): Boolean {
        return oldItem == newItem
    }


}