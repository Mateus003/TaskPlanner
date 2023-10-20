package com.example.taskplanner.View.fragments.auth

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.R
import com.example.taskplanner.View.fragments.BaseFragment
import com.example.taskplanner.databinding.FragmentRegisterBinding
import com.example.taskplanner.util.Constants
import com.example.taskplanner.util.FirebaseHelp
import com.example.taskplanner.util.initToolbar
import com.example.taskplanner.util.showBottomSheet
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class RegisterFragment : BaseFragment() {
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




        binding.buttonCreate.setOnClickListener {
            validateInformation()
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun validateInformation(){
        val email  =binding.editTextEmail.text.toString().trim()
        val name = binding.editTextName.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (email.isNullOrEmpty()){
            showBottomSheet(R.string.report_email)
        } else if (name.isNullOrEmpty()){
            showBottomSheet(R.string.report_name)
        }else if (password.isNullOrEmpty()){
            showBottomSheet(R.string.report_password)
        }else{
            createUser(email,password, name)
            binding.pgRegister.isVisible = true
            hideKeyboard()

        }

    }

    private fun createUser(email: String, password: String, name: String) {

        FirebaseHelp.getAuth().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseHelp.getAuth().currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateProfileTask ->
                            if (updateProfileTask.isSuccessful) {
                                Toast.makeText(requireContext(), R.string.user_created, Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                                saveNameUser(name)
                            } else {
                                binding.pgRegister.isVisible = false
                            }
                        }
                } else {
                    binding.pgRegister.isVisible = false
                    val error: Int = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> R.string.auth_weak_password_exception
                        is FirebaseAuthInvalidCredentialsException -> R.string.auth_invalid_credentials
                        is FirebaseAuthUserCollisionException -> R.string.auth_user_collision
                        else -> R.string.failure_create_user
                    }
                    showBottomSheet(error)
                }
            }
    }


    private fun saveNameUser(name: String){
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val dataBase = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "name" to name
        )
        dataBase.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "Dados do usuário salvos com sucesso!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Erro ao salvar os dados do usuário", e)
            }
    }
}

