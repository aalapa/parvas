package com.aravind.parva.data.model

import com.aravind.parva.data.wisdom.WisdomQuotes

/**
 * Represents a piece of wisdom (zen koan, stoic quote, etc.)
 */
data class WisdomQuote(
    val text: String,
    val author: String,
    val type: WisdomType
)

enum class WisdomType {
    ZEN_KOAN,
    STOIC,
    BUDDHIST,
    TAO
}

/**
 * Collection of 300+ wisdom quotes for daily inspiration
 * Deterministic: Day N shows quote at index (N-1) % quotes.size
 * Easily expandable to 1500+ by adding more quotes to WisdomQuotes.kt
 */
object WisdomCollection {
    
    /**
     * Get wisdom quote for a specific day number (1-343)
     * Deterministic: same day always returns same quote
     */
    fun getQuoteForDay(dayNumber: Int): WisdomQuote {
        val index = (dayNumber - 1) % quotes.size
        return quotes[index]
    }
    
    // Use the curated collection from WisdomQuotes
    val quotes: List<WisdomQuote> = WisdomQuotes.allQuotes
}
