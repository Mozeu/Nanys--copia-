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
import com.nanys.care.presentation.common.DashboardGridCard
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

    NanysScaffold(title = "Inicio", onLogout = onLogout) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Verificados", "$verified", Modifier.weight(1f))
                StatCard("Mensajes hoy", "$todayMsgs", Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth()) {
                DashboardGridCard("Conversaciones", Icons.Default.Chat, Modifier.weight(1f), "Supervisión") { onNavigate("supervisor_chats") }
                DashboardGridCard("Verificaciones", Icons.Default.VerifiedUser, Modifier.weight(1f), "Cuidadores") { onNavigate("supervisor_verification") }
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
