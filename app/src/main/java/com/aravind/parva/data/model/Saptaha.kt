package com.aravind.parva.data.model

import java.time.LocalDate

/**
 * Represents a 7-day period (Saptaha) within a Parva
 */
data class Saptaha(
    val number: Int, // 1-7 within its Parva
    val theme: CycleTheme,
    val startDate: LocalDate,
    val dinas: List<Dina> = emptyList(),
    val customGoal: String = "", // User's custom goal for this 7-day week
    val customColor: androidx.compose.ui.graphics.Color? = null // Override theme color
) {
    init {
        require(number in 1..7) { "Saptaha number must be between 1 and 7" }
    }

    /**
     * The end date of this Saptaha
     * If Dinas exist, use the last Dina's date (accounts for holds)
     * Otherwise, calculate as 7 days from start
     */
    val endDate: LocalDate
        get() = dinas.lastOrNull()?.date ?: startDate.plusDays(6)

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

    /**
     * Check if this Saptaha is in the past (cannot be edited)
     */
    val isPast: Boolean
        get() = LocalDate.now().isAfter(endDate)

    /**
     * Check if this Saptaha is editable (current or future)
     */
    val isEditable: Boolean
        get() = !isPast

    /**
     * Get the effective color (custom if set, otherwise theme default)
     */
    val color: androidx.compose.ui.graphics.Color
        get() = customColor ?: theme.color

    companion object {
        /**
         * Create a Saptaha with all 7 Dinas initialized
         */
        fun create(
            number: Int,
            theme: CycleTheme,
            startDate: LocalDate,
            absoluteDayOffset: Int, // Starting day number in Maha-Parva
            customColor: androidx.compose.ui.graphics.Color? = null
        ): Saptaha {
            val dinas = (0..6).map { dayOffset ->
                // Create Dina directly without using Dina.create() to avoid double date calculation
                Dina(
                    dayNumber = absoluteDayOffset + dayOffset,
                    date = startDate.plusDays(dayOffset.toLong()), // Simple: startDate + dayOffset
                    dinaTheme = DinaTheme.fromDay(absoluteDayOffset + dayOffset),
                    dailyIntention = "",
                    notes = "",
                    isCompleted = false
                )
            }
            return Saptaha(
                number = number,
                theme = theme,
                startDate = startDate,
                dinas = dinas,
                customColor = customColor
            )
        }

        /**
         * Create a Saptaha with adjusted dates accounting for hold periods
         * Preserves user data from old Dinas
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
            oldDinas: List<Dina>?
        ): Saptaha {
            // Generate 7 Dinas with dates adjusted for holds
            val dinas = (0..6).map { dayOffset ->
                val oldDina = oldDinas?.getOrNull(dayOffset)
                
                // CRITICAL: Use BASE date (without holds) for calculating adjustments
                // This ensures holds are applied consistently without double-counting
                val baseDinaDate = baseStartDate.plusDays(dayOffset.toLong())
                
                // Calculate adjusted date accounting for ALL holds before this base date
                val adjustedDinaDate = com.aravind.parva.utils.DateUtils.calculateAdjustedDate(
                    baseDinaDate,
                    holdPeriods
                )
                
                Dina(
                    dayNumber = absoluteDayOffset + dayOffset,
                    date = adjustedDinaDate,
                    dinaTheme = DinaTheme.fromDay(absoluteDayOffset + dayOffset),
                    dailyIntention = oldDina?.dailyIntention ?: "",
                    notes = oldDina?.notes ?: "",
                    isCompleted = oldDina?.isCompleted ?: false
                )
            }
            
            return Saptaha(
                number = number,
                theme = theme,
                startDate = adjustedStartDate,
                dinas = dinas,
                customGoal = existingGoal ?: "",
                customColor = customColor
            )
        }
    }
}

