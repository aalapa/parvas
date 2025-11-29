package com.aravind.parva.data.model

import androidx.compose.ui.graphics.Color

/**
 * Wrapper to associate a theme with a custom color
 */
data class ThemeWithColor(
    val theme: CycleTheme,
    val customColor: Color? = null
) {
    /**
     * Get the effective color (custom if set, otherwise theme default)
     */
    val color: Color
        get() = customColor ?: theme.color

    val displayName: String get() = theme.displayName
    val description: String get() = theme.description
    val goalPrompts: List<String> get() = theme.goalPrompts
}

