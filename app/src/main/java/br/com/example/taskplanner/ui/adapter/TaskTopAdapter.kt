package br.com.example.taskplanner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.example.taskplanner.data.model.Task
import br.com.example.taskplanner.databinding.ItemTaskTopBinding

class TaskTopAdapter(
    private val taskTopSelected : (Task, Int) -> Unit
): ListAdapter<Task, TaskTopAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object{
        val SELECT_REMOVE: Int = 3
        val SELECT_EDIT: Int = 4
        val SELECT_DETAILS: Int = 5

        private val DIFF_CALLBACK = object  : DiffUtil.ItemCallback<Task>(){
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id && oldItem.description == newItem.description
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem && oldItem.description == newItem.description

            }
        }

    }

    //Criar o layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemTaskTopBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )

        )
    }

    //Exibir o layout
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)

        holder.binding.textDescription.text = task.description

        holder.binding.btnRemove.setOnClickListener {taskTopSelected(task, SELECT_REMOVE)}
        holder.binding.btnEdit.setOnClickListener {taskTopSelected(task, SELECT_EDIT)}
        holder.binding.btnDetails.setOnClickListener {taskTopSelected(task, SELECT_DETAILS)}

    }

    //Referenciar o layout do XML
    inner class MyViewHolder(val binding: ItemTaskTopBinding) : ViewHolder(binding.root)


}