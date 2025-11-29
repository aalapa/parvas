package com.aravind.parva.data.model

/**
 * Different visual styles for mandala representation
 * Each Maha-Parva uses one style consistently across all levels
 */
enum class MandalaStyle(val displayName: String, val description: String) {
    CIRCULAR_PETAL(
        "Circular Petal",
        "Classic smooth circular sections - flowing and organic"
    ),
    SEPTAGON(
        "Septagon",
        "Seven-sided geometric polygon - clean and structured"
    ),
    LOTUS_FLOWER(
        "Lotus Flower",
        "Rounded petals like a lotus - spiritual and elegant"
    ),
    STAR_MANDALA(
        "Star Mandala",
        "Seven-pointed star radiating outward - dynamic and energetic"
    ),
    CONCENTRIC_RINGS(
        "Concentric Rings",
        "Seven concentric circular layers - depth and progression"
    );

    companion object {
        /**
         * Get style by Maha-Parva number (1-7)
         * Cycles through available styles
         */
        fun forMahaParva(mahaParvaNumber: Int): MandalaStyle {
            val index = (mahaParvaNumber - 1) % values().size
            return values()[index]
        }
    }
}

