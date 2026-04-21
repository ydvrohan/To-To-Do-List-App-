package com.example.todoapp

import androidx.lifecycle.LiveData

// ============================================================
// TaskRepository.kt - Repository
// The Repository is the single source of truth for data.
// It sits between the ViewModel and the database (DAO).
// In a real app, it could also fetch data from a remote server.
// ============================================================

class TaskRepository(private val taskDao: TaskDao) {

    // LiveData from the DAO — UI will observe this
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()
    val completedTasks: LiveData<List<Task>> = taskDao.getCompletedTasks()
    val pendingTasks: LiveData<List<Task>> = taskDao.getPendingTasks()

    // These are 'suspend' functions — they run in a background coroutine
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteCompletedTasks() {
        taskDao.deleteCompletedTasks()
    }

    fun searchTasks(query: String): LiveData<List<Task>> {
        return taskDao.searchTasks(query)
    }
}
