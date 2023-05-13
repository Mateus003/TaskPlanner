package br.com.example.taskplanner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.example.taskplanner.data.model.Task
import br.com.example.taskplanner.databinding.ItemTaskBinding

class TaskAdapter(
    private val taskList: List<Task>

): RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {



    //Criar o layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        )
    }
    //Exibir o layout
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task  = taskList[position]

        holder.binding.textDescription.text = task.description

    }
    // Mostrar a quantidade de layout
    override fun getItemCount(): Int {
        return  taskList.size
    }
    //Referenciar o layout do XML
    inner class MyViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root)
}