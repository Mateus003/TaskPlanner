package br.com.example.taskplanner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.taskplanner.data.model.Status
import br.com.example.taskplanner.data.model.Task
import br.com.example.taskplanner.databinding.FragmentDoingBinding
import br.com.example.taskplanner.ui.adapter.TaskAdapter

class DoingFragment : Fragment() {

    private var _binding: FragmentDoingBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getListTask()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecyclerView(){
        taskAdapter = TaskAdapter(requireContext()) { task, option ->
            optionSelected(task, option)

        }
        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        binding.rvTask.adapter = taskAdapter
    }

    private fun getListTask(){
        var tasks =  mutableListOf(Task("as", "estudar progrmação", Status.DOING))


        taskAdapter.submitList(tasks)
    }

    private fun optionSelected(task: Task, option: Int){
        when(option){
            TaskAdapter.SELECT_BACK->{}
            TaskAdapter.SELECT_REMOVE ->{}
            TaskAdapter.SELECT_EDIT ->{}
            TaskAdapter.SELECT_DETAILS ->{}
            TaskAdapter.SELECT_NEXT->{}

        }
    }

}