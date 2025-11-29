package com.aravind.parva.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.Parva
import com.aravind.parva.ui.components.MiniParvaSection
import com.aravind.parva.ui.components.ParvaProgress
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParvaDetailScreen(
    parvaId: String,
    onBackClick: () -> Unit
) {
    // Sample data - in a real app, this would come from a ViewModel
    val parva = remember {
        Parva.create(
            title = "Morning Meditation",
            description = "Establish a daily meditation practice",
            startDate = LocalDate.now()
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(parva.title) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with description and progress
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            parva.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        ParvaProgress(parva = parva)
                    }
                }
            }

            // Mini-Parvas (7 sections of 7 days each)
            items(7) { miniParvaIndex ->
                val miniParvaNumber = miniParvaIndex + 1
                val startDay = miniParvaIndex * 7 + 1
                val endDay = startDay + 6
                val daysInMiniParva = parva.days.subList(startDay - 1, endDay)

                MiniParvaSection(
                    miniParvaNumber = miniParvaNumber,
                    days = daysInMiniParva
                )
            }
        }
    }
}

