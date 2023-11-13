package com.example.taskplanner.model

import android.os.Parcelable
import android.widget.TextView
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Task(
    var id: String = "",
    var descriptionTask: String = "",
    var status: Status = Status.TODO,
    var date: String = "",
    var category: Category = Category.PERSONAL,
    var textCategory: String = "Pessoal"

) : Parcelable
