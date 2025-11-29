package com.aravind.parva.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.MahaParva
import java.time.format.DateTimeFormatter

@Composable
fun MahaParvaCard(
    mahaParva: MahaParva,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = mahaParva.title,
                style = MaterialTheme.typography.titleLarge
            )
            
            if (mahaParva.description.isNotEmpty()) {
                Text(
                    text = mahaParva.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Show current position
            val currentDay = mahaParva.currentDayNumber
            val currentParva = mahaParva.currentParva
            val currentSaptaha = mahaParva.currentSaptaha
            
            if (currentDay != null && currentParva != null && currentSaptaha != null) {
                Text(
                    text = "Day $currentDay • ${currentParva.theme.displayName} • ${currentSaptaha.theme.displayName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Day ${mahaParva.currentDayNumber ?: "—"} of 343",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                Text(
                    text = mahaParva.startDate.format(dateFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LinearProgressIndicator(
                progress = mahaParva.progress,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

