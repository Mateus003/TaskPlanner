package com.example.taskplanner.View.fragments.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.R
import com.example.taskplanner.View.TaskViewModel
import com.example.taskplanner.databinding.FragmentFormTaskBinding
import com.example.taskplanner.model.Category
import com.example.taskplanner.model.Status
import com.example.taskplanner.model.Task
import com.example.taskplanner.util.Constants.TASK
import com.example.taskplanner.util.FirebaseHelp
import com.example.taskplanner.util.initToolbar
import com.example.taskplanner.util.showBottomSheet
import com.google.android.material.snackbar.Snackbar
import java.util.Date
import java.util.Locale

class FormTaskFragment : BaseFragment() {
    private lateinit var binding: FragmentFormTaskBinding

    private lateinit var task:Task

    private var newTask: Boolean = true
    private var status = Status.TODO

    private  var category: Category = Category.PERSONAL

    private var txtCategory: String = "Pessoal"


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

        if (newTask){
            task = Task()
            task.id= FirebaseHelp.getDatabase().database.reference.push().key!!
            setCategory()

        }

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


    private fun setCategory(){
        binding.radioGroupCategory.setOnCheckedChangeListener { radioGroup, id ->
            when(id){
                R.id.personal->{
                    category = Category.PERSONAL
                    txtCategory = "Pessoal"
                }
                R.id.work ->{
                    category = Category.WORK
                    txtCategory = "Trabalho"

                }
                R.id.finance -> {
                    category = Category.FINANCE
                    txtCategory = "Finanças"

                }
                R.id.others->{
                    category = Category.OTHERS
                    txtCategory= "Outros"

                }
            }
        }

    }

    private fun updateTask(){
        val radioGroupCategory =  binding.radioGroupCategory
        radioGroupCategory.check(R.id.personal)

        val radioGroup = binding.radioGroup
        radioGroup.check(R.id.todo)

        val getTask = arguments?.getParcelable<Task>(TASK)

        getTask?.let {
            this.task = it
            configTask()

            setCategory()


            when(it.status){
                Status.TODO -> radioGroup.check(R.id.todo)
                Status.DOING-> radioGroup.check(R.id.doing)
                else -> radioGroup.check(R.id.done)
            }

            when(it.category){
                Category.PERSONAL-> radioGroupCategory.check(R.id.personal)
                Category.FINANCE-> radioGroupCategory.check(R.id.finance)
                Category.WORK-> radioGroupCategory.check(R.id.work)
                else-> radioGroupCategory.check(R.id.others)

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


            return

        }else{
            task.textCategory = txtCategory

            task.descriptionTask = taskDescription
            task.status = status

            task.category = category

            task.date = getStringDate()

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

    private fun getStringDate(): String {
        val currentTime = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(currentTime)

    }



}