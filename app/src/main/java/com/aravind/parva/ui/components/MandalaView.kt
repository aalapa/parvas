package com.aravind.parva.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.CycleTheme
import com.aravind.parva.data.model.MandalaStyle
import kotlin.math.*

/**
 * A mandala view with multiple visual styles
 * Each section represents a Parva or Saptaha depending on context
 */
@Composable
fun MandalaView(
    sections: List<MandalaSection>,
    currentSectionIndex: Int? = null,
    style: MandalaStyle = MandalaStyle.CIRCULAR_PETAL,
    onSectionClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    require(sections.size == 7) { "Mandala must have exactly 7 sections" }

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val sectionIndex = detectSection(
                            offset,
                            Offset(size.width / 2f, size.height / 2f),
                            style
                        )
                        onSectionClick(sectionIndex)
                    }
                }
        ) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val radius = min(centerX, centerY) * 0.85f

            when (style) {
                MandalaStyle.CIRCULAR_PETAL -> drawCircularPetal(
                    sections, currentSectionIndex, centerX, centerY, radius
                )
                MandalaStyle.SEPTAGON -> drawSeptagon(
                    sections, currentSectionIndex, centerX, centerY, radius
                )
                MandalaStyle.LOTUS_FLOWER -> drawLotusFlower(
                    sections, currentSectionIndex, centerX, centerY, radius
                )
                MandalaStyle.STAR_MANDALA -> drawStarMandala(
                    sections, currentSectionIndex, centerX, centerY, radius
                )
                MandalaStyle.CONCENTRIC_RINGS -> drawConcentricRings(
                    sections, currentSectionIndex, centerX, centerY, radius
                )
            }
        }
        
        // Center text
        if (currentSectionIndex != null && style != MandalaStyle.CONCENTRIC_RINGS) {
            Text(
                text = sections[currentSectionIndex].centerText,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}

/**
 * Detect which section was tapped based on style
 */
private fun detectSection(
    tapOffset: Offset,
    center: Offset,
    style: MandalaStyle
): Int {
    val dx = tapOffset.x - center.x
    val dy = tapOffset.y - center.y
    
    return when (style) {
        MandalaStyle.CONCENTRIC_RINGS -> {
            // For rings, calculate distance from center
            val distance = sqrt(dx * dx + dy * dy)
            // Assume equal ring widths
            min((distance / (center.x * 0.85f / 7f)).toInt(), 6)
        }
        else -> {
            // For all other styles, use angle-based detection
            var angle = atan2(dy, dx) * 180 / PI.toFloat()
            if (angle < 0) angle += 360
            angle = (angle + 90) % 360
            (angle / (360f / 7f)).toInt() % 7
        }
    }
}

/**
 * Style 1: Circular Petal (Original)
 */
private fun DrawScope.drawCircularPetal(
    sections: List<MandalaSection>,
    currentIndex: Int?,
    centerX: Float,
    centerY: Float,
    radius: Float
) {
    val anglePerSection = 360f / 7f
    
    sections.forEachIndexed { index, section ->
        val startAngle = index * anglePerSection - 90f
        val arcColor = if (index == currentIndex) {
            section.color.copy(alpha = 1f)
        } else {
            section.color.copy(alpha = 0.6f)
        }
        
        // Draw filled arc
        drawArc(
            color = arcColor,
            startAngle = startAngle,
            sweepAngle = anglePerSection,
            useCenter = true,
            topLeft = Offset(centerX - radius, centerY - radius),
            size = Size(radius * 2, radius * 2)
        )
        
        // Draw border
        drawArc(
            color = Color.White,
            startAngle = startAngle,
            sweepAngle = anglePerSection,
            useCenter = true,
            topLeft = Offset(centerX - radius, centerY - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = 3f)
        )
        
        // Draw text
        drawRotatedText(section.label, index, anglePerSection, centerX, centerY, radius, startAngle)
    }
    
    // Center circle
    drawCircle(Color.White, radius * 0.25f, Offset(centerX, centerY))
    if (currentIndex != null) {
        drawCircle(sections[currentIndex].color, radius * 0.2f, Offset(centerX, centerY))
    }
}

/**
 * Style 2: Septagon (7-sided polygon)
 */
private fun DrawScope.drawSeptagon(
    sections: List<MandalaSection>,
    currentIndex: Int?,
    centerX: Float,
    centerY: Float,
    radius: Float
) {
    val anglePerSection = 360f / 7f
    
    sections.forEachIndexed { index, section ->
        val startAngle = (index * anglePerSection - 90f) * PI.toFloat() / 180f
        val endAngle = ((index + 1) * anglePerSection - 90f) * PI.toFloat() / 180f
        
        val arcColor = if (index == currentIndex) {
            section.color.copy(alpha = 1f)
        } else {
            section.color.copy(alpha = 0.6f)
        }
        
        // Create triangular section
        val path = Path().apply {
            moveTo(centerX, centerY)
            lineTo(
                centerX + radius * cos(startAngle),
                centerY + radius * sin(startAngle)
            )
            lineTo(
                centerX + radius * cos(endAngle),
                centerY + radius * sin(endAngle)
            )
            close()
        }
        
        drawPath(path, arcColor)
        drawPath(path, Color.White, style = Stroke(width = 3f))
        
        // Draw text
        val midAngle = (startAngle + endAngle) / 2f
        drawRotatedText(
            section.label, index, anglePerSection, 
            centerX, centerY, radius, 
            midAngle * 180f / PI.toFloat() + 90f
        )
    }
    
    // Center circle
    drawCircle(Color.White, radius * 0.25f, Offset(centerX, centerY))
    if (currentIndex != null) {
        drawCircle(sections[currentIndex].color, radius * 0.2f, Offset(centerX, centerY))
    }
}

/**
 * Style 3: Lotus Flower (rounded petals)
 */
private fun DrawScope.drawLotusFlower(
    sections: List<MandalaSection>,
    currentIndex: Int?,
    centerX: Float,
    centerY: Float,
    radius: Float
) {
    val anglePerSection = 360f / 7f
    
    sections.forEachIndexed { index, section ->
        val midAngle = (index * anglePerSection - 90f) * PI.toFloat() / 180f
        val petalWidth = anglePerSection * 0.8f
        
        val arcColor = if (index == currentIndex) {
            section.color.copy(alpha = 1f)
        } else {
            section.color.copy(alpha = 0.6f)
        }
        
        // Draw petal shape (oval)
        val path = Path().apply {
            val cx = centerX + radius * 0.5f * cos(midAngle)
            val cy = centerY + radius * 0.5f * sin(midAngle)
            addOval(
                androidx.compose.ui.geometry.Rect(
                    cx - radius * 0.3f,
                    cy - radius * 0.5f,
                    cx + radius * 0.3f,
                    cy + radius * 0.5f
                )
            )
        }
        
        // Rotate the oval to point outward
        drawContext.canvas.save()
        drawContext.canvas.nativeCanvas.rotate(
            midAngle * 180f / PI.toFloat(),
            centerX + radius * 0.5f * cos(midAngle),
            centerY + radius * 0.5f * sin(midAngle)
        )
        drawPath(path, arcColor)
        drawPath(path, Color.White, style = Stroke(width = 2f))
        drawContext.canvas.restore()
        
        // Draw text
        drawRotatedText(
            section.label, index, anglePerSection,
            centerX, centerY, radius * 0.7f,
            midAngle * 180f / PI.toFloat() + 90f
        )
    }
    
    // Center circle
    drawCircle(Color.White, radius * 0.3f, Offset(centerX, centerY))
    if (currentIndex != null) {
        drawCircle(sections[currentIndex].color, radius * 0.25f, Offset(centerX, centerY))
    }
}

/**
 * Style 4: Star Mandala (7-pointed star)
 */
private fun DrawScope.drawStarMandala(
    sections: List<MandalaSection>,
    currentIndex: Int?,
    centerX: Float,
    centerY: Float,
    radius: Float
) {
    val anglePerSection = 360f / 7f
    val innerRadius = radius * 0.4f
    
    sections.forEachIndexed { index, section ->
        val outerAngle = (index * anglePerSection - 90f) * PI.toFloat() / 180f
        val nextOuterAngle = ((index + 1) * anglePerSection - 90f) * PI.toFloat() / 180f
        val innerAngle = (index * anglePerSection + anglePerSection / 2f - 90f) * PI.toFloat() / 180f
        
        val arcColor = if (index == currentIndex) {
            section.color.copy(alpha = 1f)
        } else {
            section.color.copy(alpha = 0.6f)
        }
        
        // Create star point
        val path = Path().apply {
            moveTo(centerX, centerY)
            lineTo(centerX + radius * cos(outerAngle), centerY + radius * sin(outerAngle))
            lineTo(centerX + innerRadius * cos(innerAngle), centerY + innerRadius * sin(innerAngle))
            lineTo(centerX + radius * cos(nextOuterAngle), centerY + radius * sin(nextOuterAngle))
            close()
        }
        
        drawPath(path, arcColor)
        drawPath(path, Color.White, style = Stroke(width = 3f))
        
        // Draw text on outer points
        val textAngle = outerAngle
        drawRotatedText(
            section.label, index, anglePerSection,
            centerX, centerY, radius * 0.85f,
            textAngle * 180f / PI.toFloat() + 90f
        )
    }
    
    // Center circle
    drawCircle(Color.White, innerRadius, Offset(centerX, centerY))
    if (currentIndex != null) {
        drawCircle(sections[currentIndex].color, innerRadius * 0.8f, Offset(centerX, centerY))
    }
}

/**
 * Style 5: Concentric Rings (7 layers)
 */
private fun DrawScope.drawConcentricRings(
    sections: List<MandalaSection>,
    currentIndex: Int?,
    centerX: Float,
    centerY: Float,
    radius: Float
) {
    val ringWidth = radius / 7f
    
    // Draw from outside in so inner rings appear on top
    for (i in 6 downTo 0) {
        val section = sections[i]
        val outerRadius = (i + 1) * ringWidth
        val innerRadius = i * ringWidth
        
        val ringColor = if (i == currentIndex) {
            section.color.copy(alpha = 1f)
        } else {
            section.color.copy(alpha = 0.6f)
        }
        
        // Draw ring
        drawCircle(
            color = ringColor,
            radius = outerRadius,
            center = Offset(centerX, centerY)
        )
        
        // Draw inner border
        if (i > 0) {
            drawCircle(
                color = Color.White,
                radius = innerRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2f)
            )
        }
        
        // Draw text
        val textRadius = (outerRadius + innerRadius) / 2f
        drawContext.canvas.nativeCanvas.apply {
            save()
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 32f
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
                setShadowLayer(8f, 0f, 0f, android.graphics.Color.BLACK)
                isAntiAlias = true
            }
            drawText(
                section.label,
                centerX,
                centerY + textRadius + 10f,
                paint
            )
            restore()
        }
    }
}

/**
 * Helper function to draw rotated text
 */
private fun DrawScope.drawRotatedText(
    text: String,
    index: Int,
    anglePerSection: Float,
    centerX: Float,
    centerY: Float,
    radius: Float,
    baseAngle: Float
) {
    val middleAngle = baseAngle + anglePerSection / 2f
    val labelRadius = radius * 0.70f
    val labelAngle = middleAngle * PI.toFloat() / 180f
    val labelX = centerX + labelRadius * cos(labelAngle)
    val labelY = centerY + labelRadius * sin(labelAngle)
    
    drawContext.canvas.nativeCanvas.apply {
        save()
        translate(labelX, labelY)
        rotate(middleAngle + 90f)
        
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 36f
            textAlign = android.graphics.Paint.Align.CENTER
            isFakeBoldText = true
            setShadowLayer(10f, 0f, 0f, android.graphics.Color.BLACK)
            isAntiAlias = true
        }
        
        drawText(text, 0f, 0f, paint)
        restore()
    }
}

/**
 * Data class representing a section in the mandala
 */
data class MandalaSection(
    val label: String,
    val color: Color,
    val centerText: String = "",
    val theme: CycleTheme
)

