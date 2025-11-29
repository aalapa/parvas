package com.aravind.parva.data.model

import java.time.LocalDate

/**
 * Represents a 7-day period (Saptaha) within a Parva
 */
data class Saptaha(
    val number: Int, // 1-7 within its Parva
    val theme: CycleTheme,
    val startDate: LocalDate,
    val dinas: List<Dina> = emptyList()
) {
    init {
        require(number in 1..7) { "Saptaha number must be between 1 and 7" }
    }

    /**
     * The end date of this Saptaha (7 days from start)
     */
    val endDate: LocalDate
        get() = startDate.plusDays(6)

    /**
     * Get completion progress (0.0 to 1.0)
     */
    val progress: Float
        get() {
            if (dinas.isEmpty()) return 0f
            val completedDays = dinas.count { it.isCompleted }
            return completedDays.toFloat() / dinas.size.toFloat()
        }

    /**
     * Check if this Saptaha contains today's date
     */
    val isActive: Boolean
        get() {
            val today = LocalDate.now()
            return !today.isBefore(startDate) && !today.isAfter(endDate)
        }

    companion object {
        /**
         * Create a Saptaha with all 7 Dinas initialized
         */
        fun create(
            number: Int,
            theme: CycleTheme,
            startDate: LocalDate,
            absoluteDayOffset: Int // Starting day number in Maha-Parva
        ): Saptaha {
            val dinas = (0..6).map { dayOffset ->
                Dina.create(
                    dayNumber = absoluteDayOffset + dayOffset,
                    startDate = startDate.plusDays(dayOffset.toLong())
                )
            }
            return Saptaha(
                number = number,
                theme = theme,
                startDate = startDate,
                dinas = dinas
            )
        }
    }
}

