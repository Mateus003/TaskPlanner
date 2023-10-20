package com.example.taskplanner.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseHelp {
    companion object{
        fun getDatabase() = Firebase.database.reference

        fun getAuth()= FirebaseAuth.getInstance()


        fun getIdUser() = getAuth().currentUser!!.uid


        fun getIsAuth() = getAuth().currentUser != null

    }
}