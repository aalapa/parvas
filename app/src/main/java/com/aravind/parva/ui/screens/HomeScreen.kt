package com.aravind.parva.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.ui.components.MahaParvaCard
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMahaParvaClick: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    // Sample data - in real app, this would come from ViewModel/Repository
    var mahaParvas by remember {
        mutableStateOf(
            listOf(
                MahaParva.create(
                    title = "Spiritual Growth",
                    description = "A journey of 343 days towards inner peace",
                    startDate = LocalDate.now()
                ),
                MahaParva.create(
                    title = "Master Kotlin",
                    description = "Complete mastery of Kotlin and Android development",
                    startDate = LocalDate.now().minusDays(50)
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Maha-Parva") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Add new Maha-Parva
                    val newMahaParva = MahaParva.create(
                        title = "New Journey ${mahaParvas.size + 1}",
                        description = "A new 343-day cycle"
                    )
                    mahaParvas = mahaParvas + newMahaParva
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Maha-Parva")
            }
        }
    ) { padding ->
        if (mahaParvas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    "No Maha-Parvas yet. Tap + to create one!",
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
                items(mahaParvas) { mahaParva ->
                    MahaParvaCard(
                        mahaParva = mahaParva,
                        onClick = { onMahaParvaClick(mahaParva.id) }
                    )
                }
            }
        }
    }
}

