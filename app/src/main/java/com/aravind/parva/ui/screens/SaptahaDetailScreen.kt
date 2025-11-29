package com.aravind.parva.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aravind.parva.data.model.Dina
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.ui.components.GoalCard
import com.aravind.parva.viewmodel.MahaParvaViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaptahaDetailScreen(
    viewModel: MahaParvaViewModel,
    parvaIndex: Int,
    saptahaIndex: Int,
    yojanaMode: Boolean, // Inherited from Maha-Parva
    onBackClick: () -> Unit,
    onDinaClick: (Int) -> Unit
) {
    // Get data from ViewModel
    val mahaParva by viewModel.mahaParva.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    
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
    
    val currentMahaParva = mahaParva!!
    val parva = currentMahaParva.parvas[parvaIndex]
    val saptaha = parva.saptahas[saptahaIndex]

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Saptaha ${saptaha.number} - ${saptaha.theme.sanskritName}") },
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 3.dp,
                            color = saptaha.theme.color,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Saptaha ${saptaha.number} - ${saptaha.theme.sanskritName}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            saptaha.theme.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            saptaha.theme.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
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
                    customColor = saptaha.customColor,
                    currentGoal = saptaha.customGoal,
                    isEditable = saptaha.isEditable,
                    onGoalChanged = { newGoal ->
                        viewModel.updateSaptahaGoal(parvaIndex, saptahaIndex, newGoal)
                    }
                )
            }

            // Mode indicator
            item {
                Text(
                    text = if (yojanaMode) "Yojana Mode: Plan all days" else "Today Mode: Current day only",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (yojanaMode) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                )
            }
            
            // 7 Dinas
            items(saptaha.dinas) { dina ->
                val isToday = dina.date == LocalDate.now()
                val isClickable = yojanaMode || isToday
                
                DinaCard(
                    dina = dina,
                    accentColor = saptaha.theme.color,
                    isToday = isToday,
                    isClickable = isClickable,
                    onClick = {
                        if (isClickable) {
                            onDinaClick(dina.dayInSaptaha - 1)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Enable Yojana mode to plan future periods")
                            }
                        }
                    }
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
    isClickable: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isClickable) {
                    Modifier.border(
                        width = if (isToday) 3.dp else if (dina.isCompleted) 2.dp else 1.dp,
                        color = accentColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier.border(
                        width = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isClickable) 
                MaterialTheme.colorScheme.surface 
            else 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                        color = if (isClickable) MaterialTheme.colorScheme.onSurface else Color.Gray
                    )
                    if (isToday) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isClickable) accentColor else Color.Gray
                        ) {
                            Text(
                                text = "Today",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                }
                Text(
                    text = dina.dinaTheme.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isClickable) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
                Text(
                    text = dina.dinaTheme.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isClickable) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray.copy(alpha = 0.6f)
                )
                val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd")
                Text(
                    text = dina.date.format(dateFormatter),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isClickable) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
                if (dina.notes.isNotEmpty()) {
                    Text(
                        text = "\"${dina.notes.take(50)}${if (dina.notes.length > 50) "..." else ""}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isClickable) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            if (dina.isCompleted) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (isClickable) accentColor else Color.Gray
                )
            }
        }
    }
}

