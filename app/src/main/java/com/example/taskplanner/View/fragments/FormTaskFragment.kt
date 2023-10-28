package com.example.taskplanner.View.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.R
import com.example.taskplanner.View.TaskViewModel
import com.example.taskplanner.databinding.FragmentFormTaskBinding
import com.example.taskplanner.model.Status
import com.example.taskplanner.model.Task
import com.example.taskplanner.util.Constants.TASK
import com.example.taskplanner.util.Constants.TASKS
import com.example.taskplanner.util.FirebaseHelp
import com.example.taskplanner.util.initToolbar
import com.example.taskplanner.util.showBottomSheet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FormTaskFragment : BaseFragment() {
    private lateinit var binding: FragmentFormTaskBinding

    private lateinit var task:Task
    private var newTask: Boolean = true
    private var status = Status.TODO

    private val viewModel: TaskViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormTaskBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        initListener()

        updateTask()

    }

    private fun configTask(){
        binding.editTaskDescription.setText(task.descriptionTask)

        setStatus()
        newTask = false
        status = task.status
        binding.txtToolbar.text = getString(R.string.edit_task)
        binding.btnCreate.text = getString(R.string.edit)

    }

    private fun initListener(){
        binding.btnCreate.setOnClickListener {
            validateInformation()
            binding.pgBar.isVisible = true
            observerViewModel()
        }
        setStatus()
    }

    private fun setStatus(){
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, id ->
            when(id){
                R.id.todo-> status = Status.TODO
                R.id.doing -> status = Status.DOING
                R.id.done -> status = Status.DONE
            }
        }

    }

    private fun updateTask(){

        val radioGroup = binding.radioGroup
        radioGroup.check(R.id.todo)
        val getTask = arguments?.getParcelable<Task>(TASK)

        getTask?.let {
            this.task = it
            configTask()

            when(it.status){
                Status.TODO -> radioGroup.check(R.id.todo)
                Status.DOING-> radioGroup.check(R.id.doing)
                else -> radioGroup.check(R.id.done)
            }

        }
    }

    private fun observerViewModel(){
        viewModel.taskInsert.observe(viewLifecycleOwner){task->
            Snackbar.make(requireView(), R.string.save_task_successful, Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()

        }

        viewModel.taskUpdate.observe(viewLifecycleOwner){task->
            Snackbar.make(requireView(), R.string.save_task_successful, Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()

        }
    }



    private fun validateInformation() {
        val taskDescription = binding.editTaskDescription.text.toString().trim()
        if (taskDescription.isNullOrEmpty()){
            showBottomSheet(R.string.required_task_description){
                binding.pgBar.isVisible = false

            }
        }else{
            if (newTask) {
                task = Task()
                task.id= FirebaseHelp.getDatabase().database.reference.push().key!!
            }

            task.descriptionTask = taskDescription
            task.status = status

            hideKeyboard()

            if (newTask){
                viewModel.insertTask(task)
            }else{
                viewModel.updateTask(task)
                binding.pgBar.isVisible = false
                findNavController().popBackStack()

            }
        }

    }

}