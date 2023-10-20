package com.example.taskplanner.View.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.R
import com.example.taskplanner.View.fragments.BaseFragment
import com.example.taskplanner.databinding.FragmentRecoverAccountBinding
import com.example.taskplanner.util.FirebaseHelp
import com.example.taskplanner.util.initToolbar
import com.example.taskplanner.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverAccountFragment : BaseFragment() {
    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        initListener()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initListener(){
        binding.buttonSent.setOnClickListener {
            validateInformation()
            binding.pgRecover.isVisible = true

        }
    }

    private fun validateInformation() {
        val email = binding.editTextEmailRecover.text.toString().trim().lowercase()

        if (email.isNullOrEmpty()) {
            showBottomSheet(R.string.report_email)
            binding.pgRecover.isVisible = false
        } else {
            recoverAccount(email)
            hideKeyboard()
        }
    }

    private fun recoverAccount(email: String) {
        FirebaseHelp.getAuth().sendPasswordResetEmail(email).addOnCompleteListener { task->
            binding.pgRecover.isVisible = false
            if (task.isSuccessful){
                showBottomSheet(R.string.email_send)
                findNavController().navigate(R.id.action_recoverAccountFragment_to_loginFragment)
            }else{
                showBottomSheet(R.string.failure_email_sent)
            }
        }

    }


}



