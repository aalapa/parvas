# Parva - 343-Day Progression System

An Android app built with Kotlin and Jetpack Compose for tracking the complete Maha-Parva cycle: a 343-day structured progression system.

## What is Maha-Parva?

A **Maha-Parva** is a 343-day structured progression system designed to develop habits, skills, and goals through a meaningful, cyclic rhythm of growth.

### Structure (7 × 7 × 7 Model)

- **1 Maha-Parva = 343 Days** (The complete cycle)
- **7 Parvas per Maha-Parva** (each 49 days)
- **7 Saptahas per Parva** (each 7 days - "week")
- **7 Dinas per Saptaha** (individual days)

### The 7 Themes (Repeat at Every Level)

The same 7 themes flow through Parvas (49 days) and Saptahas (7 days):

1. **Beginning** (Violet) - Foundation is laid
2. **Practice** (Indigo) - Repetition builds strength
3. **Discernment** (Blue) - Clarity emerges
4. **Ascent** (Green) - Growth accelerates
5. **Mastery** (Yellow) - Skills solidify
6. **Flow** (Orange) - In rhythm
7. **Renewal** (Red) - Integration and reflection

### Daily Themes (7 Dinas - Repeat Weekly)

1. **Initiate** - Start fresh
2. **Stabilize** - Find your footing
3. **Observe** - Pay attention
4. **Strengthen** - Push through resistance
5. **Expand** - Go further
6. **Integrate** - Bring it together
7. **Reflect** - Look back and learn

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material3
- **Background Tasks**: WorkManager
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Key Features

✅ **Maha-Parva Management**
- Create multiple 343-day cycles
- Auto-calculate current position (Parva/Saptaha/Dina)
- Track progress with visual indicators

✅ **Mandala Visualization**
- Beautiful 7-petal circular mandala view
- VIBGYOR color-coded themes
- Interactive navigation

✅ **Notes & Journaling**
- Write daily notes for each Dina
- Export journal for any Maha-Parva
- Share via email or save as text file

✅ **Accountability Partner**
- Set up partner email
- Weekly reminders to share progress
- One-tap journal sharing

✅ **Customization**
- Dark theme support
- Toggle between Mandala/List views
- Export/Import all data

## Project Structure

```
app/src/main/java/com/aravind/parva/
├── data/model/
│   ├── MahaParva.kt       # 343-day cycle
│   ├── Parva.kt           # 49-day period
│   ├── Saptaha.kt         # 7-day week
│   ├── Dina.kt            # Single day
│   ├── CycleTheme.kt      # 7 main themes
│   └── DinaTheme.kt       # 7 daily themes
├── ui/
│   ├── components/
│   │   ├── MandalaView.kt          # Circular mandala
│   │   ├── MahaParvaCard.kt        # List item
│   │   └── ...dialogs              # Export/partner dialogs
│   ├── screens/
│   │   ├── HomeScreen.kt           # List of Maha-Parvas
│   │   ├── MahaParvaDetailScreen.kt # 7-petal mandala
│   │   ├── ParvaDetailScreen.kt    # 49-day breakdown
│   │   ├── SaptahaDetailScreen.kt  # 7 Dinas
│   │   ├── DinaDetailScreen.kt     # Daily notes
│   │   └── SettingsScreen.kt       # App settings
│   └── theme/              # Material3 + VIBGYOR colors
├── notifications/
│   ├── WeeklyReminderWorker.kt     # Background task
│   └── NotificationScheduler.kt    # Weekly reminders
├── MainActivity.kt         # Entry point
└── ParvaApp.kt            # Navigation graph
```

## Getting Started

1. **Open in Android Studio**
   ```
   File → Open → /path/to/mandala
   ```

2. **Sync Gradle**
   - Wait for dependencies to download (~2-5 minutes first time)

3. **Run the App**
   - Create/start an Android emulator
   - Click the green Run button (▶️)
   - Or press `Cmd+R` (Mac) / `Ctrl+R` (Windows/Linux)

## How to Use

### 1. Create a Maha-Parva
- Tap the **+** button on home screen
- Enter title and description
- Optionally set accountability partner email

### 2. Navigate the Mandala
- Tap a Maha-Parva card
- See the beautiful 7-petal mandala
- Tap any petal to drill down into that Parva

### 3. Explore Parvas & Saptahas
- View as List or Mandala (change in Settings)
- See current active section highlighted
- Tap to view individual days

### 4. Write Daily Notes
- Tap any Dina (day)
- Write your thoughts and reflections
- Mark day as complete
- Notes are saved with the date

### 5. Export Journal
- Go to Settings (3-dot menu)
- Tap "Export Journal"
- Select which Maha-Parva
- Share via email, save to files, etc.

### 6. Accountability Partner
- Set up partner email when creating Maha-Parva
- Receive weekly notification reminders
- Tap notification to share journal via email

## Building the App

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

## Future Enhancements

- [ ] Data persistence (Room database)
- [ ] Cloud sync
- [ ] Widget support
- [ ] Advanced statistics
- [ ] Custom themes beyond VIBGYOR
- [ ] Goals and habits per Maha-Parva
- [ ] Export to PDF/Markdown

## Contributing

This is a personal learning project. Feel free to fork and adapt for your own use!

## License

MIT License

