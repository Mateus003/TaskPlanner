package com.example.taskplanner.View.fragments.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskplanner.R
import com.example.taskplanner.View.TaskViewModel
import com.example.taskplanner.View.adapter.TaskAdapter
import com.example.taskplanner.databinding.FragmentDoingBinding
import com.example.taskplanner.model.Status
import com.example.taskplanner.model.Task
import com.example.taskplanner.util.Constants


class DoingFragment : Fragment() {
    private lateinit var taskAdapter: TaskAdapter

    private var _binding: FragmentDoingBinding? = null
    private val binding get() = _binding!!

    private val tasks = mutableListOf<Task>()

    private val viewModel: TaskViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addTask(requireContext())
        observerViewModel()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observerViewModel(){
        viewModel.taskDelete.observe(viewLifecycleOwner){task->
            tasks.remove(task)
            taskAdapter.submitList(tasks)

        }


        viewModel.taskList.observe(viewLifecycleOwner){_taskList->
            val taskList = _taskList.filter {
                it.status == Status.DOING
            }

            initRecyclerView(taskList)


            binding.statusDoing.isVisible = taskList.isEmpty()

        }


        viewModel.taskUpdate.observe(viewLifecycleOwner){updateTask->
            if (updateTask.status == Status.DOING){
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
        taskAdapter = TaskAdapter(){task, option ->
            optionSelected(task, option)
        }
        taskAdapter.submitList(taskList)
        with(binding.rvDoing){
           layoutManager = LinearLayoutManager(requireContext())
           setHasFixedSize(true)
           adapter = taskAdapter

        }

        binding.statusDoing.isVisible = taskAdapter.itemCount <= 0




    }

    private fun optionSelected(task: Task, option: Int){
        when(option){
            TaskAdapter.SELECTED_REMOVE -> viewModel.deleteTask(context = requireContext(), task = task)
            TaskAdapter.SELECTED_EDIT -> {
                val bundle = Bundle()
                bundle.putParcelable(Constants.TASK, task)
                findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment, bundle)
            }
    }



}
}