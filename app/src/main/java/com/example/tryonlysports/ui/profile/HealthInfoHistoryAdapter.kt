package com.example.tryonlysports.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tryonlysports.databinding.ListWeightHistoryBinding

/**
 * This is the custom adapter class for displaying the health info histories.
 *
 * @author Liu Zhixuan, Li Rui, Ye Ziyuan
 */
class HealthInfoHistoryAdapter: ListAdapter<HealthInfoHistory, HealthInfoHistoryAdapter.ViewHolder>(HealthInfoHistoryDiffCallback()) {
    /**
     * TODO
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthInfoHistoryAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * TODO
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: HealthInfoHistoryAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * TODO
     *
     * @property binding
     */
    class ViewHolder private constructor(val binding: ListWeightHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HealthInfoHistory) {
            binding.healthInfo = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListWeightHistoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

/**
 * TODO
 *
 */
class  HealthInfoHistoryDiffCallback : DiffUtil.ItemCallback<HealthInfoHistory>() {
    /**
     * TODO
     *
     * @param oldItem
     * @param newItem
     * @return
     */
    override fun areItemsTheSame(oldItem: HealthInfoHistory, newItem: HealthInfoHistory): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * TODO
     *
     * @param oldItem
     * @param newItem
     * @return
     */

    override fun areContentsTheSame(oldItem: HealthInfoHistory, newItem: HealthInfoHistory): Boolean {
        return oldItem == newItem
    }

}
