package com.aravind.parva.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.FileProvider
import com.aravind.parva.data.preferences.UserPreferencesManager
import com.aravind.parva.data.repository.MahaParvaRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferencesManager: UserPreferencesManager,
    repository: MahaParvaRepository,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Collect preferences
    val isDarkTheme by preferencesManager.darkThemeFlow.collectAsState(initial = false)
    val useDynamicColors by preferencesManager.useDynamicColorsFlow.collectAsState(initial = false)
    
    // File picker for import - OpenDocument shows all file sources
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                try {
                    val inputStream = context.contentResolver.openInputStream(it)
                    val jsonString = inputStream?.bufferedReader()?.use { reader -> reader.readText() }
                    
                    if (jsonString != null) {
                        val gson = GsonBuilder().create()
                        val importedData = gson.fromJson(jsonString, com.aravind.parva.data.model.ExportData::class.java)
                        
                        // Import all Maha-Parvas
                        importedData.mahaParvas.forEach { mahaParva ->
                            repository.insertMahaParva(mahaParva)
                        }
                        
                        snackbarHostState.showSnackbar("Data imported successfully!")
                    }
                } catch (e: Exception) {
                    snackbarHostState.showSnackbar("Failed to import: ${e.message}")
                }
            }
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                            onCheckedChange = { enabled ->
                                scope.launch {
                                    preferencesManager.setDarkTheme(enabled)
                                }
                            }
                        )
                    }
                    
                    Divider()
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Use System Colors",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                if (useDynamicColors) 
                                    "Colors from your wallpaper (Android 12+)" 
                                else 
                                    "Default: Purple & Indigo theme",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = useDynamicColors,
                            onCheckedChange = { enabled ->
                                scope.launch {
                                    preferencesManager.setUseDynamicColors(enabled)
                                }
                            }
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
                        title = "Export All Data",
                        subtitle = "Export all Maha-Parvas as JSON file",
                        onClick = {
                            scope.launch {
                                try {
                                    val allMahaParvas = repository.getAllMahaParvasOnce()
                                    val exportData = com.aravind.parva.data.model.ExportData(
                                        exportDate = LocalDate.now(),
                                        version = "1.0.0",
                                        mahaParvas = allMahaParvas
                                    )
                                    
                                    val gson = GsonBuilder()
                                        .setPrettyPrinting()
                                        .create()
                                    val jsonString = gson.toJson(exportData)
                                    
                                    // Save to file with readable date/time
                                    val timestamp = java.time.LocalDateTime.now()
                                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
                                    val file = File(context.cacheDir, "mandala_export_$timestamp.json")
                                    file.writeText(jsonString)
                                    
                                    // Share file
                                    val uri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        file
                                    )
                                    
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "application/json"
                                        putExtra(Intent.EXTRA_STREAM, uri)
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    
                                    context.startActivity(Intent.createChooser(shareIntent, "Export Data"))
                                    snackbarHostState.showSnackbar("Data exported successfully!")
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar("Failed to export: ${e.message}")
                                }
                            }
                        }
                    )
                    
                    Divider()
                    
                    SettingsItem(
                        title = "Import Data",
                        subtitle = "Import previously exported JSON file",
                        onClick = {
                            // Use wildcard to show ALL files from ALL sources (Downloads, Drive, etc)
                            importLauncher.launch(arrayOf("*/*"))
                        }
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
                        "Mandala - A 343-day journey system",
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
