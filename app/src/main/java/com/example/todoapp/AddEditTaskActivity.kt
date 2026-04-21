package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.databinding.ActivityAddEditTaskBinding

// ============================================================
// AddEditTaskActivity.kt - Add / Edit Task Screen
// This screen is used both for ADDING a new task and
// EDITING an existing one. It reads extras from the Intent
// to know which mode it's in.
// ============================================================

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTaskBinding

    companion object {
        // Keys for passing data between Activities via Intent
        const val EXTRA_ID = "com.example.todoapp.EXTRA_ID"
        const val EXTRA_TITLE = "com.example.todoapp.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.example.todoapp.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY = "com.example.todoapp.EXTRA_PRIORITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Check if we're EDITING (extras will be present) or ADDING
        if (intent.hasExtra(EXTRA_ID)) {
            // EDIT MODE — pre-fill fields with existing task data
            supportActionBar?.title = "Edit Task"
            binding.etTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            binding.etDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))

            // Set the correct radio button based on existing priority
            when (intent.getIntExtra(EXTRA_PRIORITY, 1)) {
                3 -> binding.rbHigh.isChecked = true
                2 -> binding.rbMedium.isChecked = true
                else -> binding.rbLow.isChecked = true
            }
        } else {
            // ADD MODE — start with empty fields, Low priority selected
            supportActionBar?.title = "Add Task"
            binding.rbLow.isChecked = true
        }

        // Save button click
        binding.btnSaveTask.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        // Validate: title is required
        if (title.isEmpty()) {
            binding.etTitle.error = "Title cannot be empty"
            binding.etTitle.requestFocus()
            return
        }

        // Get selected priority
        val priority = when {
            binding.rbHigh.isChecked -> 3
            binding.rbMedium.isChecked -> 2
            else -> 1
        }

        // Package the data into an Intent result
        val resultIntent = Intent().apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_DESCRIPTION, description)
            putExtra(EXTRA_PRIORITY, priority)

            // Only include ID when editing
            if (intent.hasExtra(EXTRA_ID)) {
                putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
            }
        }

        // Send the result back to MainActivity
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    // Handle back arrow in toolbar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
