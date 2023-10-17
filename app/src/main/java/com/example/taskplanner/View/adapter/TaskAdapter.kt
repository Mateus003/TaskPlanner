package com.example.taskplanner.View.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskplanner.R
import com.example.taskplanner.databinding.ItemTaskBinding
import com.example.taskplanner.model.Status
import com.example.taskplanner.model.Task

class TaskAdapter(val taskSelected: (Task, Int)->Unit) : ListAdapter<Task, TaskAdapter.TaskViewHolder>( DIFF_CALLBACK) {
    companion object{
        val SELECTED_BACK = 1
        val SELECTED_NEXT = 2
        val SELECTED_DETAIL= 3
        val SELECTED_REMOVE = 4
        val SELECTED_EDIT =5


        private val DIFF_CALLBACK = object  : DiffUtil.ItemCallback<Task>(){
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id && oldItem.descriptionTask == newItem.descriptionTask
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem && oldItem.descriptionTask == newItem.descriptionTask
            }

        }

    }


     inner class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder{
        return TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent,
            false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)

        holder.binding.textTitleTask.text = task.descriptionTask


        setIndication(task, holder)

        holder.binding.delete.setOnClickListener {
            taskSelected(task, SELECTED_REMOVE)
        }

        holder.binding.detail.setOnClickListener {
            taskSelected(task, SELECTED_DETAIL)
        }

        holder.binding.edit.setOnClickListener {
            taskSelected(task, SELECTED_EDIT)
        }
    }

    private fun setIndication(task: Task, holder: TaskViewHolder){
        val containerTask = holder.binding.containerTask
        when (task.status) {
            Status.TODO -> {
                containerTask.setBackgroundColor(ContextCompat.getColor(containerTask.context, R.color.light_green))
                holder.binding.icArrowBackIos.isVisible = false

                holder.binding.icArrowForwardIos.setOnClickListener {
                    taskSelected(task, SELECTED_NEXT)
                }

            }
            Status.DOING -> {
                containerTask.setBackgroundColor(ContextCompat.getColor(containerTask.context, R.color.light_yellow))
                holder.binding.icArrowBackIos.setOnClickListener {
                    taskSelected(task, SELECTED_BACK)
                }

                holder.binding.icArrowForwardIos.setOnClickListener {
                    taskSelected(task, SELECTED_NEXT)
                }

            }
            Status.DONE -> {
                containerTask.setBackgroundColor(ContextCompat.getColor(containerTask.context, R.color.light_pink))
                holder.binding.icArrowForwardIos.isVisible = false
                holder.binding.icArrowBackIos.setOnClickListener {
                    taskSelected(task, SELECTED_BACK)
                }



            }
        }
    }
}