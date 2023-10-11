package com.example.taskplanner.View.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taskplanner.R
import com.example.taskplanner.databinding.FragmentFormTaskBinding
import com.example.taskplanner.util.initToolbar

class FormTaskFragment : Fragment() {
  private lateinit var binding: FragmentFormTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFormTaskBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)


    }


}