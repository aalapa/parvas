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
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.data.model.Saptaha
import com.aravind.parva.ui.components.MandalaSection
import com.aravind.parva.ui.components.MandalaView
import com.aravind.parva.ui.components.ParvaGoalCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParvaDetailScreen(
    mahaParvaId: String,
    parvaIndex: Int,
    viewMode: String, // "mandala" or "list" (initial mode from navigation)
    onBackClick: () -> Unit,
    onSaptahaClick: (Int) -> Unit
) {
    // Sample data
    val mahaParva = remember {
        MahaParva.create(
            title = "Spiritual Growth",
            startDate = LocalDate.now()
        )
    }
    val parva = mahaParva.parvas[parvaIndex]
    
    // Local state to toggle between views
    var currentViewMode by remember { mutableStateOf(viewMode) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${parva.theme.displayName} Parva") },
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
                ParvaGoalCard(
                    theme = parva.theme,
                    currentGoal = parva.customGoal,
                    isEditable = parva.isEditable,
                    onGoalChanged = { newGoal ->
                        // In real app: Update via ViewModel
                        // viewModel.updateParvaGoal(mahaParvaId, parvaIndex, newGoal)
                    },
                    modifier = Modifier.padding(16.dp)
                )
                
                Text(
                    text = "7 Saptahas - Tap any section",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                val sections = parva.saptahas.map { saptaha ->
                    MandalaSection(
                        label = saptaha.theme.displayName, // Full name, not truncated
                        color = saptaha.theme.color,
                        centerText = saptaha.number.toString(),
                        theme = saptaha.theme
                    )
                }

                val currentSaptahaIndex = parva.currentSaptaha?.let { it.number - 1 }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp)
                ) {
                    MandalaView(
                        sections = sections,
                        currentSectionIndex = currentSaptahaIndex,
                        onSectionClick = { index ->
                            onSaptahaClick(index)
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
                    ParvaGoalCard(
                        theme = parva.theme,
                        currentGoal = parva.customGoal,
                        isEditable = parva.isEditable,
                        onGoalChanged = { newGoal ->
                            // In real app: Update via ViewModel
                            // viewModel.updateParvaGoal(mahaParvaId, parvaIndex, newGoal)
                        }
                    )
                }
                
                itemsIndexed(parva.saptahas) { index, saptaha ->
                    SaptahaCard(
                        saptaha = saptaha,
                        isActive = saptaha.isActive,
                        onClick = { onSaptahaClick(index) }
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
                .background(saptaha.theme.color.copy(alpha = if (isActive) 0.3f else 0.1f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Saptaha ${saptaha.number}",
                    style = MaterialTheme.typography.labelLarge,
                    color = saptaha.theme.color
                )
                Text(
                    text = saptaha.theme.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = saptaha.theme.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
                Text(
                    text = "${saptaha.startDate.format(dateFormatter)} - ${saptaha.endDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodySmall
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

