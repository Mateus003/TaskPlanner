package com.example.taskplanner.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.taskplanner.R
import com.example.taskplanner.View.adapter.ViewPagerAdapter
import com.example.taskplanner.View.fragments.ui.DoingFragment
import com.example.taskplanner.View.fragments.ui.DoneFragment
import com.example.taskplanner.View.fragments.ui.TodoFragment
import com.example.taskplanner.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTab()

        initListener()
        logout()

        setUserName()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initTab(){
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        val viewPager = binding.viewPager
        viewPager.adapter = viewPagerAdapter
        val tabLayout = binding.tabLayout

        viewPagerAdapter.addFragment(TodoFragment(), R.string.todo)
        viewPagerAdapter.addFragment(DoingFragment(), R.string.doing)
        viewPagerAdapter.addFragment(DoneFragment(), R.string.done)


        viewPager.offscreenPageLimit = viewPagerAdapter.itemCount

        TabLayoutMediator(tabLayout, viewPager){tab, position->
            tab.text = getString(viewPagerAdapter.getTitle(position))

        }.attach()

    }

    private fun initListener(){
        binding.addTask.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)
        }

    }


    private fun logout(){
        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    private fun setUserName(){
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val dataBase = FirebaseFirestore.getInstance()
        dataBase.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name")
                    binding.containerTop.isVisible = true
                    binding.addTask.isVisible = true
                    binding.viewPager.isVisible = true
                    binding.tabLayout.isVisible = true
                    binding.pgHome.isVisible = false

                    binding.name.text = name

                }
            }
    }

}