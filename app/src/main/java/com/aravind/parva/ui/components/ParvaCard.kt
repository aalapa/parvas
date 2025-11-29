package com.aravind.parva.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.Parva
import java.time.format.DateTimeFormatter

@Composable
fun ParvaCard(
    parva: Parva,
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
                text = parva.title,
                style = MaterialTheme.typography.titleLarge
            )
            
            if (parva.description.isNotEmpty()) {
                Text(
                    text = parva.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Day ${parva.currentDayNumber ?: "—"} of 49",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                Text(
                    text = "${parva.startDate.format(dateFormatter)} - ${parva.endDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            LinearProgressIndicator(
                progress = parva.progress,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

