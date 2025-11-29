package com.aravind.parva.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.CycleTheme

@Composable
fun ParvaGoalCard(
    theme: CycleTheme,
    currentGoal: String,
    isEditable: Boolean,
    onGoalChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = theme.color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "My Goal for this Parva",
                    style = MaterialTheme.typography.labelMedium,
                    color = theme.color
                )
                
                if (currentGoal.isEmpty()) {
                    Text(
                        text = "No goal set yet. Tap pencil to add one!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = currentGoal,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (isEditable) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit goal",
                        tint = theme.color
                    )
                }
            }
        }
    }

    if (showDialog) {
        ParvaGoalEditDialog(
            theme = theme,
            currentGoal = currentGoal,
            onDismiss = { showDialog = false },
            onSave = { newGoal ->
                onGoalChanged(newGoal)
                showDialog = false
            }
        )
    }
}

@Composable
private fun ParvaGoalEditDialog(
    theme: CycleTheme,
    currentGoal: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var goal by remember { mutableStateOf(currentGoal) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Set Your Goal")
                Text(
                    text = "${theme.displayName} Parva",
                    style = MaterialTheme.typography.bodyMedium,
                    color = theme.color
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = theme.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = goal,
                    onValueChange = { goal = it },
                    label = { Text("Your Goal") },
                    placeholder = { Text("What do you want to achieve?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                // Template suggestions
                Text(
                    text = "💡 Suggestions based on \"${theme.displayName}\" theme:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                theme.goalPrompts.forEach { prompt ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                goal = if (goal.isEmpty()) prompt else "$goal\n$prompt"
                            },
                        color = theme.color.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = prompt,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Text(
                    text = "Tap any suggestion to use it as a template",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(goal) }
            ) {
                Text("Save Goal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

