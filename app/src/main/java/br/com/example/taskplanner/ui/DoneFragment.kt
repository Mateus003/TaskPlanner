package br.com.example.taskplanner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.taskplanner.data.model.Status
import br.com.example.taskplanner.data.model.Task
import br.com.example.taskplanner.databinding.FragmentDoneBinding
import br.com.example.taskplanner.ui.adapter.TaskAdapter

class DoneFragment : Fragment() {

    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(getListTask())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun initRecyclerView(taskList: List<Task>){
        taskAdapter = TaskAdapter(taskList)
        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        binding.rvTask.adapter = taskAdapter
    }

    private fun getListTask(): List<Task>{
        var tasks =  mutableListOf(Task("as", "estudar progrmação", Status.DONE))


        return tasks
    }

}