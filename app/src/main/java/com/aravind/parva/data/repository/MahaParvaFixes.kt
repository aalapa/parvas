package com.aravind.parva.data.repository

import com.aravind.parva.data.model.MahaParva

/**
 * Utility functions to fix data inconsistencies
 */
object MahaParvaFixes {
    
    /**
     * Force regenerate all dates for a Maha-Parva
     * This will fix any date calculation bugs from previous versions
     * Preserves all user data (goals, notes, completion status)
     */
    fun forceRegenerateDates(mahaParva: MahaParva): MahaParva {
        // Regenerate with current hold periods
        // This will apply the FIXED date calculation logic
        return mahaParva.regenerateWithHolds(mahaParva.holdPeriods)
    }
    
    /**
     * Check if a Maha-Parva has corrupted dates
     * Returns true if dates look wrong
     */
    fun hasCorruptedDates(mahaParva: MahaParva): Boolean {
        // Check if any Saptaha is not exactly 7 days
        return mahaParva.parvas.any { parva ->
            parva.saptahas.any { saptaha ->
                val daysDuration = saptaha.dinas.size
                daysDuration != 7
            }
        }
    }
}

