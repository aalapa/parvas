package com.aravind.parva.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AccountabilityPartnerDialog(
    currentEmail: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var email by remember { mutableStateOf(currentEmail) }
    var isValidEmail by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Accountability Partner") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Set up an accountability partner to receive weekly progress updates.",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() || it.isEmpty()
                    },
                    label = { Text("Email Address") },
                    placeholder = { Text("partner@example.com") },
                    isError = !isValidEmail,
                    supportingText = {
                        if (!isValidEmail) {
                            Text("Please enter a valid email address")
                        } else {
                            Text("You'll be reminded weekly to share your progress")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isValidEmail) {
                        onSave(email)
                        onDismiss()
                    }
                },
                enabled = isValidEmail
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

