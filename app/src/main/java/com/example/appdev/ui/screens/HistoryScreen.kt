package com.example.appdev.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults // Added
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appdev.data.local.QrEntity
import com.example.appdev.data.local.QrType
import com.example.appdev.ui.theme.SkyBlue
import com.example.appdev.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.historyState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
         Text(
            text = "History",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        // Chart Section
        HistoryChart(
            scannedCount = state.scannedCount,
            generatedCount = state.generatedCount
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Spaced out filters
        ) {
            FilterChip(
                selected = state.selectedFilter == null,
                onClick = { viewModel.setFilter(null) },
                label = { Text("All") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SkyBlue,
                    selectedLabelColor = White
                )
            )
            FilterChip(
                selected = state.selectedFilter == QrType.SCANNED,
                onClick = { viewModel.setFilter(QrType.SCANNED) },
                label = { Text("Scanned") },
                 colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SkyBlue,
                    selectedLabelColor = White
                )
            )
            FilterChip(
                selected = state.selectedFilter == QrType.GENERATED,
                onClick = { viewModel.setFilter(QrType.GENERATED) },
                label = { Text("Generated") },
                 colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SkyBlue,
                    selectedLabelColor = White
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp) // Increased spacing
        ) {
            items(state.items, key = { it.id }) { item ->
                HistoryItem(item = item, onDelete = { viewModel.deleteItem(item) })
            }
        }
    }
}

@Composable
fun HistoryChart(scannedCount: Int, generatedCount: Int) {
    val total = scannedCount + generatedCount
    val maxCount = maxOf(scannedCount, generatedCount, 1)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp), // Slightly taller
        shape = RoundedCornerShape(24.dp), // Softer corners
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat style
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Activity Overview",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                     text = "Total interactions: $total",
                     style = MaterialTheme.typography.bodyMedium,
                     color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Scanned Bar
                BarItem(
                    count = scannedCount,
                    max = maxCount,
                    label = "Scanned",
                    color = SkyBlue
                )
                
                // Generated Bar
                BarItem(
                    count = generatedCount,
                    max = maxCount,
                    label = "Generated",
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun BarItem(count: Int, max: Int, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        // Value Text
        Text(
            text = count.toString(), 
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        // Bar
        val animatedHeight by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (count == 0) 0.02f else count.toFloat() / max.toFloat(),
            label = "barHeight"
        )
        
        Box(
            modifier = Modifier
                .width(48.dp) // Wider bars
                .fillMaxHeight(fraction = 0.7f) // Maximum height constraint for bars within the row
                .weight(animatedHeight, fill = false) // Use weight for height relative to container? No, inside Row, weight is width. Inside Column, weight is height. 
                // Wait, BarItem is a Column. The Row contains BarItems. 
                // We want the Bar's height to be relative to the Max visual height.
                // Let's use fillMaxHeight(fraction) manually or use weight if parent is column.
        ) {
            Box(Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(color)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = label, 
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HistoryItem(item: QrEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                 Icon(
                    imageVector = if (item.type == QrType.SCANNED) Icons.Default.QrCodeScanner else Icons.Default.QrCode,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
           
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.content,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(item.timestamp)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            androidx.compose.material3.IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
