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
    val customGoal: String = "", // User's custom goal for this 49-day period
    val customColor: androidx.compose.ui.graphics.Color? = null // Override theme color
) {
    init {
        require(number in 1..7) { "Parva number must be between 1 and 7" }
    }

    /**
     * The end date of this Parva
     * If Dinas exist, use the last Dina's date (accounts for holds)
     * Otherwise, calculate as 49 days from start
     */
    val endDate: LocalDate
        get() = allDinas.lastOrNull()?.date ?: startDate.plusDays(48)

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
     * Get the effective color (custom if set, otherwise theme default)
     */
    val color: androidx.compose.ui.graphics.Color
        get() = customColor ?: theme.color

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
            absoluteDayOffset: Int, // Starting day number in Maha-Parva
            customColor: androidx.compose.ui.graphics.Color? = null
        ): Parva {
            val saptahas = (1..7).map { saptahaNumber ->
                val saptahaStartDate = startDate.plusDays(((saptahaNumber - 1) * 7).toLong())
                val saptahaTheme = CycleTheme.fromIndex(saptahaNumber - 1)
                val saptahaDayOffset = absoluteDayOffset + (saptahaNumber - 1) * 7
                
                Saptaha.create(
                    number = saptahaNumber,
                    theme = saptahaTheme,
                    startDate = saptahaStartDate,
                    absoluteDayOffset = saptahaDayOffset,
                    customColor = customColor // Saptahas inherit Parva's gradient color
                )
            }
            return Parva(
                number = number,
                theme = theme,
                startDate = startDate,
                saptahas = saptahas,
                customColor = customColor
            )
        }

        /**
         * Create a Parva with adjusted dates accounting for hold periods
         * Preserves user data from old Saptahas
         */
        fun createWithHolds(
            number: Int,
            theme: CycleTheme,
            baseStartDate: LocalDate,
            adjustedStartDate: LocalDate,
            absoluteDayOffset: Int,
            customColor: androidx.compose.ui.graphics.Color?,
            holdPeriods: List<HoldPeriod>,
            existingGoal: String?,
            oldSaptahas: List<Saptaha>?
        ): Parva {
            val saptahas = (1..7).map { saptahaNumber ->
                val oldSaptaha = oldSaptahas?.getOrNull(saptahaNumber - 1)
                
                // Base date without holds
                val baseSaptahaStart = baseStartDate.plusDays(((saptahaNumber - 1) * 7).toLong())
                
                // Adjusted date with holds
                val adjustedSaptahaStart = com.aravind.parva.utils.DateUtils.calculateAdjustedDate(
                    baseSaptahaStart,
                    holdPeriods
                )
                
                val saptahaTheme = CycleTheme.fromIndex(saptahaNumber - 1)
                val saptahaDayOffset = absoluteDayOffset + (saptahaNumber - 1) * 7
                
                Saptaha.createWithHolds(
                    number = saptahaNumber,
                    theme = saptahaTheme,
                    baseStartDate = baseSaptahaStart,
                    adjustedStartDate = adjustedSaptahaStart,
                    absoluteDayOffset = saptahaDayOffset,
                    customColor = customColor,
                    holdPeriods = holdPeriods,
                    existingGoal = oldSaptaha?.customGoal,
                    oldDinas = oldSaptaha?.dinas
                )
            }
            
            return Parva(
                number = number,
                theme = theme,
                startDate = adjustedStartDate,
                saptahas = saptahas,
                customGoal = existingGoal ?: "",
                customColor = customColor
            )
        }
    }
}

