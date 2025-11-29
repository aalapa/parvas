package com.aravind.parva.data.model

import java.time.LocalDate
import java.util.UUID

/**
 * Represents a complete 49-day Parva cycle
 */
data class Parva(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val startDate: LocalDate,
    val days: List<ParvaDay> = emptyList(),
    val createdAt: LocalDate = LocalDate.now()
) {
    /**
     * The end date of this Parva (49 days from start)
     */
    val endDate: LocalDate
        get() = startDate.plusDays(48)

    /**
     * Check if this Parva is currently active
     */
    val isActive: Boolean
        get() {
            val today = LocalDate.now()
            return !today.isBefore(startDate) && !today.isAfter(endDate)
        }

    /**
     * Check if this Parva is completed
     */
    val isCompleted: Boolean
        get() = LocalDate.now().isAfter(endDate)

    /**
     * Get the current day number if active, null otherwise
     */
    val currentDayNumber: Int?
        get() {
            if (!isActive) return null
            val today = LocalDate.now()
            return ((today.toEpochDay() - startDate.toEpochDay()).toInt() + 1)
                .coerceIn(1, 49)
        }

    /**
     * Get completion progress (0.0 to 1.0)
     */
    val progress: Float
        get() {
            val completedDays = days.count { it.isCompleted }
            return completedDays.toFloat() / 49f
        }

    companion object {
        /**
         * Create a new Parva with all 49 days initialized
         */
        fun create(
            title: String,
            description: String = "",
            startDate: LocalDate = LocalDate.now()
        ): Parva {
            val days = (1..49).map { dayNumber ->
                ParvaDay.create(dayNumber, startDate)
            }
            return Parva(
                title = title,
                description = description,
                startDate = startDate,
                days = days
            )
        }
    }
}

