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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.CycleTheme
import kotlin.math.*

/**
 * A circular mandala view with 7 sections
 * Each section represents a Parva or Saptaha depending on context
 */
@Composable
fun MandalaView(
    sections: List<MandalaSection>,
    currentSectionIndex: Int? = null,
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
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val dx = offset.x - center.x
                        val dy = offset.y - center.y
                        
                        // Calculate angle from center
                        var angle = atan2(dy, dx) * 180 / PI.toFloat()
                        if (angle < 0) angle += 360
                        
                        // Adjust to start from top (rotate by 90 degrees)
                        angle = (angle + 90) % 360
                        
                        // Determine which section was tapped
                        val sectionIndex = (angle / (360f / 7f)).toInt() % 7
                        onSectionClick(sectionIndex)
                    }
                }
        ) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val radius = min(centerX, centerY) * 0.85f
            val anglePerSection = 360f / 7f
            
            // Draw each section
            sections.forEachIndexed { index, section ->
                val startAngle = index * anglePerSection - 90f // Start from top
                
                // Draw filled arc for the section
                val arcColor = if (index == currentSectionIndex) {
                    section.color.copy(alpha = 1f)
                } else {
                    section.color.copy(alpha = 0.6f)
                }
                
                drawArc(
                    color = arcColor,
                    startAngle = startAngle,
                    sweepAngle = anglePerSection,
                    useCenter = true,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2)
                )
                
                // Draw section border
                drawArc(
                    color = Color.White,
                    startAngle = startAngle,
                    sweepAngle = anglePerSection,
                    useCenter = true,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 3f)
                )
                
                // Draw text label
                val labelAngle = (startAngle + anglePerSection / 2f) * PI.toFloat() / 180f
                val labelRadius = radius * 0.65f
                val labelX = centerX + labelRadius * cos(labelAngle)
                val labelY = centerY + labelRadius * sin(labelAngle)
                
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 40f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                        setShadowLayer(8f, 0f, 0f, android.graphics.Color.BLACK)
                    }
                    drawText(
                        section.label,
                        labelX,
                        labelY + 15f, // Adjust for vertical centering
                        paint
                    )
                }
            }
            
            // Draw center circle
            drawCircle(
                color = Color.White,
                radius = radius * 0.25f,
                center = Offset(centerX, centerY)
            )
            
            // Draw active indicator in center if applicable
            if (currentSectionIndex != null) {
                drawCircle(
                    color = sections[currentSectionIndex].color,
                    radius = radius * 0.2f,
                    center = Offset(centerX, centerY)
                )
            }
        }
        
        // Center text
        if (currentSectionIndex != null) {
            Text(
                text = sections[currentSectionIndex].centerText,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
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

