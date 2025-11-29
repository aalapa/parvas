package com.aravind.parva.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.data.model.MandalaStyle
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun MahaParvaEditorDialog(
    existingMahaParva: MahaParva? = null, // null = create new, non-null = edit existing
    onDismiss: () -> Unit,
    onSave: (MahaParva) -> Unit
) {
    var title by remember { mutableStateOf(existingMahaParva?.title ?: "") }
    var description by remember { mutableStateOf(existingMahaParva?.description ?: "") }
    var accountabilityEmail by remember { mutableStateOf(existingMahaParva?.accountabilityPartnerEmail ?: "") }
    var startDate by remember { mutableStateOf(existingMahaParva?.startDate ?: LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedStyle by remember { mutableStateOf(existingMahaParva?.mandalaStyle ?: MandalaStyle.CIRCULAR_PETAL) }
    var useCustomColors by remember { mutableStateOf(existingMahaParva?.customStartColor != null) }
    var startColor by remember { mutableStateOf(existingMahaParva?.customStartColor ?: Color.Black) }
    var endColor by remember { mutableStateOf(existingMahaParva?.customEndColor ?: Color.Gray) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (existingMahaParva == null) "Create Maha-Parva" else "Edit Maha-Parva")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title *") },
                    placeholder = { Text("e.g., Master React Development") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("What do you want to achieve in 343 days?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )

                // Accountability Partner Email
                OutlinedTextField(
                    value = accountabilityEmail,
                    onValueChange = { accountabilityEmail = it },
                    label = { Text("Accountability Partner Email") },
                    placeholder = { Text("partner@example.com (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Divider()

                // Start Date Selection
                Text(
                    "Start Date",
                    style = MaterialTheme.typography.titleMedium
                )
                
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        startDate.format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
                    )
                }
                
                val endDate = startDate.plusDays(342 + (existingMahaParva?.totalHoldDays?.toLong() ?: 0))
                Text(
                    "End date: ${endDate.format(java.time.format.DateTimeFormatter.ofPattern("MMMM dd, yyyy"))} (343 days${if (existingMahaParva != null && existingMahaParva.totalHoldDays > 0) " + ${existingMahaParva.totalHoldDays} hold days" else ""})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Divider()

                // Mandala Style Selection
                Text(
                    "Mandala Style",
                    style = MaterialTheme.typography.titleMedium
                )

                MandalaStyle.values().forEach { style ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedStyle = style }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedStyle == style,
                            onClick = { selectedStyle = style }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                style.displayName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                style.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Divider()

                // Color Selection
                Text(
                    "Colors",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = useCustomColors,
                        onCheckedChange = { useCustomColors = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Use custom color gradient")
                }

                if (useCustomColors) {
                    Text(
                        "Pick start and end colors (app will generate 7 colors between them)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ColorPickerButton(
                            label = "Start Color",
                            color = startColor,
                            onColorSelected = { startColor = it }
                        )
                        ColorPickerButton(
                            label = "End Color",
                            color = endColor,
                            onColorSelected = { endColor = it }
                        )
                    }
                } else {
                    Text(
                        "Will use default VIBGYOR colors (Violet → Red)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val mahaParva = if (existingMahaParva == null) {
                        // Create new
                        MahaParva.create(
                            title = title,
                            description = description,
                            startDate = startDate,
                            accountabilityPartnerEmail = accountabilityEmail,
                            mandalaStyle = selectedStyle,
                            customStartColor = if (useCustomColors) startColor else null,
                            customEndColor = if (useCustomColors) endColor else null
                        )
                    } else {
                        // Update existing - need to recreate with new colors to regenerate gradient
                        MahaParva.create(
                            title = title,
                            description = description,
                            startDate = startDate,
                            accountabilityPartnerEmail = accountabilityEmail,
                            mandalaStyle = selectedStyle,
                            customStartColor = if (useCustomColors) startColor else null,
                            customEndColor = if (useCustomColors) endColor else null
                        ).copy(
                            id = existingMahaParva.id, // Keep original ID
                            createdAt = existingMahaParva.createdAt, // Keep original creation date
                            holdPeriods = existingMahaParva.holdPeriods // Keep hold periods
                        )
                    }
                    onSave(mahaParva)
                },
                enabled = title.isNotBlank()
            ) {
                Text(if (existingMahaParva == null) "Create" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
    
    // Modern Material3 Date Picker Dialog
    if (showDatePicker) {
        ModernDatePickerDialog(
            currentDate = startDate,
            allowPastDates = existingMahaParva != null, // Only allow past dates when editing
            onDateSelected = { selectedDate ->
                startDate = selectedDate
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernDatePickerDialog(
    currentDate: LocalDate,
    allowPastDates: Boolean = false,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    // Convert LocalDate to millis for DatePicker
    val initialDateMillis = currentDate
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    
    // Get today's date at midnight in millis for validation
    val todayMillis = LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(selectedDate)
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
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = true,
            title = {
                Text(
                    text = "Select Start Date",
                    modifier = Modifier.padding(16.dp)
                )
            },
            dateValidator = { dateMillis ->
                // If past dates are not allowed, only allow today or future
                if (allowPastDates) {
                    true // Allow all dates when editing
                } else {
                    dateMillis >= todayMillis // Only today or future when creating new
                }
            }
        )
    }
}

@Composable
private fun ColorPickerButton(
    label: String,
    color: Color,
    onColorSelected: (Color) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall
        )
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .clickable { showPicker = true }
        )
    }

    if (showPicker) {
        SimpleColorPickerDialog(
            currentColor = color,
            onDismiss = { showPicker = false },
            onColorSelected = {
                onColorSelected(it)
                showPicker = false
            }
        )
    }
}

@Composable
private fun SimpleColorPickerDialog(
    currentColor: Color,
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    // Preset colors for quick selection
    val presetColors = listOf(
        Color.Black, Color.DarkGray, Color.Gray, Color.LightGray, Color.White,
        Color.Red, Color(0xFFFF6B6B), Color(0xFFFF9999),
        Color(0xFFFF7F00), Color(0xFFFFA500), Color(0xFFFFD700), // Oranges/Yellows
        Color.Yellow, Color(0xFFFFFF99),
        Color.Green, Color(0xFF90EE90), Color(0xFF00FF00),
        Color.Cyan, Color(0xFF00CED1), Color(0xFF87CEEB),
        Color.Blue, Color(0xFF4169E1), Color(0xFF6495ED),
        Color(0xFF4B0082), Color(0xFF8B00FF), Color(0xFFDA70D6), // Purples
        Color.Magenta, Color(0xFFFF1493)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Color") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Choose a preset color:")
                
                // Grid of color options
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetColors.chunked(5).forEach { rowColors ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowColors.forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(color, CircleShape)
                                        .border(
                                            2.dp,
                                            if (color == currentColor)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                Color.Gray,
                                            CircleShape
                                        )
                                        .clickable { onColorSelected(color) }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

