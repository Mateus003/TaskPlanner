package br.com.example.taskplanner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.taskplanner.R
import br.com.example.taskplanner.data.model.Status
import br.com.example.taskplanner.data.model.Task
import br.com.example.taskplanner.databinding.FragmentTodoBinding
import br.com.example.taskplanner.ui.adapter.TaskAdapter

class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initRecyclerView()
        getListTask()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)
        }
    }

    private fun initRecyclerView(){
        taskAdapter = TaskAdapter(requireContext()) { task, option ->
            optionSelected(task, option)
        }
        val concatAdapter = taskAdapter
        with(binding.rvTask){
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = concatAdapter
        }

    }

    private fun getListTask(){

        val tasks =  mutableListOf(Task("ads", "estudar progrmação", Status.TODO))

        taskAdapter.submitList(tasks)


    }

    private fun optionSelected(task: Task, option: Int){
        when(option){
            TaskAdapter.SELECT_REMOVE ->{ Toast.makeText(requireContext(), "Removendo ${task.description}", Toast.LENGTH_LONG).show()}
            TaskAdapter.SELECT_EDIT ->{}
            TaskAdapter.SELECT_DETAILS ->{}
            TaskAdapter.SELECT_NEXT->{}

        }
    }

}