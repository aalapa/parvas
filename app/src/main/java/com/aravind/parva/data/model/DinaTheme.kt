package com.aravind.parva.data.model

/**
 * Represents the 7 daily themes (Dina)
 * These repeat every 7 days throughout the entire cycle
 */
enum class DinaTheme(val displayName: String, val description: String) {
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
         * Get Dina theme by day number in the overall cycle (1-343)
         */
        fun fromDay(day: Int): DinaTheme {
            require(day in 1..343) { "Day must be between 1 and 343" }
            val dayInWeek = (day - 1) % 7
            return values()[dayInWeek]
        }
    }
}

