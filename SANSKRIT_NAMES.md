# Sanskrit Names for Themes

## ✅ What Was Added

Each theme now has a **Sanskrit name** displayed alongside the English name, giving the app a more authentic spiritual/philosophical feel.

## 🕉️ The Seven Themes with Sanskrit Names

| # | Sanskrit | English | Meaning |
|---|----------|---------|---------|
| 1 | **Arambha** (आरम्भ) | Beginning | Commencement, starting point |
| 2 | **Abhyasa** (अभ्यास) | Practice | Repeated practice, discipline |
| 3 | **Viveka** (विवेक) | Discernment | Discrimination, wisdom, clarity |
| 4 | **Aroha** (आरोह) | Ascent | Climbing, rising, ascension |
| 5 | **Siddhi** (सिद्धि) | Mastery | Accomplishment, perfection, attainment |
| 6 | **Pravaha** (प्रवाह) | Flow | Stream, continuous flow |
| 7 | **Nava** (नव) | Renewal | New, fresh, renewal |

## 📱 Display Format

### Parva Level
```
Top Bar: "Parva 1 - Arambha"
Legend: "1. Arambha - Beginning"
        "2. Abhyasa - Practice"
        "3. Viveka - Discernment"
        ...
```

### Saptaha Level
```
Top Bar: "Saptaha 3 - Viveka"

Card Header: "Saptaha 3 - Viveka"
             "Discernment" (English subtitle)
             
List View: "Saptaha 1 - Arambha"
           "Practice"
           [Description]
```

### Dina Level
```
Breadcrumb: "Parva 2 - Abhyasa (Practice)"
            "Saptaha 4 - Aroha (Ascent)"
```

## 🎨 Visual Hierarchy

```
┌─────────────────────────────────────┐
│ Saptaha 1 - Arambha              ← Primary (Large, Colored)
│ Beginning                         ← Secondary (Medium, Regular)
│ The foundation is laid...         ← Tertiary (Small, Muted)
└─────────────────────────────────────┘
```

**Emphasis**: Sanskrit name gets the visual prominence, English provides context.

## 📝 Code Changes

### 1. Updated `CycleTheme.kt`

Added `sanskritName` parameter to the enum:

```kotlin
enum class CycleTheme(
    val displayName: String,
    val sanskritName: String,  // ← New parameter
    val description: String,
    val color: Color,
    val goalPrompts: List<String>
) {
    BEGINNING(
        "Beginning",
        "Arambha",  // ← Sanskrit name
        "The foundation is laid...",
        Color(0xFF8B00FF),
        listOf(...)
    ),
    // ... rest
}
```

### 2. Updated Display Screens

**ParvaDetailScreen**:
- Title bar: `"Parva ${parva.number} - ${parva.theme.sanskritName}"`
- List items: `"Saptaha ${saptaha.number} - ${saptaha.theme.sanskritName}"`

**SaptahaDetailScreen**:
- Title bar: `"Saptaha ${saptaha.number} - ${saptaha.theme.sanskritName}"`
- Header: `"Saptaha ${saptaha.number} - ${saptaha.theme.sanskritName}"`

**DinaDetailScreen**:
- Breadcrumb: `"Parva ${parva.number} - ${parva.theme.sanskritName} (${parva.theme.displayName})"`
- Breadcrumb: `"Saptaha ${saptaha.number} - ${saptaha.theme.sanskritName} (${saptaha.theme.displayName})"`

**MahaParvaDetailScreen**:
- Legend: `"${parva.number}. ${parva.theme.sanskritName} - ${parva.theme.displayName}"`

## 🌍 Why These Sanskrit Names?

### 1. **Arambha** (Beginning)
- From Sanskrit root "ā-rambh" meaning "to begin"
- Common in classical texts for "commencement"
- Easy to pronounce for English speakers

### 2. **Abhyasa** (Practice)
- Core concept in yoga philosophy
- Mentioned in Patanjali's Yoga Sutras
- Means "repeated practice" or "discipline"

### 3. **Viveka** (Discernment)
- Key concept in Advaita Vedanta
- Means "discrimination" or "discernment"
- Often paired with "vairagya" (dispassion)

### 4. **Aroha** (Ascent)
- Simple, phonetic Sanskrit word
- Literally means "climbing" or "ascending"
- Shorter alternative to "Aarohana"

### 5. **Siddhi** (Mastery)
- Well-known Sanskrit term
- Means "accomplishment," "perfection," or "attainment"
- Often refers to spiritual powers or mastery

### 6. **Pravaha** (Flow)
- From "pra-vah" meaning "to flow"
- Poetic word for a stream or current
- Captures the effortless quality of the Flow stage

### 7. **Nava** (Renewal)
- Simple Sanskrit word meaning "new"
- Used in compounds like "nav-varsha" (new year)
- Shorter and more accessible than "Navinīkarana"

