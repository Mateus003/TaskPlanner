package com.example.taskplanner.View.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskplanner.R
import com.example.taskplanner.View.TaskViewModel
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

    private val viewModel: TaskViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.addTask(requireContext())
        observerViewModel()


    }


    private fun observerViewModel(){
        viewModel.taskDelete.observe(viewLifecycleOwner){task->
            tasks.remove(task)
            taskAdapter.submitList(tasks)

        }


        viewModel.taskList.observe(viewLifecycleOwner){_taskList->
            val taskList = _taskList.filter {
                it.status == Status.DONE
            }

            initRecyclerView(taskList)


            binding.statusDone.isVisible = taskList.isEmpty()

        }


        viewModel.taskUpdate.observe(viewLifecycleOwner){updateTask->
            if (updateTask.status == Status.DONE){
                val oldList = taskAdapter.currentList

                val newList = oldList.toMutableList().apply {
                    find{
                        it.id == updateTask.id
                    }?.descriptionTask = updateTask.descriptionTask
                }

                val position = newList.indexOfFirst { it.id == updateTask.id }

                taskAdapter.submitList(newList)

                taskAdapter.notifyItemChanged(position)
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
            TaskAdapter.SELECTED_REMOVE -> viewModel.deleteTask(task, requireContext())
            TaskAdapter.SELECTED_EDIT -> {
                val bundle = Bundle()
                bundle.putParcelable(TASK, task)
                findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment, bundle)
            }
        }
    }

}