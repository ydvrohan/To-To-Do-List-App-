package com.example.todoapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ============================================================
// TaskDatabase.kt - Room Database
// This is the main database class. We use the Singleton pattern
// to ensure only ONE instance of the database exists at a time.
// ============================================================

@Database(
    entities = [Task::class], // All tables in our database
    version = 1,              // Increment this when schema changes
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {

    // Room will auto-generate the TaskDao implementation
    abstract fun taskDao(): TaskDao

    companion object {
        // @Volatile ensures the value is always up-to-date across threads
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            // If instance already exists, return it. Otherwise, create a new one.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database" // Name of the SQLite file
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
