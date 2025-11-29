# Persistence Architecture - Complete Guide

## 🎯 Problem Solved

**Issue**: Newly created Maha-Parvas were not persisting after navigating back.

**Root Cause**: The app had no shared data layer—each screen maintained its own state, and the `HomeScreen` stored data in local state that disappeared on app restart.

**Solution**: Implemented a complete Room + ViewModel + Repository architecture with proper data persistence.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────┐
│                     UI Layer (Compose)                   │
│  HomeScreen, MahaParvaDetailScreen, ParvaDetailScreen... │
└────────────────────┬────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────┐
│                   ViewModel Layer                        │
│       HomeViewModel, MahaParvaViewModel                  │
│         (Manages UI state & business logic)              │
└────────────────────┬────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────┐
│                  Repository Layer                        │
│              MahaParvaRepository                         │
│     (Single source of truth for all data)                │
└────────────────────┬────────────────────────────────────┘
                     │
                     ↓
┌─────────────────────────────────────────────────────────┐
│                 Data Access Layer (Room)                 │
│          ParvaDatabase, MahaParvaDao                     │
│              (SQLite persistence)                        │
└─────────────────────────────────────────────────────────┘
```

---

## 📦 Key Components

### 1. **Type Converters** (`Converters.kt`)

Converts complex Kotlin types to SQLite-compatible types:

```kotlin
- LocalDate ↔ String
- Color ↔ Int (ARGB)
- MandalaStyle ↔ String
- CycleTheme ↔ String
- List<Parva> ↔ JSON String
- List<Saptaha> ↔ JSON String
- List<Dina> ↔ JSON String
```

### 2. **Database Entity** (`MahaParvaEntity.kt`)

Room entity that represents a table in SQLite:

```kotlin
@Entity(tableName = "maha_parvas")
data class MahaParvaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val startDate: LocalDate,
    val parvas: List<Parva>, // Converted to JSON
    val mandalaStyle: MandalaStyle,
    val customStartColor: Color?,
    ...
)
```

**Why Entity?** Room needs a separate entity class to map Kotlin objects to database tables.

### 3. **Data Access Object** (`MahaParvaDao.kt`)

Defines database operations:

```kotlin
@Dao
interface MahaParvaDao {
    @Query("SELECT * FROM maha_parvas ORDER BY startDate DESC")
    fun getAllMahaParvas(): Flow<List<MahaParvaEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMahaParva(mahaParva: MahaParvaEntity)
    
    @Update
    suspend fun updateMahaParva(mahaParva: MahaParvaEntity)
    
