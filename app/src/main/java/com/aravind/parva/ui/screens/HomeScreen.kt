package com.aravind.parva.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.Parva
import com.aravind.parva.ui.components.ParvaCard
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onParvaClick: (String) -> Unit
) {
    // Sample data - in a real app, this would come from a ViewModel
    var parvas by remember {
        mutableStateOf(
            listOf(
                Parva.create(
                    title = "Morning Meditation",
                    description = "Establish a daily meditation practice",
                    startDate = LocalDate.now()
                ),
                Parva.create(
                    title = "Learn Kotlin",
                    description = "Master Kotlin programming language",
                    startDate = LocalDate.now().minusDays(10)
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Parva") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Add new Parva
                    val newParva = Parva.create(
                        title = "New Parva ${parvas.size + 1}",
                        description = "A new 49-day journey"
                    )
                    parvas = parvas + newParva
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Parva")
            }
        }
    ) { padding ->
        if (parvas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    "No Parvas yet. Tap + to create one!",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(parvas) { parva ->
                    ParvaCard(
                        parva = parva,
                        onClick = { onParvaClick(parva.id) }
                    )
                }
            }
        }
    }
}

