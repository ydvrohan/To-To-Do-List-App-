# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Room database classes
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao interface *

# Keep data classes (Kotlin)
-keep class com.example.todoapp.Task { *; }

# Keep ViewModel classes
-keep class * extends androidx.lifecycle.ViewModel { *; }
