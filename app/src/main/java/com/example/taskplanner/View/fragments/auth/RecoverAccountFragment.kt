package com.example.taskplanner.View.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.taskplanner.R
import com.example.taskplanner.databinding.FragmentRecoverAccountBinding
import com.example.taskplanner.util.initToolbar
import com.example.taskplanner.util.showBottomSheet
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverAccountFragment : Fragment() {
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
        binding.buttonSent.setOnClickListener {
            validateInformation()
            binding.pgRecover.isVisible = true
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun validateInformation(){
        val email = binding.editTextEmailRecover.text.toString().trim()

        if (email.isNullOrEmpty()){
            showBottomSheet(R.string.report_email)
            binding.pgRecover.isVisible = false
        }else{
            recoverAccount(email)
        }
    }

    private fun recoverAccount(email:String){
        Firebase.auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        showBottomSheet(R.string.email_not_register)
                        binding.pgRecover.isVisible = false

                    } else {
                        Firebase.auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { resetTask ->
                                if (resetTask.isSuccessful) {
                                    showBottomSheet(R.string.email_send)
                                    binding.pgRecover.isVisible = false

                                } else {
                                    showBottomSheet(R.string.failure_email_sent)
                                    binding.pgRecover.isVisible = false

                                }
                            }
                    }
                } else {
                    showBottomSheet(R.string.failure_email_sent)
                    binding.pgRecover.isVisible = false

                }
            }

    }


}