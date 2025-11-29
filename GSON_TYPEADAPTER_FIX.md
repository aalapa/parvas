# Gson TypeAdapter Fix

## ❌ The Error

```
[ksp] java.lang.IllegalArgumentException: List has more than one element.
	at kotlin.collections.CollectionsKt___CollectionsKt.single(_Collections.kt:610)
	at androidx.room.ext.Xelement_extKt.getValueClassUnderlyingProperty(xelement_ext.kt:35)
	at androidx.room.solver.TypeAdapterStore.createDefaultTypeAdapter(TypeAdapterStore.kt:383)
```

## 🔍 Root Cause

**The Problem**: Room tries to serialize complex nested data structures to JSON, but encounters types that Gson doesn't know how to handle:

```kotlin
@Entity
data class MahaParvaEntity(
    val parvas: List<Parva>,  // ← Converted to JSON by Gson
    ...
)

// Inside List<Parva> we have:
data class Parva(
    val startDate: LocalDate,      // ← Gson doesn't know how to serialize this!
    val theme: ThemeWithColor,     // ← Contains Color...
    val saptahas: List<Saptaha>    // ← More nested structures
)

data class ThemeWithColor(
    val theme: CycleTheme,
    val color: Color               // ← Compose Color, not a primitive!
)
```

### Why This Happens

1. **Room Level**: We have `@TypeConverter` for `Color` and `LocalDate` ✅
2. **Gson Level**: When serializing `List<Parva>` to JSON, Gson encounters:
   - `LocalDate` fields → Gson doesn't know how to serialize ❌
   - `Color` objects → Gson doesn't know how to serialize ❌
   - `ThemeWithColor` → Contains Color ❌

**Room's converters only work at the entity level, not during Gson serialization!**

## ✅ The Solution

Create custom Gson TypeAdapters for all complex types nested in our JSON structures:

### 1. LocalDate TypeAdapter

```kotlin
class LocalDateTypeAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    override fun serialize(src: LocalDate?, ...): JsonElement {
        return JsonPrimitive(src?.toString() ?: "")
    }

    override fun deserialize(json: JsonElement?, ...): LocalDate {
        return LocalDate.parse(json?.asString ?: "1970-01-01")
    }
}
```

**Why**: `Parva`, `Saptaha`, and `Dina` all have `LocalDate` fields.

### 2. Color TypeAdapter

```kotlin
class ColorTypeAdapter : JsonSerializer<Color>, JsonDeserializer<Color> {
    override fun serialize(src: Color?, ...): JsonElement {
        return JsonPrimitive(src?.toArgb() ?: 0)  // Convert to ARGB Int
    }

    override fun deserialize(json: JsonElement?, ...): Color {
        return Color(json?.asInt ?: 0)  // Reconstruct from Int
    }
}
```

**Why**: `ThemeWithColor` contains `Color` objects.

### 3. ThemeWithColor TypeAdapter

```kotlin
class ThemeWithColorTypeAdapter : JsonSerializer<ThemeWithColor>, JsonDeserializer<ThemeWithColor> {
    override fun serialize(src: ThemeWithColor?, ...): JsonElement {
        val obj = JsonObject()
        obj.addProperty("theme", src.theme.name)
        obj.addProperty("color", src.color.toArgb())
        return obj
    }

    override fun deserialize(json: JsonElement?, ...): ThemeWithColor {
        val obj = json?.asJsonObject
        val themeName = obj.get("theme").asString
        val colorInt = obj.get("color").asInt
        return ThemeWithColor(
            theme = CycleTheme.valueOf(themeName),
            color = Color(colorInt)
        )
    }
}
```

**Why**: `Parva` and `Saptaha` have `theme: ThemeWithColor` fields.

### 4. Register with Gson

```kotlin
class Converters {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
        .registerTypeAdapter(Color::class.java, ColorTypeAdapter())
        .registerTypeAdapter(ThemeWithColor::class.java, ThemeWithColorTypeAdapter())
        .create()
    
    // ... rest of converters
}
```

## 📊 Data Flow

### Before Fix ❌

```
MahaParva.parvas (List<Parva>)
    ↓
@TypeConverter → gson.toJson(parvas)
    ↓
Gson encounters LocalDate → ERROR! 💥
Gson encounters Color → ERROR! 💥
```

### After Fix ✅

```
MahaParva.parvas (List<Parva>)
    ↓
@TypeConverter → gson.toJson(parvas)
    ↓
Gson uses LocalDateTypeAdapter → "2024-01-01" ✅
Gson uses ColorTypeAdapter → -16776961 (ARGB) ✅
Gson uses ThemeWithColorTypeAdapter → {"theme":"Beginning","color":-16776961} ✅
    ↓
JSON String saved to database
```

## 🎯 What Gets Serialized

### Example: Maha-Parva → JSON

```kotlin
MahaParva(
    parvas = listOf(
        Parva(
            startDate = LocalDate.of(2024, 1, 1),
            theme = ThemeWithColor(
                theme = CycleTheme.Beginning,
                color = Color(0xFFFF0000)
            )
        )
    )
)
```

