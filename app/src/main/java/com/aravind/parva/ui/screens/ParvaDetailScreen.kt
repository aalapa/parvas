package com.aravind.parva.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.data.model.Saptaha
import com.aravind.parva.ui.components.MandalaSection
import com.aravind.parva.ui.components.MandalaView
import com.aravind.parva.ui.components.GoalCard
import com.aravind.parva.viewmodel.MahaParvaViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParvaDetailScreen(
    viewModel: MahaParvaViewModel,
    parvaIndex: Int,
    viewMode: String, // "mandala" or "list" (initial mode from navigation)
    yojanaMode: Boolean, // Inherited from Maha-Parva
    onBackClick: () -> Unit,
    onSaptahaClick: (Int, Boolean) -> Unit // Pass yojanaMode down
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
    
    // Local state to toggle between views
    var currentViewMode by remember { mutableStateOf(viewMode) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Parva ${parva.number} - ${parva.theme.sanskritName}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Toggle button to switch between views
                    IconButton(
                        onClick = {
                            currentViewMode = if (currentViewMode == "mandala") "list" else "mandala"
                        }
                    ) {
                        Icon(
                            imageVector = if (currentViewMode == "mandala") 
                                Icons.Default.List 
                            else 
                                Icons.Default.Menu,
                            contentDescription = if (currentViewMode == "mandala") 
                                "Switch to List View" 
                            else 
                                "Switch to Mandala View"
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
        if (currentViewMode == "mandala") {
            // Mandala view of 7 Saptahas
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Goal Card at the top
                GoalCard(
                    title = "My Goal for this Parva (49 days)",
                    theme = parva.theme,
                    customColor = parva.customColor,
                    currentGoal = parva.customGoal,
                    isEditable = parva.isEditable,
                    onGoalChanged = { newGoal ->
                        viewModel.updateParvaGoal(parvaIndex, newGoal)
                    },
                    modifier = Modifier.padding(16.dp)
                )
                
                Text(
                    text = if (yojanaMode) "Yojana Mode: Plan all Saptahas" else "Today Mode: Current Saptaha only",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (yojanaMode) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                )
                
                val currentSaptahaIndex = parva.currentSaptaha?.let { it.number - 1 }
                
                val sections = parva.saptahas.mapIndexed { index, saptaha ->
                    val isCurrentSection = index == currentSaptahaIndex
                    val isClickable = yojanaMode || isCurrentSection
                    
                    MandalaSection(
                        label = saptaha.theme.displayName,
                        color = if (isClickable) saptaha.color else Color.Gray,
                        centerText = saptaha.number.toString(),
                        theme = saptaha.theme
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp)
                ) {
                MandalaView(
                    sections = sections,
                    currentSectionIndex = if (yojanaMode) currentSaptahaIndex else null,
                    style = currentMahaParva.mandalaStyle,
                    onSectionClick = { index ->
                        val isCurrentSection = index == currentSaptahaIndex
                        if (yojanaMode || isCurrentSection) {
                            onSaptahaClick(index, yojanaMode)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Enable Yojana mode to plan future periods")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                }
            }
        } else {
            // List view of 7 Saptahas
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Goal Card as first item
                item {
                    GoalCard(
                        title = "My Goal for this Parva (49 days)",
                        theme = parva.theme,
                        customColor = parva.customColor,
                        currentGoal = parva.customGoal,
                        isEditable = parva.isEditable,
                        onGoalChanged = { newGoal ->
                            viewModel.updateParvaGoal(parvaIndex, newGoal)
                        }
                    )
                }
                
                itemsIndexed(parva.saptahas) { index, saptaha ->
                    val currentSaptahaIndex = parva.currentSaptaha?.let { it.number - 1 }
                    val isCurrentSection = index == currentSaptahaIndex
                    val isClickable = yojanaMode || isCurrentSection
                    
                    SaptahaCard(
                        saptaha = saptaha,
                        isActive = saptaha.isActive,
                        isClickable = isClickable,
                        onClick = {
                            if (isClickable) {
                                onSaptahaClick(index, yojanaMode)
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
}

@Composable
private fun SaptahaCard(
    saptaha: Saptaha,
    isActive: Boolean,
    isClickable: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isClickable) 
                MaterialTheme.colorScheme.surface 
            else 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isClickable)
                        saptaha.theme.color.copy(alpha = if (isActive) 0.3f else 0.1f)
                    else
                        Color.Gray.copy(alpha = 0.2f)
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Saptaha ${saptaha.number} - ${saptaha.theme.sanskritName}",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isClickable) saptaha.theme.color else Color.Gray
                )
                Text(
                    text = saptaha.theme.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isClickable) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
                Text(
                    text = saptaha.theme.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isClickable) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray.copy(alpha = 0.6f)
                )
                val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
                Text(
                    text = "${saptaha.startDate.format(dateFormatter)} - ${saptaha.endDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isClickable) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }

            if (isActive) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = saptaha.theme.color
                ) {
                    Text(
                        text = "Active",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}

