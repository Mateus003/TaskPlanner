package com.example.taskplanner.View.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.R
import com.example.taskplanner.View.fragments.ui.BaseFragment
import com.example.taskplanner.databinding.FragmentLoginBinding
import com.example.taskplanner.util.FirebaseHelp
import com.example.taskplanner.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class LoginFragment : BaseFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recoverPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
        binding.containerRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        binding.buttonLogin.setOnClickListener {
            binding.pgLogin.isVisible = true
            validateInformation()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun validateInformation(){
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        
        if (email.isNullOrEmpty()) {
            showBottomSheet(R.string.report_email)
            binding.pgLogin.isVisible = false

        }else if (password.isNullOrEmpty()){
            showBottomSheet(R.string.report_password)
            binding.pgLogin.isVisible = false

        }else{
            authUser(email,password)
            hideKeyboard()
        }
    }
     override fun onStart() {
        super.onStart()
        checkAuth()

    }
    private fun checkAuth(){
        if (FirebaseHelp.getIsAuth()) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }


    private fun authUser(email:String, password:String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if ((task.isSuccessful)) {

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                }else{
                    binding.pgLogin.isVisible = false
                    val error: Int = when (task.exception) {
                     is FirebaseAuthInvalidUserException -> R.string.user_not_created
                      is FirebaseAuthInvalidCredentialsException ->{
                              if ((task.exception as FirebaseAuthInvalidCredentialsException).errorCode == "ERROR_INVALID_EMAIL") {
                                  R.string.invalid_format_email
                              } else {
                                  R.string.invalid_password
                              }

                      }
                        else -> R.string.login_error
                    }
                    showBottomSheet(error)
                }
            }

    }

}