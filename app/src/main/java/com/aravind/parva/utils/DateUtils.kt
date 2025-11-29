package com.aravind.parva.utils

import com.aravind.parva.data.model.HoldPeriod
import java.time.LocalDate

object DateUtils {
    /**
     * Calculate adjusted calendar date for a journey day accounting for hold periods
     * 
     * The base date is the Nth calendar day from Maha-Parva start (ignoring holds).
     * The adjusted date adds hold days that occurred from start to that base date.
     * 
     * Example:
     *   Start: Dec 1
     *   Base Day 25: Dec 1 + 24 = Dec 25
     *   Hold: Dec 25-30 (6 days)
     *   Since hold starts on/before Dec 25, we skip it entirely
     *   Adjusted: Dec 25 + 6 = Dec 31
     * 
     * @param baseDate The original calculated date (without holds)
     * @param holdPeriods All hold periods
     * @return The adjusted date with hold days added
     */
    fun calculateAdjustedDate(baseDate: LocalDate, holdPeriods: List<HoldPeriod>): LocalDate {
        if (holdPeriods.isEmpty()) return baseDate
        
        // For each hold period that starts on or before the base date,
        // add all those hold days to shift the calendar forward
        val holdDaysToAdd = holdPeriods
            .filter { holdPeriod ->
                // Include holds that start on or before the base date
                !holdPeriod.startDate.isAfter(baseDate)
            }
            .sumOf { it.days }
        
        return baseDate.plusDays(holdDaysToAdd.toLong())
    }
}

