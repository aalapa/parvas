package com.aravind.parva.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.Dina
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.ui.components.GoalCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaptahaDetailScreen(
    mahaParvaId: String,
    parvaIndex: Int,
    saptahaIndex: Int,
    onBackClick: () -> Unit,
    onDinaClick: (Int) -> Unit
) {
    // Sample data
    val mahaParva = remember {
        MahaParva.create(
            title = "Spiritual Growth",
            startDate = LocalDate.now()
        )
    }
    val parva = mahaParva.parvas[parvaIndex]
    val saptaha = parva.saptahas[saptahaIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${saptaha.theme.displayName} Saptaha") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .background(saptaha.theme.color.copy(alpha = 0.2f))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Saptaha ${saptaha.number}",
                            style = MaterialTheme.typography.labelLarge,
                            color = saptaha.theme.color
                        )
                        Text(
                            saptaha.theme.displayName,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            saptaha.theme.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        Text(
                            "${saptaha.startDate.format(dateFormatter)} - ${saptaha.endDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Goal Card for this Saptaha
            item {
                GoalCard(
                    title = "My Goal for this Week",
                    theme = saptaha.theme,
                    currentGoal = saptaha.customGoal,
                    isEditable = saptaha.isEditable,
                    onGoalChanged = { newGoal ->
                        // In real app: Update via ViewModel
                        // viewModel.updateSaptahaGoal(mahaParvaId, parvaIndex, saptahaIndex, newGoal)
                    }
                )
            }

            // 7 Dinas
            items(saptaha.dinas) { dina ->
                DinaCard(
                    dina = dina,
                    accentColor = saptaha.theme.color,
                    isToday = dina.date == LocalDate.now(),
                    onClick = { onDinaClick(dina.dayInSaptaha - 1) }
                )
            }
        }
    }
}

@Composable
private fun DinaCard(
    dina: Dina,
    accentColor: androidx.compose.ui.graphics.Color,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isToday)
                        accentColor.copy(alpha = 0.3f)
                    else if (dina.isCompleted)
                        accentColor.copy(alpha = 0.15f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Day ${dina.dayNumber}",
                        style = MaterialTheme.typography.labelLarge,
                        color = accentColor
                    )
                    if (isToday) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = accentColor
                        ) {
                            Text(
                                text = "Today",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }
                Text(
                    text = dina.dinaTheme.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = dina.dinaTheme.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd")
                Text(
                    text = dina.date.format(dateFormatter),
                    style = MaterialTheme.typography.bodySmall
                )
                if (dina.notes.isNotEmpty()) {
                    Text(
                        text = "\"${dina.notes.take(50)}${if (dina.notes.length > 50) "..." else ""}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = accentColor
                    )
                }
            }

            if (dina.isCompleted) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.headlineMedium,
                    color = accentColor
                )
            }
        }
    }
}