**Serializes to**:

```json
{
  "parvas": [
    {
      "number": 1,
      "startDate": "2024-01-01",
      "endDate": "2024-02-18",
      "theme": {
        "theme": "Beginning",
        "color": -65536
      },
      "saptahas": [...]
    }
  ]
}
```

## 🔧 Files Modified

### `Converters.kt`

**Added**:
- `LocalDateTypeAdapter` class
- `ColorTypeAdapter` class
- `ThemeWithColorTypeAdapter` class

**Modified**:
- `gson` initialization to register all custom type adapters

## 🐛 Debugging Tips

### Test JSON Serialization

Add to your Repository or ViewModel:

```kotlin
import android.util.Log

val testParva = Parva(...)
val gson = GsonBuilder()
    .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
    .registerTypeAdapter(Color::class.java, ColorTypeAdapter())
    .registerTypeAdapter(ThemeWithColor::class.java, ThemeWithColorTypeAdapter())
    .create()

val json = gson.toJson(testParva)
Log.d("GsonTest", "Serialized: $json")

val deserialized = gson.fromJson(json, Parva::class.java)
Log.d("GsonTest", "Deserialized: $deserialized")
```

### Verify Database Contents

```bash
adb shell
run-as com.aravind.parva
cd databases
sqlite3 parva_database
SELECT id, title, substr(parvas, 1, 100) FROM maha_parvas;
```

You should see JSON like:
```
[{"number":1,"startDate":"2024-01-01","theme":{"theme":"Beginning","color":-16776961},...
```

## 📚 Alternative Approaches (We Didn't Use)

### 1. Store Color as Int Directly ❌

```kotlin
data class ThemeWithColor(
    val theme: CycleTheme,
    val colorArgb: Int  // Instead of Color
)
```

**Cons**: Loses type safety, more error-prone.

### 2. Flatten the Structure ❌

```kotlin
@Entity
data class ParvaEntity(...)  // Separate table for Parva

@Entity
data class SaptahaEntity(...)  // Separate table for Saptaha

// Foreign keys, relations, etc.
```

**Cons**: 
- Complex queries with JOINs
- More tables to manage
- Harder to maintain data integrity

### 3. Use Protocol Buffers ❌

**Cons**: 
- Overkill for this use case
- More complex setup
- Less human-readable

## ✅ Why Our Solution is Best

1. ✅ **Simple**: Just register type adapters
2. ✅ **Type-Safe**: Still use `Color` and `LocalDate` objects
3. ✅ **Single Table**: All data in one entity, simpler queries
4. ✅ **JSON**: Human-readable in database for debugging
5. ✅ **Efficient**: Gson serialization is fast
6. ✅ **Maintainable**: Easy to add new types later

## 🚀 Build Now

```bash
./gradlew clean
./gradlew build
```

Should complete successfully! 🎉

## 📖 Key Learnings

### Room @TypeConverter vs Gson TypeAdapter

| Aspect | Room @TypeConverter | Gson TypeAdapter |
|--------|---------------------|------------------|
| **Purpose** | Convert types for Room database columns | Convert types during JSON serialization |
| **Scope** | Entity-level fields only | Works anywhere in JSON structure |
| **When Used** | Reading/writing to SQLite | Inside nested structures being serialized |
| **Example** | `Color` → `Int` for database column | `Color` → `JsonPrimitive` for JSON |

### Important Distinction

```kotlin
// Room converter - for entity fields
@TypeConverter
fun fromColor(color: Color?): Int? = color?.toArgb()

// Gson adapter - for nested JSON serialization
class ColorTypeAdapter : JsonSerializer<Color> {
    override fun serialize(...) = JsonPrimitive(src?.toArgb())
}
```

**You need BOTH**:
- Room converter: For top-level entity fields
- Gson adapter: For fields nested inside JSON strings

## 🔮 Future Enhancements

### Add Pretty Printing (Optional)

```kotlin
private val gson = GsonBuilder()
    .registerTypeAdapter(...)
    .setPrettyPrinting()  // Makes JSON more readable
    .create()
```

**Trade-off**: Larger database size vs easier debugging.

### Add Null Safety

```kotlin
class LocalDateTypeAdapter {
    override fun deserialize(...): LocalDate {
        val dateString = json?.asString
        require(!dateString.isNullOrBlank()) { "Date cannot be null" }
        return LocalDate.parse(dateString)
    }
}
```

---

## ✅ Verification Checklist

After building:

- [ ] Build completes without KSP errors
- [ ] Create a Maha-Parva and save it
- [ ] Query database - verify JSON contains proper dates and colors
- [ ] Navigate to detail screens - verify data loads correctly
- [ ] Edit goals - verify changes persist
- [ ] Restart app - verify all data still there

---

**Your build should now work! 🎉**

The custom Gson TypeAdapters allow proper serialization of all complex nested types.

