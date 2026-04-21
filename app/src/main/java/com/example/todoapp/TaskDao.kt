package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.room.*

// ============================================================
// TaskDao.kt - Data Access Object
// DAO defines all the database operations (CRUD).
// Room auto-generates the implementation at compile time.
// ============================================================

@Dao
interface TaskDao {

    // INSERT a new task. If a conflict occurs, replace it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    // UPDATE an existing task
    @Update
    suspend fun updateTask(task: Task)

    // DELETE a specific task
    @Delete
    suspend fun deleteTask(task: Task)

    // GET ALL tasks ordered by priority (high first), then by date
    // LiveData = automatically updates UI when database changes!
    @Query("SELECT * FROM task_table ORDER BY priority DESC, createdAt DESC")
    fun getAllTasks(): LiveData<List<Task>>

    // GET only completed tasks
    @Query("SELECT * FROM task_table WHERE isCompleted = 1")
    fun getCompletedTasks(): LiveData<List<Task>>

    // GET only pending tasks
    @Query("SELECT * FROM task_table WHERE isCompleted = 0")
    fun getPendingTasks(): LiveData<List<Task>>

    // DELETE all completed tasks at once
    @Query("DELETE FROM task_table WHERE isCompleted = 1")
    suspend fun deleteCompletedTasks()

    // SEARCH tasks by title
    @Query("SELECT * FROM task_table WHERE title LIKE '%' || :searchQuery || '%'")
    fun searchTasks(searchQuery: String): LiveData<List<Task>>
}
