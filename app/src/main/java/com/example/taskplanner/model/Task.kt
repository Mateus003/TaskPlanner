package com.example.taskplanner.model

import android.os.Parcelable
import android.widget.TextView
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: String,
    val descriptionTask: String,
    val status: Status

) : Parcelable