    ...
}
```

**Flow**: Reactive stream that automatically updates UI when data changes.
**suspend**: Kotlin coroutine function for async operations.

### 4. **Room Database** (`ParvaDatabase.kt`)

The database container:

```kotlin
@Database(
    entities = [MahaParvaEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ParvaDatabase : RoomDatabase() {
    abstract fun mahaParvaDao(): MahaParvaDao
    
    companion object {
        fun getDatabase(context: Context): ParvaDatabase { ... }
    }
}
```

**Singleton Pattern**: Ensures only one database instance exists.

### 5. **Repository** (`MahaParvaRepository.kt`)

Single source of truth that abstracts data operations:

```kotlin
class MahaParvaRepository(private val mahaParvaDao: MahaParvaDao) {
    
    // Reactive data stream
    val allMahaParvas: Flow<List<MahaParva>> = 
        mahaParvaDao.getAllMahaParvas().map { entities ->
            entities.map { it.toMahaParva() }
        }
    
    // CRUD operations
    suspend fun saveMahaParva(mahaParva: MahaParva) { ... }
    suspend fun updateMahaParva(mahaParva: MahaParva) { ... }
    suspend fun deleteMahaParva(mahaParva: MahaParva) { ... }
    
    // Specialized updates
    suspend fun updateDina(...) { ... }
    suspend fun updateParvaGoal(...) { ... }
    suspend fun updateSaptahaGoal(...) { ... }
}
```

**Why Repository?** Provides a clean API and prepares for future Supabase integration.

### 6. **ViewModels**

#### `HomeViewModel.kt`
Manages the list of all Maha-Parvas:

```kotlin
class HomeViewModel(private val repository: MahaParvaRepository) : ViewModel() {
    
    val mahaParvas: StateFlow<List<MahaParva>>
    val isLoading: StateFlow<Boolean>
    
    fun createMahaParva(mahaParva: MahaParva) { ... }
    fun updateMahaParva(mahaParva: MahaParva) { ... }
    fun deleteMahaParva(mahaParva: MahaParva) { ... }
}
```

#### `MahaParvaViewModel.kt`
Manages a single Maha-Parva and all its sub-levels:

```kotlin
class MahaParvaViewModel(
    private val repository: MahaParvaRepository,
    private val mahaParvaId: String
) : ViewModel() {
    
    val mahaParva: StateFlow<MahaParva?>
    val isLoading: StateFlow<Boolean>
    
    fun updateParvaGoal(parvaIndex: Int, goal: String) { ... }
    fun updateSaptahaGoal(parvaIndex: Int, saptahaIndex: Int, goal: String) { ... }
    fun updateDina(dayNumber: Int, ...) { ... }
}
```

**Why ViewModels?**
- Survive configuration changes (screen rotation)
- Manage UI state properly
- Handle async operations safely
- Automatic cleanup (cancels coroutines on destroy)

### 7. **ViewModel Factories** (`ViewModelFactory.kt`)

Creates ViewModels with dependencies:

```kotlin
class HomeViewModelFactory(
    private val repository: MahaParvaRepository
) : ViewModelProvider.Factory { ... }

class MahaParvaViewModelFactory(
    private val repository: MahaParvaRepository,
    private val mahaParvaId: String
) : ViewModelProvider.Factory { ... }
```

**Why Factories?** ViewModels with constructor parameters need custom factories.

### 8. **Updated Screens**

All screens now use ViewModels:

```kotlin
// HomeScreen.kt
@Composable
fun HomeScreen(viewModel: HomeViewModel, ...) {
    val mahaParvas by viewModel.mahaParvas.collectAsStateWithLifecycle()
    // UI automatically updates when data changes!
}

// MahaParvaDetailScreen.kt
@Composable
fun MahaParvaDetailScreen(viewModel: MahaParvaViewModel, ...) {
    val mahaParva by viewModel.mahaParva.collectAsStateWithLifecycle()
    // Shows loading state while fetching data
}
```

### 9. **Navigation Wiring** (`ParvaApp.kt`)

```kotlin
@Composable
fun ParvaApp() {
    val context = LocalContext.current
    val database = remember { ParvaDatabase.getDatabase(context) }
    val repository = remember { MahaParvaRepository(database.mahaParvaDao()) }
    
    NavHost(...) {
        composable("home") {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )
            HomeScreen(viewModel = viewModel, ...)
        }
        
        composable("mahaparva/{mahaParvaId}") {
            val mahaParvaId = backStackEntry.arguments?.getString("mahaParvaId")
            val viewModel: MahaParvaViewModel = viewModel(
                key = mahaParvaId, // Unique instance per MahaParva
                factory = MahaParvaViewModelFactory(repository, mahaParvaId)
            )
            MahaParvaDetailScreen(viewModel = viewModel, ...)
        }
        // ... other screens
    }
}
```

---

## 🔄 Data Flow Example

### Creating a New Maha-Parva

```
User taps FAB
     ↓
Dialog opens, user fills details
     ↓
User clicks "Save"
     ↓
HomeScreen calls: viewModel.createMahaParva(newMahaParva)
     ↓
HomeViewModel calls: repository.saveMahaParva(newMahaParva)
     ↓
Repository converts to entity and calls: dao.insertMahaParva(entity)
     ↓
Room saves to SQLite database
     ↓
Room emits updated data through Flow
     ↓
Repository maps entities back to domain models
     ↓
HomeViewModel receives update via Flow
     ↓
UI automatically re-renders with new data!
```

### Navigating to Detail Screen

```
User taps Maha-Parva card
     ↓
Navigation to "mahaparva/{id}"
     ↓
ParvaApp creates MahaParvaViewModel with that ID
     ↓
ViewModel loads data: repository.getMahaParvaById(id)
     ↓
Repository queries database via DAO
     ↓
ViewModel emits data via StateFlow
     ↓
MahaParvaDetailScreen collects data and renders
```

---

## 🎨 Benefits of This Architecture

### ✅ **Data Persistence**
- All data is saved to SQLite database
- Survives app restarts
- Automatic backups (Android backup system)

### ✅ **Reactive UI**
- UI automatically updates when data changes
- No manual refresh needed
- Efficient updates (only changed data)

### ✅ **Configuration Changes**
- Survives screen rotation
- No data loss during lifecycle events

### ✅ **Separation of Concerns**
```
UI Layer       → Displays data, handles user input
ViewModel      → Manages UI state, business logic
Repository     → Single source of truth for data
Database       → Persistent storage
```

### ✅ **Testable**
- Each layer can be tested independently
- Easy to mock repositories for UI tests

### ✅ **Scalable**
- Easy to add new features
- Clean architecture for team collaboration

### ✅ **Future-Ready for Supabase**
```kotlin
class MahaParvaRepository(
    private val localDao: MahaParvaDao,
    private val supabaseClient: SupabaseClient  // Add later!
) {
    suspend fun saveMahaParva(mahaParva: MahaParva) {
        // Save locally
        localDao.insertMahaParva(entity)
        
        // Sync to cloud
        supabaseClient.save(mahaParva)
    }
}
```

---

## 📊 Database Schema

```sql
CREATE TABLE maha_parvas (
    id TEXT PRIMARY KEY NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    startDate TEXT NOT NULL,
    parvas TEXT NOT NULL,  -- JSON array
    accountabilityPartnerEmail TEXT NOT NULL,
    mandalaStyle TEXT NOT NULL,
    customStartColor INTEGER,
    customEndColor INTEGER,
    createdAt TEXT NOT NULL
);
```

---

## 🚀 How Data is Stored

### Example Maha-Parva in Database

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Learn React Mastery",
  "description": "343-day journey to master React",
  "startDate": "2024-01-01",
  "parvas": "[{\"number\":1,\"theme\":{\"displayName\":\"Beginning\",...},\"saptahas\":[...]}]",
  "accountabilityPartnerEmail": "friend@example.com",
  "mandalaStyle": "LotusFlower",
  "customStartColor": -16776961,
  "customEndColor": -65536,
  "createdAt": "2024-01-01"
}
```

The `parvas` field contains the entire hierarchy as JSON:
- 7 Parvas
- Each with 7 Saptahas
- Each with 7 Dinas
- All goals, notes, completion status

---

## 🔍 Key Concepts Explained

### StateFlow vs Flow
- **Flow**: Cold stream, starts emitting when collected
- **StateFlow**: Hot stream, always has a current value, perfect for UI state

### suspend Functions
- Kotlin coroutine function that can pause execution
- Used for async operations without blocking UI thread

### collectAsStateWithLifecycle
- Collects Flow data in Compose
- Automatically starts/stops collection based on lifecycle
- Prevents memory leaks

### remember vs rememberSaveable
- **remember**: Survives recomposition, lost on config change
- **rememberSaveable**: Survives config changes (screen rotation)
- Neither survives process death → Use ViewModel instead!

---

## 🐛 Debugging Tips

### Check Database Contents
```bash
# Via Android Studio
Tools → Device File Explorer → data/data/com.aravind.parva/databases/parva_database

# Via adb
adb shell
run-as com.aravind.parva
cd databases
sqlite3 parva_database
.tables
SELECT * FROM maha_parvas;
```

### View Logs
```kotlin
// Add to ViewModel
init {
    viewModelScope.launch {
        repository.allMahaParvas.collect { list ->
            Log.d("HomeViewModel", "Loaded ${list.size} Maha-Parvas")
        }
    }
}
```

---

## 🎯 What This Fixes

### Before ❌
```
User creates Maha-Parva → Stored in HomeScreen's mutableStateOf
User navigates to detail → Detail screen creates sample data (ignores ID)
User navigates back → HomeScreen still shows created item
App restarts → ALL DATA LOST! 😱
```

### After ✅
```
User creates Maha-Parva → Saved to Room database
User navigates to detail → ViewModel loads actual data from database
User navigates back → Data still there (from database)
App restarts → Data loaded from database, NOTHING LOST! 🎉
```

---

## 🔮 Future Enhancements

1. **Add Supabase Sync**
   - Modify repository to sync with cloud
   - Handle offline-first with conflict resolution

2. **Add Migrations**
   - When schema changes, add Room migrations
   - Preserve user data across updates

3. **Add Caching**
   - Cache frequently accessed data
   - Reduce database queries

4. **Add Search**
   - Full-text search in notes
   - Filter by theme, date range

5. **Add Export/Import**
   - JSON export for backup
   - Import from file

---

## 📚 Related Documentation

- `BUILD_SUMMARY.md` - Initial features
- `MANDALA_STYLES.md` - Mandala rendering
- `COMPLETE_GOAL_SYSTEM.md` - Goal system
- `USER_GUIDE.md` - User manual

---

## ✅ Testing Checklist

After building, test these scenarios:

1. ✅ Create a new Maha-Parva
2. ✅ Navigate back to home → Should still be there
3. ✅ Force-close app → Restart → Should still be there
4. ✅ Edit Maha-Parva style/colors → Should save
5. ✅ Navigate to detail screens → Should show actual data
6. ✅ Add Parva/Saptaha goals → Should persist
7. ✅ Add Dina intention/notes → Should save
8. ✅ Rotate device → Data should remain

---

## 🎉 Summary

You now have a **production-ready persistence layer** with:

✅ Room database for local storage
✅ Repository pattern for clean architecture
✅ ViewModels for lifecycle-aware state management
✅ Reactive UI with Kotlin Flow
✅ Proper separation of concerns
✅ Ready for future Supabase integration

**Your data is now safe and persistent!** 🚀

