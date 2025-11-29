package com.aravind.parva.data.model

import java.time.LocalDate

/**
 * Represents a single day (Macro-Parva) within the 49-day Parva cycle
 */
data class ParvaDay(
    val dayNumber: Int, // 1-49
    val date: LocalDate,
    val miniParvaTheme: MiniParvaTheme,
    val macroParvaTheme: MacroParvaTheme,
    val isCompleted: Boolean = false,
    val notes: String = ""
) {
    init {
        require(dayNumber in 1..49) { "Day number must be between 1 and 49" }
    }

    /**
     * Get the Mini-Parva number (1-7) that this day belongs to
     */
    val miniParvaNumber: Int
        get() = ((dayNumber - 1) / 7) + 1

    /**
     * Get the day within the current Mini-Parva (1-7)
     */
    val dayInMiniParva: Int
        get() = ((dayNumber - 1) % 7) + 1

    companion object {
        /**
         * Create a ParvaDay from a day number and start date
         */
        fun create(dayNumber: Int, startDate: LocalDate): ParvaDay {
            return ParvaDay(
                dayNumber = dayNumber,
                date = startDate.plusDays((dayNumber - 1).toLong()),
                miniParvaTheme = MiniParvaTheme.fromDay(dayNumber),
                macroParvaTheme = MacroParvaTheme.fromDay(dayNumber)
            )
        }
    }
}

