package com.nanys.care.presentation.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nanys.care.domain.model.CatalogItem
import com.nanys.care.presentation.common.ButtonIcon
import com.nanys.care.presentation.common.NanysScaffold
import com.nanys.care.presentation.common.StatCard
import com.nanys.care.presentation.viewmodel.NanysViewModel

@Composable
fun AdminDashboardScreen(viewModel: NanysViewModel, onNavigate: (String) -> Unit, onLogout: () -> Unit) {
    val stats by viewModel.adminStats.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadAdminStats() }

    NanysScaffold(title = "Panel Admin", onLogout = onLogout) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Cuidadores", "${stats.first}", Modifier.weight(1f))
                StatCard("Tutores", "${stats.second}", Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            CatalogNavButton("Experiencias", "experience", onNavigate)
            CatalogNavButton("Certificaciones", "certification", onNavigate)
            CatalogNavButton("Ciudades", "city", onNavigate)
            CatalogNavButton("Estados", "state", onNavigate)
        }
    }
}

@Composable
private fun CatalogNavButton(label: String, category: String, onNavigate: (String) -> Unit) {
    val icon = when (category) {
        "experience" -> Icons.Default.Work
        "certification" -> Icons.Default.School
        "city", "state" -> Icons.Default.Place
        else -> Icons.AutoMirrored.Filled.List
    }
    Card(onClick = { onNavigate("admin_catalog/$category") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(16.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Text(label, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CatalogManagementScreen(viewModel: NanysViewModel, category: String, onBack: () -> Unit) {
    val items by viewModel.catalogItems.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    LaunchedEffect(category) { viewModel.loadCatalog(category) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Agregar registro") },
            text = { OutlinedTextField(newName, { newName = it }, label = { Text("Nombre") }) },
            confirmButton = {
                TextButton(onClick = { viewModel.addCatalogItem(category, newName); newName = ""; showDialog = false }) {
                    ButtonIcon(Icons.Default.Add, "Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    ButtonIcon(Icons.Default.Close, "Cancelar")
                }
            }
        )
    }

    NanysScaffold(title = "Catálogo: $category", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding)) {
            Button(onClick = { showDialog = true }, modifier = Modifier.padding(16.dp)) {
                ButtonIcon(Icons.Default.Add, "Agregar")
            }
            LazyColumn(Modifier.padding(horizontal = 16.dp)) {
                items(items) { item ->
                    CatalogItemRow(item, onDelete = { viewModel.deleteCatalogItem(item) })
                }
            }
        }
    }
}

@Composable
private fun CatalogItemRow(item: CatalogItem, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(item.name)
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Eliminar") }
        }
    }
}
