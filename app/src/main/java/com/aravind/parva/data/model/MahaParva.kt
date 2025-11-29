package com.aravind.parva.data.model

import java.time.LocalDate
import java.util.UUID

/**
 * Represents a complete 343-day Maha-Parva cycle
 */
data class MahaParva(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val startDate: LocalDate,
    val parvas: List<Parva> = emptyList(),
    val accountabilityPartnerEmail: String = "",
    val createdAt: LocalDate = LocalDate.now()
) {
    /**
     * The end date of this Maha-Parva (343 days from start)
     */
    val endDate: LocalDate
        get() = startDate.plusDays(342)

    /**
     * Check if this Maha-Parva is currently active
     */
    val isActive: Boolean
        get() {
            val today = LocalDate.now()
            return !today.isBefore(startDate) && !today.isAfter(endDate)
        }

    /**
     * Check if this Maha-Parva is completed
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
                .coerceIn(1, 343)
        }

    /**
     * Get the current Parva if this Maha-Parva is active
     */
    val currentParva: Parva?
        get() = if (isActive) parvas.find { it.isActive } else null

    /**
     * Get the current Saptaha if this Maha-Parva is active
     */
    val currentSaptaha: Saptaha?
        get() = currentParva?.currentSaptaha

    /**
     * Get the current Dina if this Maha-Parva is active
     */
    val currentDina: Dina?
        get() {
            val dayNum = currentDayNumber ?: return null
            return allDinas.find { it.dayNumber == dayNum }
        }

    /**
     * Get all Dinas across all Parvas
     */
    val allDinas: List<Dina>
        get() = parvas.flatMap { it.allDinas }

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
     * Get all notes as a journal (for export)
     */
    fun getJournal(): String {
        return allDinas
            .filter { it.notes.isNotEmpty() }
            .joinToString("\n\n") { dina ->
                "Day ${dina.dayNumber} - ${dina.date} - ${dina.notes}"
            }
    }

    companion object {
        /**
         * Create a new Maha-Parva with all 7 Parvas (343 days) initialized
         */
        fun create(
            title: String,
            description: String = "",
            startDate: LocalDate = LocalDate.now(),
            accountabilityPartnerEmail: String = ""
        ): MahaParva {
            val parvas = (1..7).map { parvaNumber ->
                val parvaStartDate = startDate.plusDays(((parvaNumber - 1) * 49).toLong())
                val parvaTheme = CycleTheme.fromIndex(parvaNumber - 1)
                val parvaDayOffset = (parvaNumber - 1) * 49 + 1
                
                Parva.create(
                    number = parvaNumber,
                    theme = parvaTheme,
                    startDate = parvaStartDate,
                    absoluteDayOffset = parvaDayOffset
                )
            }
            return MahaParva(
                title = title,
                description = description,
                startDate = startDate,
                parvas = parvas,
                accountabilityPartnerEmail = accountabilityPartnerEmail
            )
        }
    }
}

