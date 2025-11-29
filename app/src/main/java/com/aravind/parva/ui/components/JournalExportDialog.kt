package com.aravind.parva.ui.components

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.aravind.parva.data.model.MahaParva
import java.io.File

@Composable
fun JournalExportDialog(
    mahaParvas: List<MahaParva>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedMahaParva by remember { mutableStateOf<MahaParva?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Export Journal") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Select a Maha-Parva to export its journal:")
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mahaParvas) { mahaParva ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = if (selectedMahaParva?.id == mahaParva.id)
                                CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            else
                                CardDefaults.cardColors()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                RadioButton(
                                    selected = selectedMahaParva?.id == mahaParva.id,
                                    onClick = { selectedMahaParva = mahaParva }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        mahaParva.title,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        "Day ${mahaParva.currentDayNumber ?: "—"} of 343",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedMahaParva?.let { mahaParva ->
                        // Generate journal
                        val journal = mahaParva.getJournal()
                        
                        // Create file
                        val file = File(context.cacheDir, "${mahaParva.title}_journal.txt")
                        file.writeText(journal)
                        
                        // Share file
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )
                        
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            putExtra(Intent.EXTRA_SUBJECT, "${mahaParva.title} - Journal")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Share Journal")
                        )
                    }
                    onDismiss()
                },
                enabled = selectedMahaParva != null
            ) {
                Text("Export & Share")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

