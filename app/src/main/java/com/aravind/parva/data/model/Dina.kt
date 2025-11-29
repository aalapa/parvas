package com.aravind.parva.data.model

import java.time.LocalDate

/**
 * Represents a single day (Dina) within the Maha-Parva cycle
 */
data class Dina(
    val dayNumber: Int, // 1-343 (absolute day in Maha-Parva)
    val date: LocalDate,
    val dinaTheme: DinaTheme,
    val dailyIntention: String = "", // What you plan to do today
    val notes: String = "", // What you actually did/learned
    val isCompleted: Boolean = false
) {
    init {
        require(dayNumber in 1..343) { "Day number must be between 1 and 343" }
    }

    /**
     * Get the Parva number (1-7) that this Dina belongs to
     */
    val parvaNumber: Int
        get() = ((dayNumber - 1) / 49) + 1

    /**
     * Get the Saptaha number (1-7) within the current Parva
     */
    val saptahaNumber: Int
        get() = (((dayNumber - 1) % 49) / 7) + 1

    /**
     * Get the day within the current Saptaha (1-7)
     */
    val dayInSaptaha: Int
        get() = ((dayNumber - 1) % 7) + 1

    /**
     * Check if this Dina is in the past (cannot be edited)
     */
    val isPast: Boolean
        get() = LocalDate.now().isAfter(date)

    /**
     * Check if this Dina is editable (today or future)
     */
    val isEditable: Boolean
        get() = !isPast

    companion object {
        /**
         * Create a Dina from a day number and start date
         */
        fun create(dayNumber: Int, startDate: LocalDate): Dina {
            return Dina(
                dayNumber = dayNumber,
                date = startDate.plusDays((dayNumber - 1).toLong()),
                dinaTheme = DinaTheme.fromDay(dayNumber)
            )
        }
    }
}

