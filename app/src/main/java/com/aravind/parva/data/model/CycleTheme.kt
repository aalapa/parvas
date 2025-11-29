package com.aravind.parva.data.model

import androidx.compose.ui.graphics.Color

/**
 * Represents the 7 themes that repeat at every hierarchical level
 * Used for Parva (49 days) and Saptaha (7 days)
 */
enum class CycleTheme(
    val displayName: String,
    val description: String,
    val color: Color
) {
    BEGINNING(
        "Beginning",
        "The foundation is laid. New habits and intentions take root.",
        Color(0xFF8B00FF) // Violet
    ),
    PRACTICE(
        "Practice",
        "Repetition builds strength. Consistency becomes easier.",
        Color(0xFF4B0082) // Indigo
    ),
    DISCERNMENT(
        "Discernment",
        "Clarity emerges. You see what works and what doesn't.",
        Color(0xFF0000FF) // Blue
    ),
    ASCENT(
        "Ascent",
        "Growth accelerates. Momentum carries you forward.",
        Color(0xFF00FF00) // Green
    ),
    MASTERY(
        "Mastery",
        "Skills solidify. Actions feel natural and effortless.",
        Color(0xFFFFFF00) // Yellow
    ),
    FLOW(
        "Flow",
        "You are in rhythm. The practice becomes part of who you are.",
        Color(0xFFFF7F00) // Orange
    ),
    RENEWAL(
        "Renewal",
        "Integration and reflection. Preparing for the next cycle.",
        Color(0xFFFF0000) // Red
    );

    companion object {
        /**
         * Get theme by position (0-6)
         */
        fun fromIndex(index: Int): CycleTheme {
            require(index in 0..6) { "Index must be between 0 and 6" }
            return values()[index]
        }

        /**
         * Get theme for a specific day within a cycle (1-based)
         * For 49-day cycle: day 1-7 = Beginning, 8-14 = Practice, etc.
         * For 7-day cycle: day 1 = Beginning, day 2 = Practice, etc.
         */
        fun fromDayInCycle(day: Int, cycleLength: Int): CycleTheme {
            require(day in 1..cycleLength) { "Day must be between 1 and $cycleLength" }
            val index = (day - 1) / (cycleLength / 7)
            return fromIndex(index)
        }
    }
}

