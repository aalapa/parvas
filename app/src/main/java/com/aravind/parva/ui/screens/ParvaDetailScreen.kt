package com.aravind.parva.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.aravind.parva.data.model.MahaParva
import com.aravind.parva.data.model.Saptaha
import com.aravind.parva.ui.components.MandalaSection
import com.aravind.parva.ui.components.MandalaView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParvaDetailScreen(
    mahaParvaId: String,
    parvaIndex: Int,
    viewMode: String, // "mandala" or "list"
    onBackClick: () -> Unit,
    onSaptahaClick: (Int) -> Unit
) {
    // Sample data
    val mahaParva = remember {
        MahaParva.create(
            title = "Spiritual Growth",
            startDate = LocalDate.now()
        )
    }
    val parva = mahaParva.parvas[parvaIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${parva.theme.displayName} Parva") },
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
        if (viewMode == "mandala") {
            // Mandala view of 7 Saptahas
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val sections = parva.saptahas.map { saptaha ->
                    MandalaSection(
                        label = saptaha.theme.displayName.take(4),
                        color = saptaha.theme.color,
                        centerText = saptaha.number.toString(),
                        theme = saptaha.theme
                    )
                }

                val currentSaptahaIndex = parva.currentSaptaha?.let { it.number - 1 }

                MandalaView(
                    sections = sections,
                    currentSectionIndex = currentSaptahaIndex,
                    onSectionClick = { index ->
                        onSaptahaClick(index)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        } else {
            // List view of 7 Saptahas
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(parva.saptahas) { index, saptaha ->
                    SaptahaCard(
                        saptaha = saptaha,
                        isActive = saptaha.isActive,
                        onClick = { onSaptahaClick(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SaptahaCard(
    saptaha: Saptaha,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(saptaha.theme.color.copy(alpha = if (isActive) 0.3f else 0.1f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Saptaha ${saptaha.number}",
                    style = MaterialTheme.typography.labelLarge,
                    color = saptaha.theme.color
                )
                Text(
                    text = saptaha.theme.displayName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = saptaha.theme.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
                Text(
                    text = "${saptaha.startDate.format(dateFormatter)} - ${saptaha.endDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (isActive) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = saptaha.theme.color
                ) {
                    Text(
                        text = "Active",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}

