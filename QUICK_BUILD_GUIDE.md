# Quick Build & Test Guide

## 🚀 Build the App

### Option 1: Android Studio (Recommended)
1. Open Android Studio
2. Click "Sync Project with Gradle Files" (Elephant icon)
3. Wait for Gradle sync to complete (downloads Room, ViewModel dependencies)
4. Click "Build" → "Rebuild Project"
5. Click "Run" (Green triangle) to install on device/emulator

### Option 2: Command Line
```bash
cd /Users/ragnor/StudioProjects/mandala
./gradlew clean
./gradlew build
./gradlew installDebug
```

---

## ✅ What to Test

### 1. Create Your First Maha-Parva
- Tap the FAB (+) button
- Enter title, description
- Select a mandala style (try "Lotus Flower")
- Optionally set custom colors
- Save

### 2. Test Persistence
- **Navigate Back** → Should still see your Maha-Parva
- **Force Close App** → Reopen → Should still be there! 🎉

### 3. Explore the Hierarchy
- Tap your Maha-Parva → See 7 Parvas in mandala view
- Tap a Parva → See 7 Saptahas (toggle between list/mandala)
- Tap a Saptaha → See 7 Dinas
- Tap a Dina → Add intention and notes

### 4. Test Goals
- At Parva level: Add a custom goal
- At Saptaha level: Add a weekly goal
- Verify they persist after navigating away

### 5. Test Editing
- From home, tap the edit icon (✏️) on a Maha-Parva
- Change mandala style or colors
- Save and verify changes persist

---

## 🐛 If You Get Errors

### Kotlin Version Conflict
If you see "Conflicting declarations" or version errors:
```bash
./gradlew clean
```
Then rebuild.

### Database Schema Errors
If Room complains about schema:
```kotlin
// In ParvaDatabase.kt, change:
.fallbackToDestructiveMigration() // This resets DB on schema change
```
This is fine for development. Production apps need proper migrations.

### ViewModel Not Found
Make sure you synced Gradle after adding dependencies.

---

## 📊 Verify Database

### View Database Contents (Android Studio)
1. Run app on emulator/device
2. Tools → Device File Explorer
3. Navigate to: `data/data/com.aravind.parva/databases/`
4. Right-click `parva_database` → "Save As"
5. Open with any SQLite viewer

### View via ADB
```bash
adb shell
run-as com.aravind.parva
cd databases
sqlite3 parva_database
.tables
SELECT * FROM maha_parvas;
.exit
```

---

## 🎯 Expected Behavior

### ✅ WORKS NOW:
- ✅ Create Maha-Parva → Persists forever
- ✅ Navigate to any level → Shows actual data
- ✅ Edit goals → Saves to database
- ✅ Add notes → Persists
- ✅ App restart → All data intact
- ✅ Device rotation → No data loss

### 🔜 TODO (Future):
- Export/Import functionality
- Email journal to accountability partner
- Supabase cloud sync
- Search & filter

---

## 📝 Key Files Changed

### New Files (Persistence Layer)
```
app/src/main/java/com/aravind/parva/
├── data/
│   ├── local/
│   │   ├── Converters.kt           ← Type converters for Room
│   │   ├── ParvaDatabase.kt        ← Database singleton
│   │   ├── dao/
│   │   │   └── MahaParvaDao.kt     ← Database operations
│   │   └── entities/
│   │       └── MahaParvaEntity.kt  ← Database table
│   └── repository/
│       └── MahaParvaRepository.kt  ← Data abstraction
├── viewmodel/
│   ├── HomeViewModel.kt            ← Home screen state
│   ├── MahaParvaViewModel.kt       ← Detail screens state
│   └── ViewModelFactory.kt         ← ViewModel creation
```

### Modified Files
```
app/build.gradle.kts                 ← Added Room, ViewModel deps
app/src/main/java/com/aravind/parva/
├── ParvaApp.kt                      ← Wired ViewModels to screens
└── ui/screens/
    ├── HomeScreen.kt                ← Uses HomeViewModel
    ├── MahaParvaDetailScreen.kt     ← Uses MahaParvaViewModel
    ├── ParvaDetailScreen.kt         ← Uses MahaParvaViewModel
    ├── SaptahaDetailScreen.kt       ← Uses MahaParvaViewModel
    └── DinaDetailScreen.kt          ← Uses MahaParvaViewModel
```

---

## 💡 Pro Tips

### Debugging Data Flow
Add logs to see what's happening:

```kotlin
// In HomeViewModel.kt init block
init {
    viewModelScope.launch {
        repository.allMahaParvas.collect { list ->
            android.util.Log.d("HomeVM", "📊 Loaded ${list.size} Maha-Parvas")
        }
    }
}
```

View logs:
```bash
adb logcat | grep "HomeVM"
```

### Clear Database (Fresh Start)
```bash
adb shell
pm clear com.aravind.parva
```

---

## 🎨 Architecture at a Glance

```
HomeScreen ──→ HomeViewModel ──→ Repository ──→ Room Database
                    ↑                              ↓
                    └──────── Flow<Data> ──────────┘
                    (Automatic UI updates!)
```

---

## 📚 Learn More

- **Room**: https://developer.android.com/training/data-storage/room
- **ViewModel**: https://developer.android.com/topic/libraries/architecture/viewmodel
- **Flow**: https://developer.android.com/kotlin/flow
- **Repository Pattern**: https://developer.android.com/codelabs/android-room-with-a-view-kotlin

---

## 🎉 You're All Set!

Your app now has:
- ✅ Full data persistence with Room
- ✅ Reactive UI with Flow
- ✅ Lifecycle-aware ViewModels
- ✅ Clean architecture (UI → ViewModel → Repository → Database)
- ✅ Ready for Supabase integration

**Happy building! 🚀**

Questions? Check `PERSISTENCE_ARCHITECTURE.md` for in-depth explanations.

