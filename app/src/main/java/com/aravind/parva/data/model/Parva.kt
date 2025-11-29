package com.aravind.parva.data.model

import java.time.LocalDate

/**
 * Represents a 49-day period (Parva) within a Maha-Parva
 */
data class Parva(
    val number: Int, // 1-7 within its Maha-Parva
    val theme: CycleTheme,
    val startDate: LocalDate,
    val saptahas: List<Saptaha> = emptyList(),
    val customGoal: String = "" // User's custom goal for this 49-day period
) {
    init {
        require(number in 1..7) { "Parva number must be between 1 and 7" }
    }

    /**
     * The end date of this Parva (49 days from start)
     */
    val endDate: LocalDate
        get() = startDate.plusDays(48)

    /**
     * Get all Dinas across all Saptahas
     */
    val allDinas: List<Dina>
        get() = saptahas.flatMap { it.dinas }

    /**
     * Get completion progress (0.0 to 1.0)
     */
    val progress: Float
        get() {
            val allDays = allDinas
            if (allDays.isEmpty()) return 0f
            val completedDays = allDays.count { it.isCompleted }
            return completedDays.toFloat() / allDays.size.toFloat()
        }

    /**
     * Check if this Parva contains today's date
     */
    val isActive: Boolean
        get() {
            val today = LocalDate.now()
            return !today.isBefore(startDate) && !today.isAfter(endDate)
        }

    /**
     * Check if this Parva is in the past (cannot be edited)
     */
    val isPast: Boolean
        get() = LocalDate.now().isAfter(endDate)

    /**
     * Check if this Parva is editable (current or future)
     */
    val isEditable: Boolean
        get() = !isPast

    /**
     * Get the current Saptaha if this Parva is active
     */
    val currentSaptaha: Saptaha?
        get() = if (isActive) saptahas.find { it.isActive } else null

    companion object {
        /**
         * Create a Parva with all 7 Saptahas initialized
         */
        fun create(
            number: Int,
            theme: CycleTheme,
            startDate: LocalDate,
            absoluteDayOffset: Int // Starting day number in Maha-Parva
        ): Parva {
            val saptahas = (1..7).map { saptahaNumber ->
                val saptahaStartDate = startDate.plusDays(((saptahaNumber - 1) * 7).toLong())
                val saptahaTheme = CycleTheme.fromIndex(saptahaNumber - 1)
                val saptahaDayOffset = absoluteDayOffset + (saptahaNumber - 1) * 7
                
                Saptaha.create(
                    number = saptahaNumber,
                    theme = saptahaTheme,
                    startDate = saptahaStartDate,
                    absoluteDayOffset = saptahaDayOffset
                )
            }
            return Parva(
                number = number,
                theme = theme,
                startDate = startDate,
                saptahas = saptahas
            )
        }
    }
}

