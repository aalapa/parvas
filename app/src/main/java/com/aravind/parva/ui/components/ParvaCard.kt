package com.aravind.parva.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.MahaParva
import java.time.format.DateTimeFormatter

@Composable
fun MahaParvaCard(
    mahaParva: MahaParva,
    onClick: () -> Unit,
    onEditClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = mahaParva.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Maha-Parva",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
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

