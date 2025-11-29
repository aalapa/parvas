# Version Management & APK Naming

## ✅ Configured Features

### 1. Custom APK Naming
APKs are now named: `parva-{version}-{buildType}.apk`

**Examples**:
```
parva-1.0.0-debug.apk
parva-1.0.1-debug.apk
parva-1.0.5-release.apk
```

### 2. Auto-Incrementing Version
Every time you build (assemble), the **patch version** auto-increments:
```
Build 1: 1.0.0 → 1.0.1
Build 2: 1.0.1 → 1.0.2
Build 3: 1.0.2 → 1.0.3
```

## 📁 Version Storage

**File**: `version.properties` (in project root)

```properties
versionMajor=1
versionMinor=0
versionPatch=0
versionCode=1
```

- **versionMajor**: Major version (e.g., 1.x.x) - Manual update
- **versionMinor**: Minor version (e.g., x.1.x) - Manual update
- **versionPatch**: Patch version (e.g., x.x.1) - **Auto-increments**
- **versionCode**: Android version code - **Auto-increments** (for Play Store)

## 🔢 Version Format

**versionName**: `{major}.{minor}.{patch}`
- Example: `1.0.5`
- Shown to users in app info

**versionCode**: Integer (1, 2, 3, ...)
- Used by Google Play Store
- Must increase with each release
- Auto-increments with patch

## 🔨 How It Works

### On Every Build
1. Gradle reads `version.properties`
2. Gets current version numbers
3. Builds APK with current version
4. Increments patch and versionCode
5. Saves back to `version.properties`
6. Names APK: `parva-{version}-{buildType}.apk`

### Example Build Sequence
```bash
# Build 1
./gradlew assembleDebug
# Creates: parva-1.0.0-debug.apk
# Updates: version.properties → 1.0.1

# Build 2
./gradlew assembleDebug
# Creates: parva-1.0.1-debug.apk
# Updates: version.properties → 1.0.2

# Build 3
./gradlew assembleRelease
# Creates: parva-1.0.2-release.apk
# Updates: version.properties → 1.0.3
```

## 📍 APK Location

After building, find your APK at:
```
app/build/outputs/apk/debug/parva-1.0.X-debug.apk
app/build/outputs/apk/release/parva-1.0.X-release.apk
```

## 🎛️ Manual Version Updates

### Update Minor Version (New Features)
Edit `version.properties`:
```properties
versionMajor=1
versionMinor=1  ← Change from 0 to 1
versionPatch=0  ← Reset to 0
```

Result: `1.1.0`

### Update Major Version (Breaking Changes)
Edit `version.properties`:
```properties
versionMajor=2  ← Change from 1 to 2
versionMinor=0  ← Reset to 0
versionPatch=0  ← Reset to 0
```

Result: `2.0.0`

## 📊 Version Tracking in Git

**Option 1: Track version.properties** (Recommended)
- Commit `version.properties` to git
- See version history in git log
- Team shares same version numbers

**Option 2: Don't track version.properties**
Add to `.gitignore`:
```
version.properties
```
- Each developer has own patch numbers
- Only commit when releasing

## 🚀 Release Workflow

### For Development Builds
```bash
# Debug builds (auto-increment)
./gradlew assembleDebug
# → parva-1.0.X-debug.apk
```

### For Release Builds
```bash
# 1. Manually update version if needed
# Edit version.properties: 1.1.0

# 2. Build release
./gradlew assembleRelease
# → parva-1.1.0-release.apk

# 3. Commit version
git add version.properties
git commit -m "Release v1.1.0"
git tag v1.1.0
```

## 🎯 Best Practices

### Semantic Versioning
```
MAJOR.MINOR.PATCH

MAJOR: Breaking changes (1.x.x → 2.0.0)
MINOR: New features (1.0.x → 1.1.0)
PATCH: Bug fixes (1.0.0 → 1.0.1) - Auto
```

### When to Update Manually

**Major (1.x.x → 2.0.0)**:
- Complete redesign
- Breaking changes to data format
- Incompatible with previous versions

**Minor (1.0.x → 1.1.0)**:
- New features (e.g., hold management)
- New screens or major UI changes
- Database schema changes

**Patch (auto-incremented)**:
- Bug fixes
- Small UI tweaks
- Performance improvements

## 📝 Example Timeline

```
v1.0.0 - Initial release
v1.0.1 - Fixed color gradient bug
v1.0.2 - Fixed Sanskrit names display
v1.0.3 - Fixed date picker
v1.1.0 - Added hold management feature
v1.1.1 - Fixed hold calculation
v1.1.2 - Improved hold UI
v1.2.0 - Added export/import
v2.0.0 - Supabase cloud sync (breaking)
```

## 🔧 Troubleshooting

### Version Not Incrementing
**Problem**: Build doesn't increment version
**Solution**: Make sure you're running `assemble` tasks:
```bash
./gradlew assembleDebug  ✅
./gradlew assembleRelease ✅
./gradlew build ✅ (includes assemble)
./gradlew compileDebugKotlin ❌ (doesn't trigger)
```

### APK Has Wrong Name
**Problem**: APK still named `app-debug.apk`
**Solution**: 
1. Clean build: `./gradlew clean`
2. Rebuild: `./gradlew assembleDebug`
3. Check: `app/build/outputs/apk/debug/`

### Version File Missing
**Problem**: `Could not read version.properties!`
**Solution**: Create `version.properties` in project root:
```properties
versionMajor=1
versionMinor=0
versionPatch=0
versionCode=1
```

## 🎨 Customization

### Change APK Name Format
Edit `app/build.gradle.kts`:
```kotlin
// Current format
output.outputFileName = "parva-${versionName}-${buildType}.apk"

// Alternative formats
output.outputFileName = "parva-v${versionName}-${buildType}.apk"
// → parva-v1.0.5-debug.apk

output.outputFileName = "parva_${versionName}_${buildType}.apk"
// → parva_1.0.5_debug.apk

output.outputFileName = "parva-${buildType}-${versionName}.apk"
// → parva-debug-1.0.5.apk
```

### Disable Auto-Increment
Comment out the auto-increment section:
```kotlin
// gradle.taskGraph.whenReady {
//     if (allTasks.any { it.name.contains("assemble", ignoreCase = true) }) {
//         incrementVersion()
//     }
// }
```

### Increment Only on Release Builds
```kotlin
gradle.taskGraph.whenReady {
    if (allTasks.any { it.name.contains("assembleRelease", ignoreCase = true) }) {
        incrementVersion()
    }
}
```

## ✅ Summary

**Configured**:
- ✅ Custom APK names: `parva-{version}-{buildType}.apk`
- ✅ Auto-increment patch version on every build
- ✅ Version stored in `version.properties`
- ✅ Both versionName and versionCode auto-increment

**Benefits**:
- 📦 Clear APK naming
- 🔢 No manual version management needed
- 📈 Version history tracked
- 🚀 Ready for Play Store releases

**Next build will create**: `parva-1.0.1-debug.apk` 🎉

