# Wisdom Quotes Feature

## Overview
The app now displays daily zen koans, stoic quotes, Buddhist wisdom, and Taoist teachings to inspire and guide users throughout their 343-day journey.

## Features

### Deterministic Quote Selection
- Each day (1-343) is mapped to a specific wisdom quote
- Same day always shows the same quote
- Quotes cycle through the collection based on day number: `(day - 1) % total_quotes`

### Quote Collection
- **Current Size**: 300+ curated quotes
- **Easily Expandable**: Can grow to 1500+ by adding more to `WisdomQuotes.kt`
- **Categories**:
  - Zen Koans & Proverbs (~70 quotes)
  - Stoic Philosophy (~75 quotes)
  - Buddhist Wisdom (~70 quotes)
  - Tao Te Ching & Taoist Wisdom (~50 quotes)

### Display Locations

#### 1. Maha-Parva Detail Screen
The wisdom quote is accessible via an info icon (ℹ️):
- Info icon appears next to "Day X of 343" text
- Clicking the icon opens a modal dialog with the quote
- Shows quote for the current day of the Maha-Parva
- Clean, non-intrusive design

```
Day 1 of 343 [ℹ️]
[Progress Bar]
```

**When clicked:**
```
┌─────────────────────────┐
│ Day 1 Wisdom            │
│                         │
│ "Before enlightenment,  │
│ chop wood, carry water. │
│ After enlightenment,    │
│ chop wood, carry water."│
│                         │
│ — Zen Proverb          │
│                         │
│           [Close]       │
└─────────────────────────┘
```

#### 2. Dina (Day) Detail Screen
The wisdom quote is accessible via an info icon (ℹ️):
- Info icon appears next to "Day X of 343" in the info card
- Clicking the icon opens a modal dialog with the quote
- Shows quote specific to that day number
- Author name colored with Saptaha theme color

```
Day 1 of 343 [ℹ️]
Initiate
"A day to begin..."
Friday, November 29, 2024
─────
Parva 1 - Arambha (Beginning)
Saptaha 1 - Arambha (Beginning)
```

**When clicked:**
```
┌─────────────────────────┐
│ Day 1 Wisdom            │
│                         │
│ "The journey of a       │
│ thousand miles begins   │
│ with a single step."    │
│                         │
│ — Lao Tzu              │
│   (colored by theme)    │
│                         │
│           [Close]       │
└─────────────────────────┘
```

## File Structure

```
app/src/main/java/com/aravind/parva/
├── data/
│   ├── model/
│   │   └── WisdomQuote.kt         # Data model & WisdomCollection
│   └── wisdom/
│       └── WisdomQuotes.kt        # 300+ curated quotes
└── ui/screens/
    ├── MahaParvaDetailScreen.kt   # Shows daily quote
    └── DinaDetailScreen.kt        # Shows day-specific quote
```

## Data Model

### WisdomQuote
```kotlin
data class WisdomQuote(
    val text: String,      // Quote content
    val author: String,    // Attribution
    val type: WisdomType   // ZEN_KOAN, STOIC, BUDDHIST, TAO
)
```

### WisdomCollection
```kotlin
object WisdomCollection {
    fun getQuoteForDay(dayNumber: Int): WisdomQuote
    val quotes: List<WisdomQuote>
}
```

## Expanding the Collection

To add more quotes, edit `app/src/main/java/com/aravind/parva/data/wisdom/WisdomQuotes.kt`:

1. Add quotes to the appropriate list:
   - `zenKoans` - Zen koans and proverbs
   - `stoicQuotes` - Marcus Aurelius, Seneca, Epictetus
   - `buddhistWisdom` - Buddha and Buddhist teachers
   - `taoQuotes` - Lao Tzu, Chuang Tzu, Tao Te Ching

2. Follow the format:
```kotlin
WisdomQuote(
    "Quote text here", 
    "Author Name", 
    WisdomType.ZEN_KOAN // or STOIC, BUDDHIST, TAO
)
```

3. The collection automatically combines all lists

## Design Rationale

### Why Deterministic?
- **Consistency**: Users see the same quote if they revisit a day
- **Meaningful**: Quotes become associated with specific milestones
- **Predictable**: No randomness in the journey

### Why Multiple Traditions?
- **Diversity**: Different wisdom traditions offer varied perspectives
- **Universal Themes**: All traditions emphasize mindfulness, discipline, growth
- **Rich Content**: 300+ quotes ensure variety for 343 days

### Why Modal Dialog Instead of Inline Display?
- **Clean UI**: Doesn't clutter the main screen with long text
- **Intentional**: User must click to see the quote (mindful engagement)
- **Flexible**: Works well with both short and long quotes
- **Focus**: Modal gives the quote full attention when viewed
- **Accessibility**: Info icon is recognizable and easy to tap

### Integration with App Themes
- Quotes complement the 7-phase cycle (Beginning, Practice, etc.)
- Author attribution uses theme colors for visual cohesion
- Info icon colored with theme colors for consistency
- Modal design doesn't distract from daily goals and planning

## Example Quotes by Type

**Zen Koan**
> "The obstacle is the path." — Zen Proverb

**Stoic**
> "You have power over your mind, not outside events." — Marcus Aurelius

**Buddhist**
> "Peace comes from within. Do not seek it without." — Buddha

**Tao**
> "Nature does not hurry, yet everything is accomplished." — Lao Tzu

## Future Enhancements (Optional)

1. **Expand to 1500+ quotes** for even more variety
2. **User-added quotes** - let users contribute their own wisdom
3. **Favorite quotes** - bookmark meaningful ones
4. **Share functionality** - share quotes on social media
5. **Quote of the day notification** - morning reminder with the day's quote
6. **Filter by tradition** - user preference for quote types
7. **Multi-language support** - Sanskrit, Latin, original languages

## Technical Notes

- Uses lazy initialization for efficient memory usage
- All quotes stored in-memory (no database needed)
- Lightweight implementation (~50KB for 300 quotes)
- No network calls required
- Works offline
- Deterministic formula ensures O(1) quote lookup

## Testing

To test the feature:
1. Create a new Maha-Parva starting today
2. Navigate to Maha-Parva detail screen → see quote for Day 1
3. Navigate to a Dina detail screen → see the same quote
4. Advance days (if hold periods) → see different quotes

---

**Created**: November 29, 2024
**Status**: Complete ✅
**Quote Count**: 300+
**Expandable To**: 1500+

