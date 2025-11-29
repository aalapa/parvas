package com.aravind.parva.data.model

/**
 * Represents the 7 Mini-Parva themes
 * Each theme lasts for 7 days (one Mini-Parva)
 */
enum class MiniParvaTheme(val displayName: String, val description: String) {
    BEGINNING(
        "Beginning",
        "The foundation is laid. New habits and intentions take root."
    ),
    PRACTICE(
        "Practice",
        "Repetition builds strength. Consistency becomes easier."
    ),
    DISCERNMENT(
        "Discernment",
        "Clarity emerges. You see what works and what doesn't."
    ),
    ASCENT(
        "Ascent",
        "Growth accelerates. Momentum carries you forward."
    ),
    MASTERY(
        "Mastery",
        "Skills solidify. Actions feel natural and effortless."
    ),
    FLOW(
        "Flow",
        "You are in rhythm. The practice becomes part of who you are."
    ),
    RENEWAL(
        "Renewal",
        "Integration and reflection. Preparing for the next cycle."
    );

    companion object {
        /**
         * Get Mini-Parva theme by day number (1-49)
         */
        fun fromDay(day: Int): MiniParvaTheme {
            require(day in 1..49) { "Day must be between 1 and 49" }
            val miniParvaIndex = (day - 1) / 7
            return values()[miniParvaIndex]
        }
    }
}

