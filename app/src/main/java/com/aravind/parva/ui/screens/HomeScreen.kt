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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.ui.components.MahaParvaCard
import com.aravind.parva.ui.components.MahaParvaEditorDialog
import com.aravind.parva.viewmodel.HomeViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMahaParvaClick: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    // Get data from ViewModel
    val mahaParvas by viewModel.mahaParvas.collectAsStateWithLifecycle(initialValue = emptyList())
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(initialValue = false)
    
    var showCreateDialog by remember { mutableStateOf(false) }
    var editingMahaParva by remember { mutableStateOf<MahaParva?>(null) }
    var deletingMahaParva by remember { mutableStateOf<MahaParva?>(null) }

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
                    showCreateDialog = true
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
                        onClick = { onMahaParvaClick(mahaParva.id) },
                        onEditClick = { editingMahaParva = mahaParva },
                        onDeleteClick = { deletingMahaParva = mahaParva }
                    )
                }
            }
        }
    }
    
    // Create/Edit Dialog
    if (showCreateDialog || editingMahaParva != null) {
        MahaParvaEditorDialog(
            existingMahaParva = editingMahaParva,
            onDismiss = {
                showCreateDialog = false
                editingMahaParva = null
            },
            onSave = { newOrUpdatedMahaParva ->
                if (editingMahaParva != null) {
                    // Update existing
                    viewModel.updateMahaParva(newOrUpdatedMahaParva)
                } else {
                    // Create new
                    viewModel.createMahaParva(newOrUpdatedMahaParva)
                }
                showCreateDialog = false
                editingMahaParva = null
            }
        )
    }
    
    // Delete Confirmation Dialog
    deletingMahaParva?.let { mahaParva ->
        AlertDialog(
            onDismissRequest = { deletingMahaParva = null },
            title = { Text("Delete Maha-Parva?") },
            text = {
                Text(
                    "Are you sure you want to delete \"${mahaParva.title}\"?\n\n" +
                    "This will permanently delete all 343 days of data including " +
                    "goals, intentions, and notes. This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteMahaParva(mahaParva)
                        deletingMahaParva = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingMahaParva = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

