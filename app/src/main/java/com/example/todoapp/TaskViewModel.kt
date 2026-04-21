package com.example.todoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ============================================================
// TaskViewModel.kt - ViewModel
// The ViewModel holds UI-related data and survives screen rotations.
// It talks to the Repository and exposes LiveData to the UI.
// ============================================================

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val allTasks: LiveData<List<Task>>
    val pendingTasks: LiveData<List<Task>>

    init {
        // Set up the database → DAO → repository chain
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks
        pendingTasks = repository.pendingTasks
    }

    // Launch a coroutine in the background (IO thread) for DB operations
    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTask(task)
    }

    fun deleteCompletedTasks() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCompletedTasks()
    }

    fun searchTasks(query: String): LiveData<List<Task>> {
        return repository.searchTasks(query)
    }
}
