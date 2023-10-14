package com.example.taskplanner.View.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taskplanner.R
import com.example.taskplanner.databinding.FragmentRegisterBinding
import com.example.taskplanner.util.initToolbar
import com.example.taskplanner.util.showBottomSheet

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun validateInformation(){
        val email  =binding.editTextEmail.text
        val name = binding.editTextName.text
        val password = binding.editTextPassword.text

        if (email.isNullOrEmpty()){
            showBottomSheet(R.string.report_email)
        } else if (name.isNullOrEmpty()){
            showBottomSheet(R.string.report_name)
        }else if (password.isNullOrEmpty()){
            showBottomSheet(R.string.report_password)
        }


    }

}