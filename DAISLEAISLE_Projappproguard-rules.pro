# Aisle ProGuard/R8 Rules

# Preserve Room annotations and entities
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Database class *
-keepclassmembers @androidx.room.Entity class *
-keepclassmembers class * { @androidx.room.Query *; }
-keepclassmembers class * { @androidx.room.Insert *; }
-keepclassmembers class * { @androidx.room.Update *; }
-keepclassmembers class * { @androidx.room.Delete *; }
-keepclassmembers class * { @androidx.room.RawQuery *; }

# Preserve Compose
-keep class **.compose.** { *; }
-keep @androidx.compose.runtime.Composable class *

# Preserve FileProvider
-keep public class androidx.core.content.FileProvider
-keepclassmembers class androidx.core.content.FileProvider { *; }

# Preserve WorkManager
-keep class androidx.work.** { *; }

# Preserve Room migration classes
-keepclassmembers class **$Migration {
    <init>(...);
}

# Preserve all entities referenced by Room
-keep class com.example.data.local.entities.** { *; }

# Preserve ViewModels
-keep class ** extends androidx.lifecycle.ViewModel { *; }
-keepclassmembers class ** extends androidx.lifecycle.ViewModel { *; }

# Preserve serialization
-keepclassmembers class **$* implements java.io.Serializable
-keepclassmembers class **$* implements android.os.Parcelable

# Preserve Room schema location
-keepattributes SourceFile,LineNumberTable
-keep public class * {
    public protected *;
}

# Suppress warnings for missing classes from transitive dependencies
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
