package com.example.taskplanner.View.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.taskplanner.databinding.FragmentTodoBinding
import com.example.taskplanner.model.Status
import com.example.taskplanner.model.Task
import com.example.taskplanner.util.Constants
import com.example.taskplanner.util.FirebaseHelp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val tasks = mutableListOf<Task>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTask()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addTask() {
        FirebaseHelp.getDatabase().child(Constants.TASKS).child(FirebaseHelp.getIdUser())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    tasks.clear()
                    for (ds in snapshot.children) {
                        val task = ds.getValue(Task::class.java) as Task
                        if (task.status == Status.TODO) {
                            tasks.add(task)
                        }
                    }



                    binding.pgBarTodo.isVisible = false
                    initRecyclerView(tasks)

                    if (tasks.isEmpty()) {
                        binding.statusTodo.isVisible = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), R.string.logout, Toast.LENGTH_SHORT).show()
                }
            })


    }

    private fun initRecyclerView(taskList: List<Task>) {
        taskAdapter = TaskAdapter() { task, option ->
            optionSelected(task, option)
        }
        taskAdapter.submitList(taskList)
        with(binding.rvTodo) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
        binding.statusTodo.isVisible = taskAdapter.itemCount <= 0

    }

    private fun deleteTask(task: Task) {
        FirebaseHelp.getDatabase().child(Constants.TASKS).child(FirebaseHelp.getIdUser()).child(task.id)
            .removeValue()
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), R.string.task_removed_successful, Toast.LENGTH_SHORT).show()
                    tasks.remove(task)
                    taskAdapter.submitList(tasks)
                }
            }
    }

    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            TaskAdapter.SELECTED_REMOVE -> deleteTask(task)
            TaskAdapter.SELECTED_EDIT -> {
                val bundle = Bundle()
                bundle.putParcelable(Constants.TASK, task)
                findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment, bundle)
            }
        }
    }
}
