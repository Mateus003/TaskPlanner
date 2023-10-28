package com.example.taskplanner.View

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskplanner.R
import com.example.taskplanner.model.Status
import com.example.taskplanner.model.Task
import com.example.taskplanner.util.Constants
import com.example.taskplanner.util.FirebaseHelp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TaskViewModel : ViewModel() {
    private val _taskDelete = MutableLiveData<Task>()
    val taskDelete: LiveData<Task> = _taskDelete

    private val _taskList =  MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> =  _taskList

    private val _taskInsert = MutableLiveData<Task>()
    val taskInsert: LiveData<Task> = _taskInsert

    private val _taskUpdate = MutableLiveData<Task>()
    val taskUpdate: LiveData<Task> = _taskUpdate


    fun insertTask(task: Task){
        FirebaseHelp.getDatabase().child(Constants.TASKS).child(FirebaseHelp.getIdUser() )
            .child(task.id).setValue(task).addOnCompleteListener {result->
                if (result.isSuccessful){
                    _taskInsert.postValue(task)
                }
            }

    }


    fun addTask(context: Context) {
        FirebaseHelp.getDatabase().child(Constants.TASKS).child(FirebaseHelp.getIdUser())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val taskList = mutableListOf<Task>()
                    for (ds in snapshot.children) {
                        val task = ds.getValue(Task::class.java) as Task
                        taskList.add(task)
                    }
                    _taskList.postValue(taskList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, R.string.logout, Toast.LENGTH_SHORT).show()
                }
            })


    }

    fun updateTask(task: Task){
        val map = mapOf(
            "descriptionTask" to task.descriptionTask,
            "status" to task.status,
        )

        FirebaseHelp.getDatabase().child(Constants.TASKS).child(FirebaseHelp.getIdUser() )
            .child(task.id)
            .updateChildren(map).addOnCompleteListener {result->
                if (result.isSuccessful){
                    _taskInsert.postValue(task)
                }
            }
    }

     fun deleteTask(task: Task, context: Context) {
        FirebaseHelp.getDatabase().child(Constants.TASKS).child(FirebaseHelp.getIdUser()).child(task.id)
            .removeValue()
            .addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(context, R.string.task_removed_successful, Toast.LENGTH_SHORT).show()

                }
            }
    }

}