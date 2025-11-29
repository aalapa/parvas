# Getting Started with Parva App Development

## Opening the Project in Android Studio

1. Open Android Studio
2. Click **File → Open**
3. Navigate to `/Users/ragnor/StudioProjects/mandala`
4. Click **OK**
5. Wait for Gradle sync to complete (this may take a few minutes)

## Understanding the Project Structure

```
mandala/
├── app/                                    # Main application module
│   ├── src/main/
│   │   ├── java/com/aravind/parva/
│   │   │   ├── data/model/              # Data models
│   │   │   │   ├── Parva.kt            # Main 49-day cycle
│   │   │   │   ├── ParvaDay.kt         # Single day representation
│   │   │   │   ├── MiniParvaTheme.kt   # 7 weekly themes
│   │   │   │   └── MacroParvaTheme.kt  # 7 daily themes
│   │   │   ├── ui/
│   │   │   │   ├── screens/            # Screen composables
│   │   │   │   ├── components/         # Reusable components
│   │   │   │   └── theme/              # Material3 theme
│   │   │   ├── MainActivity.kt         # App entry point
│   │   │   └── ParvaApp.kt            # Navigation setup
│   │   └── res/                         # Resources (XML, icons, etc.)
│   └── build.gradle.kts                 # App-level dependencies
├── build.gradle.kts                     # Project-level config
└── settings.gradle.kts                  # Gradle settings
```

## Running the App

### Option 1: Using an Emulator
1. In Android Studio, click **Device Manager** (phone icon in toolbar)
2. Create a new virtual device if you don't have one:
   - Click **Create Device**
   - Select a phone (e.g., Pixel 5)
   - Select a system image (e.g., API 34)
   - Click **Finish**
3. Click the green **Run** button (▶️) or press `Ctrl+R` (Mac: `Cmd+R`)
4. Select your emulator and click **OK**

### Option 2: Using a Physical Device
1. Enable Developer Options on your Android phone:
   - Go to **Settings → About Phone**
   - Tap **Build Number** 7 times
2. Enable USB Debugging:
   - Go to **Settings → Developer Options**
   - Turn on **USB Debugging**
3. Connect your phone via USB
4. Click the green **Run** button
5. Select your device

## Key Files to Understand (Beginner-Friendly)

### 1. MainActivity.kt
- The entry point of your app
- Sets up the UI theme and navigation
- Think of it as the "container" for everything

### 2. ParvaApp.kt
- Handles navigation between screens
- Defines routes: "home" and "parva/{id}"

### 3. Data Models (data/model/)
- **Parva**: A complete 49-day cycle
- **ParvaDay**: A single day with its themes
- **MiniParvaTheme**: One of 7 weekly themes
- **MacroParvaTheme**: One of 7 daily themes

### 4. Screens (ui/screens/)
- **HomeScreen**: Shows list of all Parvas
- **ParvaDetailScreen**: Shows the 49-day breakdown

### 5. Components (ui/components/)
- **ParvaCard**: Shows a Parva in the list
- **MiniParvaSection**: Shows a 7-day Mini-Parva
- **ParvaProgress**: Shows progress bar

## Making Your First Change

Let's change the app's primary color:

1. Open `app/src/main/java/com/aravind/parva/ui/theme/Color.kt`
2. Find the line with `val Purple40`
3. Change it to a different color, e.g.:
   ```kotlin
   val Purple40 = Color(0xFF1E88E5)  // Blue color
   ```
4. Click the **Run** button to see the change

## Common Jetpack Compose Concepts

### Composables
Functions marked with `@Composable` that describe UI:
```kotlin
@Composable
fun MyButton() {
    Button(onClick = { /* action */ }) {
        Text("Click Me")
    }
}
```

### State
Variables that trigger UI updates when changed:
```kotlin
var count by remember { mutableStateOf(0) }
Text("Count: $count")
```

### Modifiers
Chain properties to style/layout components:
```kotlin
Text(
    text = "Hello",
    modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
)
```

## Next Steps

1. **Explore the code**: Read through the files to understand how they work
2. **Modify UI**: Try changing colors, text sizes, padding
3. **Add features**: 
   - Add ability to edit Parva titles
   - Add ability to mark days as complete
   - Add data persistence (save Parvas)
4. **Learn Compose**: Check out [Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)

## Useful Android Studio Shortcuts

- `Cmd/Ctrl + B`: Go to definition
- `Cmd/Ctrl + N`: Find class
- `Cmd/Ctrl + Shift + N`: Find file
- `Cmd/Ctrl + /`: Comment/uncomment line
- `Cmd/Ctrl + Alt + L`: Format code
- `Shift + Shift`: Search everywhere

## Troubleshooting

### Gradle sync fails
- Check your internet connection
- Click **File → Invalidate Caches → Invalidate and Restart**

### App won't run
- Check that you have an emulator/device selected
- Look at the **Logcat** tab for error messages

### Can't find imports
- Click **File → Sync Project with Gradle Files**

## Resources

- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Android Developer Guide](https://developer.android.com/guide)

## Getting Help

If you get stuck:
1. Read the error message carefully
2. Search the error on Google/Stack Overflow
3. Check the official Android documentation
4. Ask on [r/androiddev](https://reddit.com/r/androiddev) or [Stack Overflow](https://stackoverflow.com/questions/tagged/android)

Happy coding! 🚀

