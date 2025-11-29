package com.aravind.parva.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.MahaParva
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DinaDetailScreen(
    mahaParvaId: String,
    parvaIndex: Int,
    saptahaIndex: Int,
    dinaIndex: Int,
    onBackClick: () -> Unit
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
    val dina = saptaha.dinas[dinaIndex]

    var notes by remember { mutableStateOf(dina.notes) }
    var isCompleted by remember { mutableStateOf(dina.isCompleted) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Day ${dina.dayNumber}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isCompleted = !isCompleted }
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Mark as complete",
                            tint = if (isCompleted) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dina info card
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
                        "Day ${dina.dayNumber} of 343",
                        style = MaterialTheme.typography.labelLarge,
                        color = saptaha.theme.color
                    )
                    Text(
                        dina.dinaTheme.displayName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        dina.dinaTheme.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")
                    Text(
                        dina.date.format(dateFormatter),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        "Parva: ${parva.theme.displayName}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Saptaha: ${saptaha.theme.displayName}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Notes section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Notes for Posterity",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Record your thoughts, experiences, and insights from this day.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp),
                        placeholder = { Text("Write your reflections here...") },
                        maxLines = 15
                    )

                    Button(
                        onClick = {
                            // In real app: Save to database
                            // viewModel.updateDinaNotes(mahaParvaId, dina.dayNumber, notes)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Notes")
                    }
                }
            }

            // Status card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Day Completed",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = isCompleted,
                        onCheckedChange = { isCompleted = it }
                    )
                }
            }
        }
    }
}

