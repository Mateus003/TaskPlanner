package com.example.taskplanner.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.taskplanner.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.initToolbar(toolbar:Toolbar){
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title =""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

}

fun Fragment.showBottomSheet(message: Int, onclick: () -> Unit = {}) {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    val bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater, null, false)

    bottomSheetDialog.setContentView(bottomSheetBinding.root)

    bottomSheetBinding.message.text = getText(message)

    bottomSheetBinding.buttonUnderstood.setOnClickListener {
        onclick()
        bottomSheetDialog.dismiss()
    }

    bottomSheetDialog.show()
}
