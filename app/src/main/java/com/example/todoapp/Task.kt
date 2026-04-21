package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

// ============================================================
// Task.kt - Data Model
// This is our "Entity" — it maps to a table in Room Database.
// ============================================================

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,           // Auto-generated unique ID
    val title: String,          // Task title (required)
    val description: String,    // Task details (optional)
    val isCompleted: Boolean = false, // Completion status
    val priority: Int = 1,      // 1=Low, 2=Medium, 3=High
    val createdAt: Long = System.currentTimeMillis() // Timestamp
)