## 💡 Design Rationale

### Why Sanskrit?
1. **Cultural Authenticity**: The app uses Sanskrit concepts (Maha-Parva, Parva, Saptaha, Dina)
2. **Philosophical Depth**: Sanskrit terms carry deeper meaning than English translations
3. **Spiritual Connection**: Many users interested in 343-day cycles appreciate Sanskrit
4. **Aesthetic Appeal**: Sanskrit names add elegance and gravitas

### Why Latin Script?
1. **Accessibility**: Most users can read Latin script
2. **Pronunciation**: No need to learn Devanagari script
3. **Universal**: Works across all devices and platforms
4. **Practical**: Easier to type and search

### Display Strategy
- **Primary**: Sanskrit name (large, colored)
- **Context**: English name (medium, for clarity)
- **Details**: Description (small, for understanding)

This balances authenticity with usability.

## 🔄 Mandala View Labels

In mandala views, the labels still show the **full English name** for readability:

```
Mandala Petal Labels:
- "Beginning" (not "Arambha")
- "Practice" (not "Abhyasa")
- etc.
```

**Why?** Rotating text in mandalas needs to be instantly recognizable. Sanskrit names are better for linear text.

**Future**: Could add a setting to toggle between Sanskrit/English in mandalas.

## 📚 Cultural Context

These seven stages mirror concepts from various wisdom traditions:

### Yoga Sutras Connection
- **Abhyasa** (Practice) + **Viveka** (Discernment) are core Patanjali concepts
- **Siddhi** (Mastery) refers to the fruits of sustained practice

### Vedantic Philosophy
- **Viveka** (Discernment) is one of the "four qualifications" (Sadhana Chatushtaya)
- **Aroha** (Ascent) parallels spiritual elevation

### General Philosophy
- **Arambha** → **Abhyasa** → **Siddhi** mirrors the classical learning progression
- **Pravaha** (Flow) aligns with the concept of effortless action (Sahaja)
- **Nava** (Renewal) reflects the cyclical nature of spiritual growth

## 🎯 User Experience

### Before
```
Parva Detail Screen
├── Title: "Beginning Parva"
└── List: "Saptaha 1", "Saptaha 2", ...
```

### After ✨
```
Parva Detail Screen
├── Title: "Parva 1 - Arambha"
└── List: "Saptaha 1 - Arambha"
          "Practice"
          "The foundation is laid..."
```

**Benefits**:
- ✅ More culturally authentic
- ✅ Sanskrit names stand out visually
- ✅ English provides context
- ✅ Feels more meaningful and intentional

## 🔮 Future Enhancements

### 1. Devanagari Script (Optional)
```kotlin
enum class CycleTheme(
    val sanskritName: String,
    val sanskritDevanagari: String,  // ← Optional display
    ...
) {
    BEGINNING(
        "Arambha",
        "आरम्भ",
        ...
    )
}
```

**Display**: "Arambha (आरम्भ)" or toggle in settings

### 2. Pronunciation Guide
```kotlin
enum class CycleTheme(
    val sanskritName: String,
    val pronunciation: String,  // ← Help users pronounce
    ...
) {
    BEGINNING(
        "Arambha",
        "ah-rahm-bha",
        ...
    )
}
```

### 3. Etymology / Meaning
Add a `etymology` field explaining the root and deeper meaning:
```kotlin
val etymology: String = "From ā-√rambh, meaning 'to begin' or 'to commence'"
```

### 4. Settings Toggle
```
Settings > Display
[ ] Show Sanskrit names
[ ] Show Devanagari script
[ ] Show pronunciation guide
```

## ✅ Testing Checklist

- [ ] Sanskrit names appear in Parva titles
- [ ] Sanskrit names appear in Saptaha titles
- [ ] Sanskrit names appear in lists
- [ ] Sanskrit names appear in breadcrumbs (Dina screen)
- [ ] Sanskrit names appear in MahaParva legend
- [ ] English names still visible for context
- [ ] Text doesn't overflow on small screens
- [ ] All 7 Sanskrit names are unique
- [ ] Spelling is correct (Arambha, Abhyasa, etc.)

## 📖 Summary

**Added**:
- 🕉️ Sanskrit names for all 7 themes
- 📱 Updated UI to display "Parva 1 - Arambha" format
- 🎨 Visual hierarchy (Sanskrit primary, English secondary)
- 📚 Cultural authenticity with practical accessibility

**The Seven Sanskrit Names**:
1. **Arambha** - Beginning
2. **Abhyasa** - Practice
3. **Viveka** - Discernment
4. **Aroha** - Ascent
5. **Siddhi** - Mastery
6. **Pravaha** - Flow
7. **Nava** - Renewal

**Your journey through the 343 days now has deeper cultural resonance! 🎉**

