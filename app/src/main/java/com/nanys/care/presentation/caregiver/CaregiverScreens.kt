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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.nanys.care.R
import com.nanys.care.data.local.entity.CaregiverProfileEntity
import com.nanys.care.domain.model.BookingStatus
import com.nanys.care.presentation.common.*
import com.nanys.care.presentation.viewmodel.NanysViewModel
import java.util.Locale

@Composable
fun CaregiverDashboardScreen(
    viewModel: NanysViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val email = viewModel.userEmail ?: return
    val bookings by viewModel.bookings.collectAsState()
    val pending = bookings.count { it.status == BookingStatus.PENDING }
    val accepted = bookings.count { it.status == BookingStatus.ACCEPTED }
    val completed = bookings.count { it.status == BookingStatus.COMPLETED }

    LaunchedEffect(email) {
        viewModel.loadBookingsForCaregiver(email)
        viewModel.loadCaregiverPublic(email)
    }
    val caregiverProfile by viewModel.selectedCaregiver.collectAsState()

    NanysScaffold(
        title = "Inicio",
        onProfileClick = { onNavigate("caregiver_profile") },
        onMessagesClick = { onNavigate("chat_list") },
        onSettingsClick = { onNavigate("settings") },
        onLogout = onLogout
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Solicitudes pendientes", "$pending", Modifier.weight(1f))
                StatCard("Citas aceptadas", "$accepted", Modifier.weight(1f))
                StatCard("Calificación", String.format("%.1f", caregiverProfile?.averageRating ?: 0.0), Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth()) {
                DashboardGridCard("Solicitudes", Icons.Default.Notifications, Modifier.weight(1f), "$pending pendientes") { onNavigate("caregiver_requests") }
                DashboardGridCard("Agenda", Icons.Default.CalendarMonth, Modifier.weight(1f), "$accepted aceptadas") { onNavigate("caregiver_agenda") }
            }
            Row(Modifier.fillMaxWidth()) {
                DashboardGridCard("Chat", Icons.Default.Chat, Modifier.weight(1f), "Mensajes") { onNavigate("chat_list") }
                DashboardGridCard("Mi perfil", Icons.Default.Person, Modifier.weight(1f), "Tarifas y disponibilidad") { onNavigate("caregiver_profile") }
            }
            Row(Modifier.fillMaxWidth()) {
                DashboardGridCard("Reglamento", Icons.Default.MenuBook, Modifier.weight(1f), "Políticas") { onNavigate("caregiver_regulations") }
                DashboardGridCard("Notas privadas", Icons.Default.Note, Modifier.weight(1f), "$completed completadas") { onNavigate("caregiver_private_notes") }
            }
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
                    Button(onClick = { viewModel.respondBooking(booking.id, true) }) {
                        ButtonIcon(Icons.Default.Check, "Aceptar")
                    }
                    OutlinedButton(onClick = { viewModel.respondBooking(booking.id, false) }) {
                        ButtonIcon(Icons.Default.Close, "Rechazar")
                    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverProfileScreen(
    viewModel: NanysViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit = onBack
) {
    val email = viewModel.userEmail ?: return
    var experience by remember { mutableIntStateOf(0) }
    var certs by remember { mutableStateOf("") }
    var availabilityStart by remember { mutableStateOf("") }
    var availabilityEnd by remember { mutableStateOf("") }
    var availabilityExceptions by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var extraRate by remember { mutableStateOf("") }
    var rateClearedOnFocus by remember { mutableStateOf(false) }
    var extraRateClearedOnFocus by remember { mutableStateOf(false) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }
    var attemptedSave by remember { mutableStateOf(false) }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    val cities by viewModel.cities.collectAsState()
    val states by viewModel.states.collectAsState()
    val certifications by viewModel.certifications.collectAsState()

    LaunchedEffect(email) { viewModel.loadCaregiverPublic(email) }
    val profile by viewModel.selectedCaregiver.collectAsState()
    LaunchedEffect(profile) {
        profile?.let {
            experience = it.experienceYears
            certs = it.certifications
            availabilityStart = it.availabilityStart.ifBlank { it.availability.substringBefore("-", "") }
            availabilityEnd = it.availabilityEnd.ifBlank { it.availability.substringAfter("-", "") }
            availabilityExceptions = it.availabilityExceptions
            rate = it.hourlyRate.toEditableMoney()
            extraRate = it.extraChildRate.toEditableMoney()
            rateClearedOnFocus = false
            extraRateClearedOnFocus = false
            city = it.city
            state = it.state
        }
    }

    val baseRate = rate.toDoubleOrNull()
    val extraChildRate = extraRate.toDoubleOrNull() ?: 0.0
    val formValid = certs.isNotBlank() &&
        availabilityStart.isNotBlank() &&
        availabilityEnd.isNotBlank() &&
        baseRate != null &&
        city.isNotBlank() &&
        state.isNotBlank()

    NanysScaffold(title = "Mi perfil", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            profile?.let {
                Text(it.fullName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                RatingStars(it.averageRating)
                Text("${it.reviewCount} reseñas")
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                experience.toString(),
                { experience = it.filter(Char::isDigit).take(2).toIntOrNull() ?: 0 },
                label = { Text("Años experiencia") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            DropdownField("Certificación", certs, certifications) { certs = it }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { showStartPicker = true }, modifier = Modifier.weight(1f)) {
                    ButtonIcon(Icons.Default.Schedule, "Inicio $availabilityStart")
                }
                OutlinedButton(onClick = { showEndPicker = true }, modifier = Modifier.weight(1f)) {
                    ButtonIcon(Icons.Default.Schedule, "Fin $availabilityEnd")
                }
            }
            OutlinedTextField(
                availabilityExceptions,
                { availabilityExceptions = it },
                label = { Text("Excepciones de disponibilidad") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                rate,
                { rate = sanitizeDecimalInput(it) },
                label = { Text("Tarifa/hora") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused && !rateClearedOnFocus) {
                            rate = ""
                            rateClearedOnFocus = true
                        }
                    },
                isError = attemptedSave && baseRate == null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                extraRate,
                { extraRate = sanitizeDecimalInput(it) },
                label = { Text("Aumento por niño extra/hora") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused && !extraRateClearedOnFocus) {
                            extraRate = ""
                            extraRateClearedOnFocus = true
                        }
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            DropdownField("Ciudad", city, cities) { city = it }
            DropdownField("Estado", state, states) { state = it }
            if (attemptedSave && !formValid) {
                Text(
                    "Completa certificación, horario, tarifa, ciudad y estado.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Button(
                onClick = {
                    attemptedSave = true
                    if (formValid) {
                        val availability = "$availabilityStart-$availabilityEnd"
                        viewModel.updateCaregiverProfile(
                            CaregiverProfileEntity(
                                email = email,
                                photoUri = profile?.photoUri ?: "default",
                                experienceYears = experience,
                                certifications = certs,
                                availability = availability,
                                availabilityStart = availabilityStart,
                                availabilityEnd = availabilityEnd,
                                availabilityExceptions = availabilityExceptions.trim(),
                                hourlyRate = baseRate ?: 0.0,
                                extraChildRate = extraChildRate,
                                city = city,
                                state = state,
                                verified = profile?.verified ?: false
                            ),
                            onDone = onSaved
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                ButtonIcon(Icons.Default.Save, "Guardar perfil")
            }
        }
    }

    if (showStartPicker) {
        TimePickerDialog(
            initialValue = availabilityStart,
            onDismiss = { showStartPicker = false },
            onConfirm = {
                availabilityStart = it
                showStartPicker = false
            }
        )
    }
    if (showEndPicker) {
        TimePickerDialog(
            initialValue = availabilityEnd,
            onDismiss = { showEndPicker = false },
            onConfirm = {
                availabilityEnd = it
                showEndPicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val (initialHour, initialMinute) = parseTime(initialValue)
    val state = rememberTimePickerState(initialHour = initialHour, initialMinute = initialMinute)
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(String.format(Locale.US, "%02d:%02d", state.hour, state.minute)) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        },
        text = { TimePicker(state) }
    )
}

private fun parseTime(value: String): Pair<Int, Int> {
    val parts = value.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 8
    val minute = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
    return hour to minute
}

private fun sanitizeDecimalInput(input: String): String {
    val normalized = input.replace(',', '.').filter { it.isDigit() || it == '.' }
    val dot = normalized.indexOf('.')
    if (dot == -1) return normalized.take(5)
    val integer = normalized.take(dot).take(5)
    val decimals = normalized.drop(dot + 1).replace(".", "").take(2)
    return "$integer.$decimals"
}

private fun Double.toEditableMoney(): String {
    if (this == 0.0) return ""
    return String.format(Locale.US, "%.2f", this).trimEnd('0').trimEnd('.')
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
            Button(onClick = { viewModel.addPrivateNote(tutorEmail, noteText, rating) }) {
                ButtonIcon(Icons.Default.Save, "Guardar nota")
            }
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
