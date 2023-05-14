package br.com.example.taskplanner.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import br.com.example.taskplanner.R
import br.com.example.taskplanner.databinding.FragmentRecoverAccountBinding
import br.com.example.taskplanner.util.initToolbar
import br.com.example.taskplanner.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverAccountFragment : Fragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        auth = Firebase.auth
        initListeners()
    }

    private fun initListeners() {
        binding.btnRecover.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim()

        if (email.isNotEmpty()) {
            recoverPassword(email)
            binding.progressBar.isVisible = true
        } else {
            showBottomSheet(message = getString(R.string.email_empty))
        }
    }

    private fun recoverPassword(email: String){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                showBottomSheet(
                        message = "E-mail de redefinição enviado com sucesso",
                        onClick = {
                            findNavController().navigate(R.id.action_recoverAccountFragment_to_loginFragment)
                        }
                    )
                    binding.progressBar.isVisible = false


                }else{
                    showBottomSheet(message = "${task.exception?.message}")
                    binding.progressBar.isVisible = false
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}