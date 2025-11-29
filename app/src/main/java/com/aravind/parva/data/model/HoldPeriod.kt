package com.aravind.parva.data.model

import java.time.LocalDate

/**
 * Represents a period when the Maha-Parva was on hold (paused)
 * Used for sick days, travel, breaks, etc.
 */
data class HoldPeriod(
    val startDate: LocalDate,
    val days: Int,
    val reason: String = ""
) {
    /**
     * The end date of this hold period
     */
    val endDate: LocalDate
        get() = startDate.plusDays((days - 1).toLong())

    /**
     * Check if a given date falls within this hold period
     */
    fun contains(date: LocalDate): Boolean {
        return !date.isBefore(startDate) && !date.isAfter(endDate)
    }
}

