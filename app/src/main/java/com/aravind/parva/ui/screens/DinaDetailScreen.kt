package com.aravind.parva.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.viewmodel.MahaParvaViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DinaDetailScreen(
    viewModel: MahaParvaViewModel,
    parvaIndex: Int,
    saptahaIndex: Int,
    dinaIndex: Int,
    onBackClick: () -> Unit
) {
    // Get data from ViewModel
    val mahaParva by viewModel.mahaParva.collectAsStateWithLifecycle()
    val isLoadingData by viewModel.isLoading.collectAsStateWithLifecycle()
    
    // Handle loading and null states
    if (isLoadingData || mahaParva == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    val currentMahaParva = mahaParva!!
    val parva = currentMahaParva.parvas[parvaIndex]
    val saptaha = parva.saptahas[saptahaIndex]
    val dina = saptaha.dinas[dinaIndex]

    var dailyIntention by remember(dina.dayNumber) { mutableStateOf(dina.dailyIntention) }
    var notes by remember(dina.dayNumber) { mutableStateOf(dina.notes) }
    var isCompleted by remember(dina.dayNumber) { mutableStateOf(dina.isCompleted) }

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
                        "Parva ${parva.number} - ${parva.theme.sanskritName} (${parva.theme.displayName})",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        "Saptaha ${saptaha.number} - ${saptaha.theme.sanskritName} (${saptaha.theme.displayName})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Daily Intention section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = saptaha.theme.color,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "🎯",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Daily Intention",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        "What do you intend to accomplish today?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (!dina.isEditable) {
                        // Show as read-only for past days
                        Text(
                            text = if (dailyIntention.isEmpty()) 
                                "No intention was set" 
                            else 
                                dailyIntention,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (dailyIntention.isEmpty())
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        OutlinedTextField(
                            value = dailyIntention,
                            onValueChange = { dailyIntention = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g., Complete React hooks tutorial, build todo app") },
                            minLines = 2,
                            maxLines = 4,
                            enabled = dina.isEditable
                        )
                    }
                }
            }

            // Notes section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = saptaha.theme.color,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "📝",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Reflection & Notes",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        "Record what you actually did, learned, and experienced.",
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
                            viewModel.updateDina(
                                dayNumber = dina.dayNumber,
                                dailyIntention = dailyIntention,
                                notes = notes,
                                isCompleted = isCompleted
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save")
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

