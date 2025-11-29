package com.aravind.parva.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.ParvaDay
import com.aravind.parva.ui.theme.*

@Composable
fun MiniParvaSection(
    miniParvaNumber: Int,
    days: List<ParvaDay>
) {
    val miniParvaTheme = days.firstOrNull()?.miniParvaTheme

    // Color for each Mini-Parva
    val themeColor = when (miniParvaNumber) {
        1 -> ParvaBlue
        2 -> ParvaPurple
        3 -> ParvaGreen
        4 -> ParvaOrange
        5 -> ParvaRed
        6 -> ParvaTeal
        7 -> ParvaIndigo
        else -> ParvaBlue
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Mini-Parva $miniParvaNumber",
                        style = MaterialTheme.typography.titleMedium,
                        color = themeColor
                    )
                    if (miniParvaTheme != null) {
                        Text(
                            text = miniParvaTheme.displayName,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = miniParvaTheme.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Divider(color = themeColor, thickness = 2.dp)

            // 7 days in this Mini-Parva
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                days.forEach { day ->
                    DayItem(
                        day = day,
                        accentColor = themeColor
                    )
                }
            }
        }
    }
}

@Composable
private fun DayItem(
    day: ParvaDay,
    accentColor: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (day.isCompleted)
                    accentColor.copy(alpha = 0.1f)
                else
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Day ${day.dayNumber}",
                style = MaterialTheme.typography.labelLarge,
                color = accentColor
            )
            Text(
                text = day.macroParvaTheme.displayName,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = day.macroParvaTheme.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        if (day.isCompleted) {
            Text(
                text = "✓",
                style = MaterialTheme.typography.titleLarge,
                color = accentColor
            )
        }
    }
}

