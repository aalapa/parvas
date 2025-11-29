package com.aravind.parva.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aravind.parva.data.model.HoldPeriod
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.ui.components.HoldManagementDialog
import com.aravind.parva.ui.components.MandalaSection
import com.aravind.parva.ui.components.MandalaView
import com.aravind.parva.viewmodel.MahaParvaViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahaParvaDetailScreen(
    viewModel: MahaParvaViewModel,
    onBackClick: () -> Unit,
    onParvaClick: (Int, Boolean) -> Unit // Added yojanaMode parameter
) {
    // Get data from ViewModel
    val mahaParva by viewModel.mahaParva.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
    var showHoldDialog by remember { mutableStateOf(false) }
    var yojanaMode by remember { mutableStateOf(false) } // Planning mode toggle
    
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(currentMahaParva.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Yojana (Planning) Mode Toggle
                    IconButton(
                        onClick = { yojanaMode = !yojanaMode }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = if (yojanaMode) "Exit Yojana Mode" else "Enter Yojana Mode",
                            tint = if (yojanaMode)
                                MaterialTheme.colorScheme.tertiary
                            else
                                MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    
                    // Hold Management
                    IconButton(onClick = { showHoldDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Manage Hold Periods",
                            tint = if (currentMahaParva.isOnHold)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onPrimary
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

            val currentParvaIndex = currentMahaParva.currentParva?.let { it.number - 1 }
            
            val sections = currentMahaParva.parvas.mapIndexed { index, parva ->
                val isCurrentSection = index == currentParvaIndex
                val isClickable = yojanaMode || isCurrentSection
                
                MandalaSection(
                    label = parva.theme.displayName,
                    color = if (isClickable) parva.color else Color.Gray,
                    centerText = parva.number.toString(),
                    theme = parva.theme
                )
            }

            MandalaView(
                sections = sections,
                currentSectionIndex = if (yojanaMode) currentParvaIndex else null, // Only highlight in non-yojana mode
                style = currentMahaParva.mandalaStyle,
                onSectionClick = { index ->
                    val isCurrentSection = index == currentParvaIndex
                    if (yojanaMode || isCurrentSection) {
                        onParvaClick(index, yojanaMode)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Enable Yojana mode to plan future periods")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // Legend and Mode Indicator
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            if (yojanaMode) "Yojana Mode: Plan all periods" else "Today Mode: Focus on current",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (yojanaMode) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Divider()
                    
                    currentMahaParva.parvas.forEachIndexed { index, parva ->
                        val isCurrent = index == currentParvaIndex
                        Text(
                            "${parva.number}. ${parva.theme.sanskritName} - ${parva.theme.displayName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (yojanaMode || isCurrent) 
                                MaterialTheme.colorScheme.onSurface 
                            else 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }

    // Hold Management Dialog
    if (showHoldDialog) {
        HoldManagementDialog(
            mahaParva = currentMahaParva,
            onSave = { newHoldPeriods ->
                viewModel.updateHoldPeriods(newHoldPeriods)
                showHoldDialog = false
            },
            onDismiss = { showHoldDialog = false }
        )
    }
}

