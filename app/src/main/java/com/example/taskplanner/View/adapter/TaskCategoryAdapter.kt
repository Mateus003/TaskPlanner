package com.example.taskplanner.View.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskplanner.R
import com.example.taskplanner.databinding.ItemCategoryBinding
import com.example.taskplanner.databinding.ItemTaskBinding
import com.example.taskplanner.model.TaskCategory

class TaskCategoryAdapter: RecyclerView.Adapter<TaskCategoryAdapter.TaskCategoryViewHolder>() {

    inner class TaskCategoryViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<TaskCategory>(){

        override fun areItemsTheSame(oldItem: TaskCategory, newItem: TaskCategory): Boolean = oldItem.textCategory == newItem.textCategory

        override fun areContentsTheSame(oldItem: TaskCategory, newItem: TaskCategory): Boolean {
            return oldItem.textCategory == newItem.textCategory
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCategoryAdapter.TaskCategoryViewHolder {
        return TaskCategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent,
            false))
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TaskCategoryViewHolder, position: Int) {
        val category = differ.currentList[position]


        holder.itemView.apply {
            findViewById<TextView>(R.id.txtCategory).text = category.textCategory



            findViewById<CardView>(R.id.containerText).background= category.containerTextCategory.background

        }
    }

    

}