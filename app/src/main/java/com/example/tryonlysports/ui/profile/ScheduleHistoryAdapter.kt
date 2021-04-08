package com.example.tryonlysports.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tryonlysports.databinding.ListScheduleHistoryBinding

/**
 * TODO
 *
 */
class ScheduleHistoryAdapter: ListAdapter<ScheduleHistory, ScheduleHistoryAdapter.ViewHolder>(ScheduleHistoryDiffCallback()) {
    /**
     * TODO
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * TODO
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * TODO
     *
     * @property binding
     */
    class ViewHolder private constructor(val binding: ListScheduleHistoryBinding) : RecyclerView.ViewHolder(binding.root){
        /**
         * TODO
         *
         * @param item
         */
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

/**
 * TODO
 *
 */
class ScheduleHistoryDiffCallback : DiffUtil.ItemCallback<ScheduleHistory>() {
    /**
     * TODO
     *
     * @param oldItem
     * @param newItem
     * @return
     */
    override fun areItemsTheSame(oldItem: ScheduleHistory, newItem: ScheduleHistory): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * TODO
     *
     * @param oldItem
     * @param newItem
     * @return
     */
    override fun areContentsTheSame(oldItem: ScheduleHistory, newItem: ScheduleHistory): Boolean {
        return oldItem == newItem
    }


}