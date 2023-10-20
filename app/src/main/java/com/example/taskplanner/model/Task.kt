package com.example.taskplanner.model

import android.os.Parcelable
import android.widget.TextView
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var descriptionTask: String = "",
    var status: Status = Status.TODO

) : Parcelable
