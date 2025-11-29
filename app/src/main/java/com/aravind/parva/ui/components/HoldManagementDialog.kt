package com.aravind.parva.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.HoldPeriod
import com.aravind.parva.data.model.MahaParva
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HoldManagementDialog(
    mahaParva: MahaParva,
    onSave: (List<HoldPeriod>) -> Unit,
    onDismiss: () -> Unit
) {
    var holdPeriods by remember { mutableStateOf(mahaParva.holdPeriods.toMutableList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Hold Periods") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Summary
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "Summary",
                            style = MaterialTheme.typography.labelLarge
                        )
                        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                        Text(
                            "Start: ${mahaParva.startDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        val newEndDate = mahaParva.startDate.plusDays((342 + holdPeriods.sumOf { it.days }).toLong())
                        Text(
                            "End: ${newEndDate.format(dateFormatter)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "Total hold days: ${holdPeriods.sumOf { it.days }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Divider()

                // List of hold periods
                Text(
                    "Hold Periods",
                    style = MaterialTheme.typography.titleMedium
                )

                if (holdPeriods.isEmpty()) {
                    Text(
                        "No hold periods yet. Tap + to add one.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(holdPeriods) { hold ->
                            HoldPeriodItem(
                                holdPeriod = hold,
                                onDelete = {
                                    holdPeriods = holdPeriods.toMutableList().apply { remove(hold) }
                                }
                            )
                        }
                    }
                }

                // Add button
                OutlinedButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Hold Period")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(holdPeriods) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showAddDialog) {
        AddHoldPeriodDialog(
            onAdd = { newHold ->
                holdPeriods = holdPeriods.toMutableList().apply { add(newHold) }
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
private fun HoldPeriodItem(
    holdPeriod: HoldPeriod,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                Text(
                    "${holdPeriod.startDate.format(dateFormatter)} - ${holdPeriod.endDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "${holdPeriod.days} day${if (holdPeriod.days > 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                if (holdPeriod.reason.isNotEmpty()) {
                    Text(
                        holdPeriod.reason,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun AddHoldPeriodDialog(
    onAdd: (HoldPeriod) -> Unit,
    onDismiss: () -> Unit
) {
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var days by remember { mutableStateOf("1") }
    var reason by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Hold Period") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Start Date
                Text(
                    "Start Date",
                    style = MaterialTheme.typography.labelMedium
                )
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(startDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")))
                }

                // Number of Days
                OutlinedTextField(
                    value = days,
                    onValueChange = { if (it.isEmpty() || it.toIntOrNull() != null) days = it },
                    label = { Text("Number of Days") },
                    placeholder = { Text("e.g., 7") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Reason (optional)
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason (optional)") },
                    placeholder = { Text("e.g., Vacation, Sick") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                val endDate = startDate.plusDays((days.toIntOrNull() ?: 1) - 1L)
                Text(
                    "End date: ${endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val daysInt = days.toIntOrNull() ?: 1
                    if (daysInt > 0) {
                        onAdd(HoldPeriod(startDate, daysInt, reason))
                    }
                },
                enabled = days.toIntOrNull()?.let { it > 0 } == true
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showDatePicker) {
        SimpleDatePickerDialog(
            currentDate = startDate,
            onDateSelected = {
                startDate = it
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun SimpleDatePickerDialog(
    currentDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var year by remember { mutableStateOf(currentDate.year) }
    var month by remember { mutableStateOf(currentDate.monthValue) }
    var day by remember { mutableStateOf(currentDate.dayOfMonth) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Date") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = year.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { newYear ->
                            if (newYear in 2020..2100) year = newYear
                        }
                    },
                    label = { Text("Year") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = month.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { newMonth ->
                            if (newMonth in 1..12) month = newMonth
                        }
                    },
                    label = { Text("Month (1-12)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = day.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { newDay ->
                            if (newDay in 1..31) day = newDay
                        }
                    },
                    label = { Text("Day (1-31)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Selected: ${try { LocalDate.of(year, month, day).format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")) } catch (e: Exception) { "Invalid date" }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    try {
                        val selectedDate = LocalDate.of(year, month, day)
                        onDateSelected(selectedDate)
                    } catch (e: Exception) {
                        // Invalid date
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

