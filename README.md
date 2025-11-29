# Parva - 49-Day Progression System

An Android app built with Kotlin and Jetpack Compose for tracking 49-day structured progression cycles.

## What is a Parva?

A **Parva** is a 49-day structured progression system designed to develop habits, skills, and goals through a meaningful rhythm of growth.

### Structure (7 × 7 × 1 Model)

- **1 Parva = 49 Days**
- **7 Mini-Parvas per Parva** (each lasting 7 days)
- **Each Mini-Parva contains 7 Macro-Parvas (days)**

## Mini-Parva Themes (7 Weekly Themes)

1. **Beginning** - Foundation is laid
2. **Practice** - Repetition builds strength
3. **Discernment** - Clarity emerges
4. **Ascent** - Growth accelerates
5. **Mastery** - Skills solidify
6. **Flow** - In rhythm
7. **Renewal** - Integration and reflection

## Macro-Parva Themes (7 Daily Themes - Repeat Weekly)

1. **Initiate** - Start fresh
2. **Stabilize** - Find your footing
3. **Observe** - Pay attention
4. **Strengthen** - Push through resistance
5. **Expand** - Go further
6. **Integrate** - Bring it together
7. **Reflect** - Look back and learn

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (to be implemented)
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Project Structure

```
app/src/main/java/com/aravind/parva/
├── data/
│   └── model/          # Data models (Parva, ParvaDay, Themes)
├── ui/
│   ├── components/     # Reusable Compose components
│   ├── screens/        # Screen composables
│   └── theme/          # Material3 theme configuration
├── MainActivity.kt     # Main entry point
└── ParvaApp.kt        # Navigation setup
```

## Getting Started

1. Open the project in Android Studio
2. Sync Gradle files
3. Run the app on an emulator or physical device

## Features

- ✅ Create and track multiple Parvas
- ✅ Visual representation of 49-day structure
- ✅ Mini-Parva themes with descriptions
- ✅ Daily Macro-Parva themes
- ⏳ Progress tracking (coming soon)
- ⏳ Daily check-ins (coming soon)
- ⏳ Data persistence (coming soon)
- ⏳ Notifications (coming soon)

## Building the App

```bash
./gradlew assembleDebug
```

## License

MIT License

