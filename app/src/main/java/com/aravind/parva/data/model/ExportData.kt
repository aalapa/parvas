package com.aravind.parva.data.model

import java.time.LocalDate

/**
 * Data class for exporting and importing all app data
 */
data class ExportData(
    val exportDate: LocalDate,
    val version: String,
    val mahaParvas: List<MahaParva>
)

