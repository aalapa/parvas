package com.aravind.parva.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.ui.components.MandalaSection
import com.aravind.parva.ui.components.MandalaView
import com.aravind.parva.viewmodel.MahaParvaViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahaParvaDetailScreen(
    viewModel: MahaParvaViewModel,
    onBackClick: () -> Unit,
    onParvaClick: (Int) -> Unit
) {
    // Get data from ViewModel
    val mahaParva by viewModel.mahaParva.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    // Handle loading and null states
    if (isLoading || mahaParva == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    val currentMahaParva = mahaParva!! // Safe because we checked above

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentMahaParva.title) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Description card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            currentMahaParva.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        val currentDay = currentMahaParva.currentDayNumber
                        if (currentDay != null) {
                            Text(
                                "Day $currentDay of 343",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        LinearProgressIndicator(
                            progress = (currentMahaParva.currentDayNumber ?: 0).toFloat() / 343f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
            }

            // Mandala showing 7 Parvas
            Text(
                "The 7 Parvas",
                style = MaterialTheme.typography.titleLarge
            )

            val sections = currentMahaParva.parvas.map { parva ->
                MandalaSection(
                    label = parva.theme.displayName, // Full theme name
                    color = parva.color, // Use custom color if set
                    centerText = parva.number.toString(),
                    theme = parva.theme
                )
            }

            val currentParvaIndex = currentMahaParva.currentParva?.let { it.number - 1 }

            MandalaView(
                sections = sections,
                currentSectionIndex = currentParvaIndex,
                style = currentMahaParva.mandalaStyle, // Use Maha-Parva's style
                onSectionClick = { index ->
                    onParvaClick(index)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Legend
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Tap a section to explore",
                        style = MaterialTheme.typography.labelLarge
                    )
                    currentMahaParva.parvas.forEach { parva ->
                        Text(
                            "${parva.number}. ${parva.theme.sanskritName} - ${parva.theme.displayName}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

