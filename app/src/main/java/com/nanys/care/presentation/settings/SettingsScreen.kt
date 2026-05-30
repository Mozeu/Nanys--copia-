package com.nanys.care.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nanys.care.presentation.common.NanysScaffold

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkTheme by remember { mutableStateOf(false) }

    NanysScaffold(title = "Configuración", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Preferencias de la aplicación (demo local)", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, null, tint = MaterialTheme.colorScheme.primary)
                    Text("Notificaciones simuladas")
                }
                Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, null, tint = MaterialTheme.colorScheme.primary)
                    Text("Tema oscuro")
                }
                Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
            }
            Spacer(Modifier.height(16.dp))
            Text("Nanys Care v1.0 — Datos almacenados localmente con Room.", style = MaterialTheme.typography.bodySmall)
        }
    }
}
