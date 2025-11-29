# Yojana Mode - Planning Toggle Feature

## Overview

**Yojana** (योजना) means "planning" in Sanskrit. This feature provides a toggle between two interaction modes:

1. **Today Mode** (Default): Only the current/active period is accessible
2. **Yojana Mode**: All periods (past, present, future) are accessible for planning

## User Experience

### Toggle Location

The Yojana mode toggle appears in the **top app bar of the Maha-Parva detail screen**:
- **Icon**: Edit/Planning icon
- **Color**: 
  - Default (OFF): White
  - Active (ON): Tertiary color (highlighted)

### Mode Inheritance

The toggle state is set at the Maha-Parva level and **cascades down** to all sub-levels:
```
Maha-Parva (toggle here)
  └─ Parva screens (inherit)
      └─ Saptaha screens (inherit)
          └─ Dina screens (inherit)
```

## Behavior by Screen

### Maha-Parva Detail Screen (7 Parvas Mandala)

**Today Mode (OFF):**
- Only the current Parva is colored and clickable
- Other Parvas are grayed out
- Clicking a grayed Parva shows toast: "Enable Yojana mode to plan future periods"

**Yojana Mode (ON):**
- All 7 Parvas are colored (using their gradient colors)
- All Parvas are clickable for planning

### Parva Detail Screen (7 Saptahas - Mandala or List View)

**Today Mode (OFF):**
- Only the current Saptaha is colored and clickable
- Other Saptahas are grayed (mandala) or have reduced opacity (list)
- Clicking a grayed Saptaha shows toast

**Yojana Mode (ON):**
- All 7 Saptahas are fully colored and clickable
- Text at top: "Yojana Mode: Plan all Saptahas"

### Saptaha Detail Screen (7 Dinas List)

**Today Mode (OFF):**
- Only today's Dina is colored and clickable
- Other Dinas are grayed out with lower opacity
- Clicking a grayed Dina shows toast

**Yojana Mode (ON):**
- All 7 Dinas are fully colored and clickable
- Text at top: "Yojana Mode: Plan all days"

## Visual Styling

### Today Mode (Non-active sections)
- **Color**: Gray
- **Opacity**: 0.4-0.6
- **Clickable**: No
- **Feedback**: Toast message on tap

### Yojana Mode (All sections)
- **Color**: Original theme/gradient colors
- **Opacity**: Full
- **Clickable**: Yes
- **Feedback**: Normal navigation

## Implementation Details

### State Management

1. **Toggle State**: Stored as `yojanaMode: Boolean` in `MahaParvaDetailScreen`
2. **State Passing**: Threaded through navigation as URL parameter
3. **Default Value**: `false` (Today Mode)

### Navigation Flow

```kotlin
// MahaParvaDetailScreen
onParvaClick = { parvaIndex, yojanaMode ->
    navController.navigate("mahaparva/$id/parva/$parvaIndex/list/$yojanaMode")
}

// ParvaDetailScreen
onSaptahaClick = { saptahaIndex, yojanaMode ->
    navController.navigate("mahaparva/$id/parva/$p/saptaha/$saptahaIndex/$yojanaMode")
}
```

### Key Components Modified

1. **MahaParvaDetailScreen.kt**
   - Added toggle button in top bar
   - Added `yojanaMode` state
   - Gray out logic for non-active Parvas
   - Toast message on restricted tap

2. **ParvaDetailScreen.kt**
   - Accept `yojanaMode` parameter
   - Gray out logic for non-active Saptahas (both mandala and list views)
   - Pass mode to Saptaha navigation

3. **SaptahaDetailScreen.kt**
   - Accept `yojanaMode` parameter
   - Gray out logic for non-active Dinas
   - Mode indicator text

4. **ParvaApp.kt**
   - Updated navigation routes to include `yojanaMode` parameter
   - Thread the mode through all screen transitions

### Toast Messages

All toast messages use the same text:
```
"Enable Yojana mode to plan future periods"
```

## Use Cases

### For Daily Practice (Today Mode)
- User wants to focus only on today's tasks
- Prevents accidental navigation to future/past periods
- Clean, uncluttered view

### For Planning (Yojana Mode)
- User wants to set goals for upcoming weeks
- User wants to review past periods
- User wants to adjust future intentions

## Design Philosophy

This feature embodies the balance between:
- **Mindfulness**: Stay present (Today Mode)
- **Planning**: Prepare for the future (Yojana Mode)

The Sanskrit term "Yojana" aligns with the app's spiritual and methodical approach to personal development through the 343-day Maha-Parva cycle.

## Future Enhancements (Potential)

1. **Remember toggle state** per Maha-Parva (currently resets to OFF)
2. **Keyboard shortcuts** for power users
3. **Quick toggle** from any screen (floating action button)
4. **Analytics**: Track how often users use planning mode
5. **Smart suggestions**: "You haven't planned next week yet" notifications

