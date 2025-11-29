# Mandala View Improvements

## Changes Made

### 1. ✅ Added Toggle Button in ParvaDetailScreen

**What changed:**
- Added a toggle icon button in the top app bar
- Button switches between List view (☰) and Mandala view (◉)
- State is managed locally, so you can toggle freely

**How it works:**
- Click the icon in the top-right corner
- View switches instantly between list and circular mandala
- Your choice persists while viewing that Parva

**Code location:** `ParvaDetailScreen.kt`

---

### 2. ✅ Improved Text Placement in Mandala

**What changed:**
- Text now rotates to follow the arc/circumference
- Full theme names displayed (no truncation)
- Text positioned along the circle edge (70% radius)
- Better readability with rotation

**Technical improvements:**
- Canvas `translate()` and `rotate()` for proper text placement
- Text rotated perpendicular to radius (+90°)
- Anti-aliasing enabled for smooth rendering
- Shadow enhanced for better contrast

**Code location:** `MandalaView.kt` (lines 97-130)

---

### 3. ✅ Reusable MandalaView Component

**Architecture:**
The `MandalaView.kt` component is **fully reusable** and used in:
1. `MahaParvaDetailScreen.kt` - Shows 7 Parvas
2. `ParvaDetailScreen.kt` - Shows 7 Saptahas

**Any changes to `MandalaView.kt` automatically apply everywhere!**

**Why this matters:**
- Single source of truth
- Consistent behavior across the app
- Easy to maintain and enhance
- Future improvements benefit all mandala instances

---

## Visual Improvements

### Before:
- Text truncated: "Begi", "Prac", "Disc"
- Text not rotated (always horizontal)
- Could get cut off at edges
- No toggle button to switch views

### After:
- Full text: "Beginning", "Practice", "Discernment"
- Text rotates to follow arc
- Better positioned along circumference
- Toggle button (List ⟷ Mandala) in top bar

---

## How to Use

### Viewing a Parva (49 days):

1. **Tap a Maha-Parva** from home screen
2. **Tap any Parva petal** (one of 7 colored sections)
3. **You see the List view by default**
4. **Tap the icon** in top-right corner (looks like ◉)
5. **Mandala view appears!** Shows 7 Saptahas in circular form
6. **Tap again** to switch back to List
7. **Tap any section** to drill into that Saptaha

---

## Code Architecture

### Reusable Component Pattern

```kotlin
// MandalaView.kt - Single reusable component
@Composable
fun MandalaView(
    sections: List<MandalaSection>,
    currentSectionIndex: Int?,
    onSectionClick: (Int) -> Unit,
    modifier: Modifier
)

// Used in MahaParvaDetailScreen.kt
MandalaView(
    sections = mahaParva.parvas.map { it.toMandalaSection() },
    ...
)

// Used in ParvaDetailScreen.kt
MandalaView(
    sections = parva.saptahas.map { it.toMandalaSection() },
    ...
)
```

### Data Model

```kotlin
data class MandalaSection(
    val label: String,        // Full theme name
    val color: Color,         // VIBGYOR color
    val centerText: String,   // Section number
    val theme: CycleTheme     // Theme enum
)
```

---

## Text Rendering Details

### Rotation Logic

```kotlin
val middleAngle = startAngle + anglePerSection / 2f
val labelAngle = middleAngle * PI / 180f

// Position
val labelX = centerX + labelRadius * cos(labelAngle)
val labelY = centerY + labelRadius * sin(labelAngle)

// Rotate to follow arc
rotate(middleAngle + 90f) // +90° makes text perpendicular to radius
```

### Why +90 degrees?

Without rotation: Text points toward center (hard to read)
With +90° rotation: Text follows the circular arc (easy to read)

```
        Beginning ←─── rotated to follow curve
       ╱
      ╱
    ●  ← center
```

---

## Future Enhancement Ideas

### Already Possible (Easy):
- [ ] Adjust `labelRadius` to move text closer/farther from edge
- [ ] Change `textSize` for larger/smaller text
- [ ] Modify colors or shadows
- [ ] Add animation on section tap

### Future Features (Medium):
- [ ] Pinch to zoom mandala
- [ ] Long-press for section details
- [ ] Progress ring around outer edge
- [ ] Completion indicators on sections

### Advanced (Complex):
- [ ] 3D mandala with depth
- [ ] Animated transitions between sections
- [ ] Curved text along actual arc path
- [ ] Custom fonts/calligraphy

---

## Testing the Changes

### Test Checklist:

1. ✅ **Home Screen → Tap Maha-Parva**
   - See 7-petal mandala with full theme names

2. ✅ **Tap any petal**
   - Default to List view

3. ✅ **Tap toggle icon (top-right)**
   - Switch to Mandala view (7 Saptahas)
   - Text should be rotated and readable

4. ✅ **Tap toggle again**
   - Switch back to List view

5. ✅ **Tap any Saptaha (in either view)**
   - Navigate to 7 Dinas

6. ✅ **Verify text doesn't cut off**
   - Check all 7 sections
   - Text should be fully visible

---

## Benefits

### User Experience:
- ✅ Choice of view mode (List or Mandala)
- ✅ Better text readability
- ✅ More beautiful visualization
- ✅ Full theme names (no abbreviations)

### Developer Experience:
- ✅ Single reusable component
- ✅ Easy to maintain
- ✅ Consistent behavior
- ✅ Well-documented code

### Design:
- ✅ Professional look
- ✅ Follows material design
- ✅ VIBGYOR color harmony
- ✅ Sacred geometry precision

---

## Files Modified

1. **`ParvaDetailScreen.kt`**
   - Added toggle button
   - Added local state for view mode
   - Updated to use full theme names

2. **`MandalaView.kt`**
   - Improved text rotation
   - Better positioning (70% radius)
   - Enhanced rendering (anti-aliasing, shadows)

3. **`MahaParvaDetailScreen.kt`**
   - Updated to use full theme names
   - Benefits from improved MandalaView rendering

---

## Summary

All mandala views now:
- Show **full theme names**
- Have **rotated text** along the circumference
- Are **fully reusable** (DRY principle)
- Support **toggle between List/Mandala** views

**One component. Multiple uses. Consistent excellence.** 🪷

---

**Version**: 1.1.0  
**Date**: November 2025  
**Status**: ✅ Complete & Tested

