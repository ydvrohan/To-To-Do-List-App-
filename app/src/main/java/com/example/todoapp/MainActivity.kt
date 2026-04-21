package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

// ============================================================
// MainActivity.kt - Main Screen
// Displays the list of tasks. Users can:
//   - See all tasks in a RecyclerView
//   - Swipe to delete a task
//   - Tap a task to edit it
//   - Tap FAB to add a new task
//   - Search tasks via the toolbar
// ============================================================

class MainActivity : AppCompatActivity() {

    // ViewBinding — eliminates findViewById()
    private lateinit var binding: ActivityMainBinding

    // ViewModel — survives screen rotation
    private lateinit var taskViewModel: TaskViewModel

    // Our RecyclerView adapter
    private lateinit var taskAdapter: TaskAdapter

    companion object {
        const val ADD_TASK_REQUEST = 1
        const val EDIT_TASK_REQUEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "My Tasks"

        // Set up ViewModel
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        // Set up RecyclerView
        setupRecyclerView()

        // Observe LiveData — UI updates automatically when data changes
        taskViewModel.allTasks.observe(this) { tasks ->
            taskAdapter.submitList(tasks)
            updateEmptyState(tasks.isEmpty())
        }

        // FAB: Open AddEditTaskActivity to add a new task
        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST)
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskClick = { task ->
                // Open edit screen when task is tapped
                val intent = Intent(this, AddEditTaskActivity::class.java).apply {
                    putExtra(AddEditTaskActivity.EXTRA_ID, task.id)
                    putExtra(AddEditTaskActivity.EXTRA_TITLE, task.title)
                    putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.description)
                    putExtra(AddEditTaskActivity.EXTRA_PRIORITY, task.priority)
                }
                startActivityForResult(intent, EDIT_TASK_REQUEST)
            },
            onCheckboxClick = { task ->
                // Toggle completion status
                taskViewModel.updateTask(task.copy(isCompleted = !task.isCompleted))
            }
        )

        binding.recyclerView.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        // Swipe-to-delete with UNDO via Snackbar
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = taskAdapter.currentList[viewHolder.adapterPosition]
                taskViewModel.deleteTask(task)

                // Show Snackbar with undo option
                Snackbar.make(binding.root, "Task deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        taskViewModel.insertTask(task)
                    }.show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerView.visibility = android.view.View.GONE
            binding.layoutEmptyState.visibility = android.view.View.VISIBLE
        } else {
            binding.recyclerView.visibility = android.view.View.VISIBLE
            binding.layoutEmptyState.visibility = android.view.View.GONE
        }
    }

    // Inflate menu (search icon + overflow menu)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // Set up search
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    taskViewModel.searchTasks(newText).observe(this@MainActivity) { tasks ->
                        taskAdapter.submitList(tasks)
                    }
                }
                return true
            }
        })
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_completed -> {
                taskViewModel.deleteCompletedTasks()
                Snackbar.make(binding.root, "Completed tasks cleared!", Snackbar.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Called when AddEditTaskActivity finishes
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE) ?: return
            val description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION) ?: ""
            val priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1)

            when (requestCode) {
                ADD_TASK_REQUEST -> {
                    val task = Task(title = title, description = description, priority = priority)
                    taskViewModel.insertTask(task)
                    Snackbar.make(binding.root, "Task added! ✓", Snackbar.LENGTH_SHORT).show()
                }
                EDIT_TASK_REQUEST -> {
                    val id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1)
                    if (id == -1) return
                    // Find the existing task to preserve its completed status
                    val existingTask = taskAdapter.currentList.find { it.id == id }
                    val updatedTask = Task(
                        id = id,
                        title = title,
                        description = description,
                        priority = priority,
                        isCompleted = existingTask?.isCompleted ?: false
                    )
                    taskViewModel.updateTask(updatedTask)
                    Snackbar.make(binding.root, "Task updated!", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
