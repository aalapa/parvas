package com.aravind.parva.data.local.entities

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aravind.parva.data.model.HoldPeriod
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.data.model.MandalaStyle
import com.aravind.parva.data.model.Parva
import java.time.LocalDate

/**
 * Room entity for MahaParva
 * Stores the complete 343-day cycle
 * 
 * Note: Colors are stored as Int (ARGB) to avoid Room value class issues
 */
@Entity(tableName = "maha_parvas")
data class MahaParvaEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val parvas: List<Parva>, // Will be converted to JSON
    val accountabilityPartnerEmail: String,
    val mandalaStyle: MandalaStyle,
    val customStartColorArgb: Int?, // Stored as Int instead of Color
    val customEndColorArgb: Int?,   // Stored as Int instead of Color
    val holdPeriods: List<HoldPeriod> = emptyList(), // Hold periods
    val createdAt: LocalDate
) {
    /**
     * Convert to domain model
     */
    fun toMahaParva(): MahaParva {
        return MahaParva(
            id = id,
            title = title,
            description = description,
            startDate = startDate,
            parvas = parvas,
            accountabilityPartnerEmail = accountabilityPartnerEmail,
            mandalaStyle = mandalaStyle,
            customStartColor = customStartColorArgb?.let { Color(it) }, // Convert Int to Color
            customEndColor = customEndColorArgb?.let { Color(it) },     // Convert Int to Color
            holdPeriods = holdPeriods,
            createdAt = createdAt
        )
    }

    companion object {
        /**
         * Convert from domain model
         */
        fun fromMahaParva(mahaParva: MahaParva): MahaParvaEntity {
            return MahaParvaEntity(
                id = mahaParva.id,
                title = mahaParva.title,
                description = mahaParva.description,
                startDate = mahaParva.startDate,
                parvas = mahaParva.parvas,
                accountabilityPartnerEmail = mahaParva.accountabilityPartnerEmail,
                mandalaStyle = mahaParva.mandalaStyle,
                customStartColorArgb = mahaParva.customStartColor?.toArgb(), // Convert Color to Int
                customEndColorArgb = mahaParva.customEndColor?.toArgb(),     // Convert Color to Int
                holdPeriods = mahaParva.holdPeriods,
                createdAt = mahaParva.createdAt
            )
        }
    }
}

