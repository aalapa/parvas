package com.aravind.parva.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * Utility functions for color manipulation
 */
object ColorUtils {
    
    /**
     * Generate 7 colors by interpolating between start and end colors
     * Uses linear interpolation (lerp) in RGB color space
     */
    fun generateGradient(startColor: Color, endColor: Color): List<Color> {
        return (0..6).map { index ->
            val fraction = index / 6f // 0.0 to 1.0
            lerp(startColor, endColor, fraction)
        }
    }

    /**
     * Default VIBGYOR colors
     */
    val VIBGYOR = listOf(
        Color(0xFF8B00FF), // Violet
        Color(0xFF4B0082), // Indigo
        Color(0xFF0000FF), // Blue
        Color(0xFF00FF00), // Green
        Color(0xFFFFFF00), // Yellow
        Color(0xFFFF7F00), // Orange
        Color(0xFFFF0000)  // Red
    )

    /**
     * Get colors for a Maha-Parva
     * Returns custom gradient if colors are set, otherwise VIBGYOR
     */
    fun getColorsForMahaParva(
        startColor: Color?,
        endColor: Color?
    ): List<Color> {
        return if (startColor != null && endColor != null) {
            generateGradient(startColor, endColor)
        } else {
            VIBGYOR
        }
    }
}

