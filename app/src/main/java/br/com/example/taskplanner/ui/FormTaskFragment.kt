package br.com.example.taskplanner.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import br.com.example.taskplanner.R
import br.com.example.taskplanner.databinding.FragmentFormTaskBinding
import br.com.example.taskplanner.util.initToolbar
import br.com.example.taskplanner.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var task: br.com.example.taskplanner.data.model.Task
    private var newTask: Boolean = true
    private lateinit var status: br.com.example.taskplanner.data.model.Status
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        reference = Firebase.database.reference
        auth = Firebase.auth
        status = br.com.example.taskplanner.data.model.Status.TODO
        initListeners()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener { validateData() }

        binding.rgStatus.setOnCheckedChangeListener {_, id ->
            status = when(id){
                R.id.rbTodo-> {br.com.example.taskplanner.data.model.Status.TODO}
                R.id.rbDoing-> {br.com.example.taskplanner.data.model.Status.DOING}
                else->{br.com.example.taskplanner.data.model.Status.DONE}
            }

        }
    }

    private fun validateData() {
        val description = binding.edtDescription.text.toString().trim()

        if (description.isNotEmpty()) {
            binding.progressBar.isVisible = true

            if (newTask) task = br.com.example.taskplanner.data.model.Task()
            task.description = description
            task.status = status
            task.id = reference.database.reference.push().key ?:""

            saveTask()

        } else {
            showBottomSheet(message = getString(R.string.description_empty_form_task_fragment))
        }
    }


    private fun saveTask(){
        reference
            .child("tasks")
            .child(auth.currentUser?.uid ?: "")
            .child(task.id)
            .setValue(task).addOnCompleteListener{result->
                if (result.isSuccessful){
                    Toast.makeText(requireContext(),R.string.txt_sucess ,Toast.LENGTH_SHORT).show()

                    if (newTask){
                        findNavController().popBackStack()
                    }else{
                        binding.progressBar.isVisible = false
                    }
                }else{
                    showBottomSheet(
                        message = getString(R.string.txt_error)
                    )
                    binding.progressBar.isVisible = false

                }

            }




    }

}