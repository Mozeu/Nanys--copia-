package com.nanys.care.presentation.supervisor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nanys.care.domain.model.CaregiverProfile
import com.nanys.care.presentation.common.NanysScaffold
import com.nanys.care.presentation.common.StatCard
import com.nanys.care.presentation.viewmodel.NanysViewModel

@Composable
fun SupervisorDashboardScreen(viewModel: NanysViewModel, onNavigate: (String) -> Unit, onLogout: () -> Unit) {
    val verified by viewModel.verifiedCount.collectAsState()
    val todayMsgs by viewModel.todayMessages.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadSupervisorStats()
        viewModel.loadCaregivers()
    }

    NanysScaffold(title = "Panel Supervisor", onLogout = onLogout) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Verificados", "$verified", Modifier.weight(1f))
                StatCard("Mensajes hoy", "$todayMsgs", Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            Card(onClick = { onNavigate("supervisor_chats") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp)) {
                Row(Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Chat, null)
                    Spacer(Modifier.width(12.dp))
                    Text("Ver todas las conversaciones", fontWeight = FontWeight.Medium)
                }
            }
            Card(onClick = { onNavigate("supervisor_verification") }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp)) {
                Row(Modifier.padding(16.dp)) {
                    Icon(Icons.Default.VerifiedUser, null)
                    Spacer(Modifier.width(12.dp))
                    Text("Gestionar verificaciones", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun VerificationScreen(viewModel: NanysViewModel, onBack: () -> Unit) {
    val caregivers by viewModel.caregivers.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadCaregivers() }

    NanysScaffold(title = "Verificación de antecedentes", onLogout = onBack, showProfileMenu = false) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp)) {
            items(caregivers) { cg ->
                VerificationRow(cg, onToggle = { viewModel.setCaregiverVerified(cg.email, !cg.verified) })
            }
        }
    }
}

@Composable
private fun VerificationRow(cg: CaregiverProfile, onToggle: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(cg.fullName, fontWeight = FontWeight.SemiBold)
                Text("${cg.city} · ${cg.experienceYears} años exp.")
            }
            Switch(checked = cg.verified, onCheckedChange = { onToggle() })
        }
    }
}
