package com.nanys.care.presentation.caregiver

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nanys.care.R
import com.nanys.care.data.local.entity.CaregiverProfileEntity
import com.nanys.care.domain.model.BookingStatus
import com.nanys.care.presentation.common.*
import com.nanys.care.presentation.viewmodel.NanysViewModel

@Composable
fun CaregiverDashboardScreen(
    viewModel: NanysViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val email = viewModel.userEmail ?: return
    val bookings by viewModel.bookings.collectAsState()
    val pending = bookings.count { it.status == BookingStatus.PENDING }
    val today = bookings.count { it.status == BookingStatus.ACCEPTED }
    var profile by remember { mutableStateOf<com.nanys.care.domain.model.CaregiverProfile?>(null) }

    LaunchedEffect(email) {
        viewModel.loadBookingsForCaregiver(email)
        viewModel.loadCaregiverPublic(email)
        profile = viewModel.selectedCaregiver.value
    }
    val caregiverProfile by viewModel.selectedCaregiver.collectAsState()

    NanysScaffold(
        title = "Panel Cuidador",
        onProfileClick = { onNavigate("caregiver_profile") },
        onMessagesClick = { onNavigate("chat_list") },
        onSettingsClick = { onNavigate("settings") },
        onLogout = onLogout
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Solicitudes pendientes", "$pending", Modifier.weight(1f))
                StatCard("Citas hoy", "$today", Modifier.weight(1f))
                StatCard("Calificación", String.format("%.1f", caregiverProfile?.averageRating ?: 0.0), Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            NavigationButton("Mis solicitudes", Icons.Default.Notifications, { onNavigate("caregiver_requests") })
            NavigationButton("Agenda", Icons.Default.CalendarMonth, { onNavigate("caregiver_agenda") })
            NavigationButton("Chat", Icons.Default.Chat, { onNavigate("chat_list") })
            NavigationButton("Mi perfil", Icons.Default.Person, { onNavigate("caregiver_profile") })
            NavigationButton("Reglamento", Icons.Default.MenuBook, { onNavigate("caregiver_regulations") })
            NavigationButton("Notas privadas", Icons.Default.Note, { onNavigate("caregiver_private_notes") })
        }
    }
}

@Composable
private fun NavigationButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(text, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CaregiverRequestsScreen(viewModel: NanysViewModel, onBack: () -> Unit, onViewTutor: (String) -> Unit) {
    val bookings by viewModel.bookings.collectAsState()
    val pending = bookings.filter { it.status == BookingStatus.PENDING }
    NanysScaffold(title = "Solicitudes", onLogout = onBack, showProfileMenu = false) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp)) {
            items(pending) { booking ->
                BookingCard(booking, onClick = { onViewTutor(booking.tutorEmail) })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.respondBooking(booking.id, true) }) { Text("Aceptar") }
                    OutlinedButton(onClick = { viewModel.respondBooking(booking.id, false) }) { Text("Rechazar") }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CaregiverAgendaScreen(viewModel: NanysViewModel, onBack: () -> Unit) {
    val bookings by viewModel.bookings.collectAsState()
    NanysScaffold(title = "Agenda", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            VisualCalendarSection(bookings)
        }
    }
}

@Composable
fun CaregiverProfileScreen(viewModel: NanysViewModel, onBack: () -> Unit) {
    val email = viewModel.userEmail ?: return
    var experience by remember { mutableIntStateOf(0) }
    var certs by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    val cities by viewModel.cities.collectAsState()
    val states by viewModel.states.collectAsState()

    LaunchedEffect(email) { viewModel.loadCaregiverPublic(email) }
    val profile by viewModel.selectedCaregiver.collectAsState()
    LaunchedEffect(profile) {
        profile?.let {
            experience = it.experienceYears
            certs = it.certifications
            availability = it.availability
            rate = it.hourlyRate.toString()
            city = it.city
            state = it.state
        }
    }

    NanysScaffold(title = "Mi perfil", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            profile?.let {
                Text(it.fullName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                RatingStars(it.averageRating)
                Text("${it.reviewCount} reseñas")
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(experience.toString(), { experience = it.toIntOrNull() ?: 0 }, label = { Text("Años experiencia") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(certs, { certs = it }, label = { Text("Certificaciones") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(availability, { availability = it }, label = { Text("Disponibilidad") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(rate, { rate = it }, label = { Text("Tarifa/hora") }, modifier = Modifier.fillMaxWidth())
            DropdownField("Ciudad", city, cities) { city = it }
            DropdownField("Estado", state, states) { state = it }
            Button(
                onClick = {
                    viewModel.updateCaregiverProfile(
                        CaregiverProfileEntity(email, experienceYears = experience, certifications = certs, availability = availability, hourlyRate = rate.toDoubleOrNull() ?: 0.0, city = city, state = state, verified = profile?.verified ?: false)
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) { Text("Guardar perfil") }
        }
    }
}

@Composable
fun RegulationsScreen(onBack: () -> Unit) {
    NanysScaffold(title = "Reglamento", onLogout = onBack, showProfileMenu = false) { padding ->
        Text(
            stringResource(R.string.regulations_body),
            modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun PrivateNotesScreen(viewModel: NanysViewModel, onBack: () -> Unit) {
    val email = viewModel.userEmail ?: return
    val notes by viewModel.privateNotes.collectAsState()
    var tutorEmail by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(3) }

    LaunchedEffect(email) { viewModel.loadPrivateNotes(email) }

    NanysScaffold(title = "Notas privadas", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(tutorEmail, { tutorEmail = it }, label = { Text("Email tutor") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(noteText, { noteText = it }, label = { Text("Nota") }, modifier = Modifier.fillMaxWidth())
            Text("Calificación privada: $rating")
            Slider(rating.toFloat(), { rating = it.toInt() }, valueRange = 1f..5f, steps = 3)
            Button(onClick = { viewModel.addPrivateNote(tutorEmail, noteText, rating) }) { Text("Guardar nota") }
            Spacer(Modifier.height(16.dp))
            notes.forEach { n ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(n.tutorName, fontWeight = FontWeight.Bold)
                        RatingStars(n.rating.toDouble())
                        Text(n.note)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(label: String, value: String, options: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, { expanded = it }) {
        OutlinedTextField(value, {}, readOnly = true, label = { Text(label) }, modifier = Modifier.fillMaxWidth().menuAnchor(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) })
        ExposedDropdownMenu(expanded, { expanded = false }) {
            options.forEach { opt ->
                DropdownMenuItem(text = { Text(opt) }, onClick = { onSelect(opt); expanded = false })
            }
        }
    }
}
