package com.example.todoapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemTaskBinding

// ============================================================
// TaskAdapter.kt - RecyclerView Adapter
// Connects our list of Tasks to the RecyclerView in the UI.
// Uses ListAdapter + DiffUtil for efficient list updates.
// ============================================================

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,       // Callback: item clicked
    private val onCheckboxClick: (Task) -> Unit    // Callback: checkbox toggled
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    // Called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    // Called to bind data to an existing ViewHolder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // ViewHolder holds references to each item's views
    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                tvTaskTitle.text = task.title
                tvTaskDescription.text = task.description.ifEmpty { "No description" }
                checkboxCompleted.isChecked = task.isCompleted

                // Strike-through text if task is completed
                if (task.isCompleted) {
                    tvTaskTitle.paintFlags = tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvTaskTitle.alpha = 0.5f
                } else {
                    tvTaskTitle.paintFlags = tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    tvTaskTitle.alpha = 1.0f
                }

                // Set priority indicator color
                val priorityColor = when (task.priority) {
                    3 -> R.color.priority_high    // Red
                    2 -> R.color.priority_medium  // Orange
                    else -> R.color.priority_low  // Green
                }
                viewPriorityIndicator.setBackgroundColor(
                    ContextCompat.getColor(root.context, priorityColor)
                )

                // Priority label
                tvPriority.text = when (task.priority) {
                    3 -> "HIGH"
                    2 -> "MEDIUM"
                    else -> "LOW"
                }

                // Click listeners
                root.setOnClickListener { onTaskClick(task) }

                checkboxCompleted.setOnClickListener {
                    onCheckboxClick(task)
                }
            }
        }
    }

    // DiffUtil calculates differences between old & new lists efficiently
    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}
