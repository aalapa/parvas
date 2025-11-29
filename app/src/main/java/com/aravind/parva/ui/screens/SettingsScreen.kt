package com.aravind.parva.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onExportJournalClick: () -> Unit,
    onExportDataClick: () -> Unit,
    onImportDataClick: () -> Unit
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var saptahaViewMode by remember { mutableStateOf("list") } // "list" or "mandala"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Appearance",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Dark Theme",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "Use dark color scheme",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { 
                                isDarkTheme = it
                                // In real app: Save to DataStore
                            }
                        )
                    }
                }
            }

            // View Preferences Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "View Preferences",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        "Saptaha View Mode",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Choose how to display the 7 Saptahas within a Parva",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = saptahaViewMode == "list",
                            onClick = { saptahaViewMode = "list" },
                            label = { Text("List View") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = saptahaViewMode == "mandala",
                            onClick = { saptahaViewMode = "mandala" },
                            label = { Text("Mandala View") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Data Management Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Data Management",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    SettingsItem(
                        title = "Export Journal",
                        subtitle = "Export notes as a journal for a specific Maha-Parva",
                        onClick = onExportJournalClick
                    )
                    
                    Divider()
                    
                    SettingsItem(
                        title = "Export All Data",
                        subtitle = "Export all Maha-Parvas and notes",
                        onClick = onExportDataClick
                    )
                    
                    Divider()
                    
                    SettingsItem(
                        title = "Import Data",
                        subtitle = "Import previously exported data",
                        onClick = onImportDataClick
                    )
                }
            }

            // About Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "About",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Parva - A 343-day journey system",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Version 1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

