package com.example.taskplanner.View.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskplanner.R
import com.example.taskplanner.View.adapter.TaskAdapter
import com.example.taskplanner.databinding.FragmentDoneBinding
import com.example.taskplanner.model.Status
import com.example.taskplanner.model.Task
import com.example.taskplanner.util.Constants.TASK
import com.example.taskplanner.util.Constants.TASKS
import com.example.taskplanner.util.FirebaseHelp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DoneFragment : Fragment() {
    private lateinit var binding: FragmentDoneBinding

    private lateinit var taskAdapter: TaskAdapter

    private val tasks = mutableListOf<Task>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        addTask()
        
    }

    private fun addTask() {
        FirebaseHelp.getDatabase().child("tasks").child(FirebaseHelp.getIdUser())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    tasks.clear()

                    for (ds in snapshot.children) {
                        val task = ds.getValue(Task::class.java) as Task
                        if (task.status == Status.DONE) {
                            tasks.add(task)
                        }
                    }

                    initRecyclerView(tasks)

                    if (tasks.isEmpty()) {
                        binding.statusDone.isVisible = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), R.string.logout, Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun deleteTask(task: Task) {
        FirebaseHelp.getDatabase().child(TASKS).child(FirebaseHelp.getIdUser()).child(task.id)
            .removeValue()
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), R.string.task_removed_successful, Toast.LENGTH_SHORT).show()
                    tasks.remove(task)
                    taskAdapter.submitList(tasks)
                }
            }
    }

    private fun initRecyclerView(taskList: List<Task>){
        taskAdapter = TaskAdapter(){task,option->
            optionSelected(task, option)

        }


        taskAdapter.submitList(taskList)

        with(binding.rvDone){
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter

        }

        binding.statusDone.isVisible = taskAdapter.itemCount <= 0


    }


    private fun optionSelected(task: Task, option: Int){
        when(option){
            TaskAdapter.SELECTED_REMOVE -> deleteTask(task)
            TaskAdapter.SELECTED_EDIT -> {
                val bundle = Bundle()
                bundle.putParcelable(TASK, task)
                findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment, bundle)
            }
        }
    }

}