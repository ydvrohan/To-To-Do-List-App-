# 📋 TodoApp — Beginner Android Project

A fully functional **To-Do List Android App** built with Kotlin.
Perfect for beginners to learn core Android concepts and add to a resume.

---

## 🚀 How to Run

1. Install **Android Studio** (latest version): https://developer.android.com/studio
2. Open Android Studio → **File → Open** → Select this `TodoApp` folder
3. Wait for **Gradle sync** to complete (first time may take 2–5 minutes)
4. Click the green **▶ Run** button or press `Shift + F10`
5. Choose an emulator or connect a real Android device

> **Minimum Android version:** Android 7.0 (API 24)

---

## ✨ Features

| Feature | Description |
|---|---|
| ➕ Add Tasks | Create tasks with title, description, and priority |
| ✏️ Edit Tasks | Tap any task to modify it |
| ✅ Complete Tasks | Check off tasks — they get a strikethrough |
| 🗑️ Swipe to Delete | Swipe left or right to delete, with Undo option |
| 🔍 Search | Real-time task search from the toolbar |
| 🎨 Priority Levels | Low (green), Medium (orange), High (red) color indicators |
| 💾 Persistent Storage | Tasks are saved even after closing the app (Room DB) |
| 📭 Empty State | Friendly screen shown when no tasks exist |

---

## 🏗️ Architecture & Tech Stack

This project uses **MVVM (Model-View-ViewModel)** architecture:

```
UI Layer          →  MainActivity, AddEditTaskActivity
ViewModel Layer   →  TaskViewModel
Repository Layer  →  TaskRepository
Data Layer        →  TaskDao, TaskDatabase (Room/SQLite)
```

### Libraries Used

| Library | Purpose |
|---|---|
| **Room** | Local database (SQLite wrapper) |
| **LiveData** | Observable data that auto-updates UI |
| **ViewModel** | Survives screen rotation |
| **RecyclerView** | Efficient scrollable list |
| **Coroutines** | Background thread operations |
| **Material Design 3** | Modern UI components |
| **ViewBinding** | Type-safe view references |

---

## 📁 Project Structure

```
app/src/main/
├── java/com/example/todoapp/
│   ├── Task.kt                 → Room Entity (data model)
│   ├── TaskDao.kt              → Database queries (CRUD)
│   ├── TaskDatabase.kt         → Room Database singleton
│   ├── TaskRepository.kt       → Single source of truth
│   ├── TaskViewModel.kt        → Business logic + LiveData
│   ├── TaskAdapter.kt          → RecyclerView adapter
│   ├── MainActivity.kt         → Main list screen
│   └── AddEditTaskActivity.kt  → Add/Edit task screen
└── res/
    ├── layout/
    │   ├── activity_main.xml         → Main screen layout
    │   ├── activity_add_edit_task.xml → Form layout
    │   └── item_task.xml             → List item layout
    ├── menu/
    │   └── main_menu.xml             → Toolbar menu
    ├── drawable/
    │   └── bg_priority_badge.xml     → Pill shape for badges
    └── values/
        ├── colors.xml
        ├── strings.xml
        └── themes.xml
```

---

## 📝 Resume Description

> *"Developed a full-featured Android To-Do application using Kotlin, implementing MVVM architecture with Room Database for local data persistence, LiveData for reactive UI updates, RecyclerView with DiffUtil for efficient list rendering, and Material Design 3 components for a polished user experience."*

### Keywords to highlight on your resume:
`Kotlin` · `Android SDK` · `MVVM Architecture` · `Room Database` · `LiveData` · `ViewModel` · `RecyclerView` · `Coroutines` · `Material Design` · `ViewBinding` · `SQLite`

---

## 🔮 Ideas to Extend the Project (for extra credit!)

- [ ] Add **due dates** with DatePickerDialog
- [ ] Add **notifications** using WorkManager
- [ ] Add **categories/tags** for tasks
- [ ] Implement **dark mode** toggle
- [ ] Add **sorting options** (by date, priority, name)
- [ ] Sync tasks to **Firebase** (cloud storage)
- [ ] Add **widgets** for the home screen
- [ ] Write **unit tests** with JUnit and Mockito

---

## 🤝 Learning Resources

- [Android Developers Official Docs](https://developer.android.com/docs)
- [Room Database Codelab](https://developer.android.com/codelabs/android-room-with-a-view-kotlin)
- [Kotlin for Android](https://developer.android.com/kotlin)
- [Material Design Components](https://m3.material.io/components)
