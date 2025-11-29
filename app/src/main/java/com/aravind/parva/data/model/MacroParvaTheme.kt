package com.aravind.parva.data.model

/**
 * Represents the 7 daily micro-themes (Macro-Parva)
 * These repeat every 7 days throughout the 49-day cycle
 */
enum class MacroParvaTheme(val displayName: String, val description: String) {
    INITIATE(
        "Initiate",
        "Start fresh. Set intentions and take the first step."
    ),
    STABILIZE(
        "Stabilize",
        "Find your footing. Build consistency."
    ),
    OBSERVE(
        "Observe",
        "Pay attention. Notice patterns and insights."
    ),
    STRENGTHEN(
        "Strengthen",
        "Push through resistance. Build resilience."
    ),
    EXPAND(
        "Expand",
        "Go further. Explore new dimensions."
    ),
    INTEGRATE(
        "Integrate",
        "Bring it all together. Make connections."
    ),
    REFLECT(
        "Reflect",
        "Look back and learn. Prepare for the next week."
    );

    companion object {
        /**
         * Get Macro-Parva theme by day number (1-49)
         */
        fun fromDay(day: Int): MacroParvaTheme {
            require(day in 1..49) { "Day must be between 1 and 49" }
            val dayInWeek = (day - 1) % 7
            return values()[dayInWeek]
        }
    }
}

