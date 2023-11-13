package com.example.taskplanner.View.fragments.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {

    //Esconder teclado do dispostivo
    fun hideKeyboard(){
        val view = activity?.currentFocus
        if (view!=null){
            val inm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inm.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }
}