package com.nanys.care.presentation.tutor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nanys.care.data.local.entity.ChildEntity
import com.nanys.care.data.local.entity.TutorProfileEntity
import com.nanys.care.domain.model.BookingStatus
import com.nanys.care.domain.model.CaregiverProfile
import com.nanys.care.domain.model.CaregiverSearchFilter
import com.nanys.care.presentation.caregiver.DropdownField
import com.nanys.care.presentation.common.*
import com.nanys.care.presentation.viewmodel.NanysViewModel
import java.time.LocalDate

@Composable
fun TutorDashboardScreen(viewModel: NanysViewModel, onNavigate: (String) -> Unit, onLogout: () -> Unit) {
    val bookings by viewModel.bookings.collectAsState()
    val upcoming = bookings.count { it.status == BookingStatus.ACCEPTED || it.status == BookingStatus.PENDING }
    val pending = bookings.count { it.status == BookingStatus.PENDING }
    val accepted = bookings.count { it.status == BookingStatus.ACCEPTED }
    val searchResults by viewModel.searchResults.collectAsState()
    val appointmentCaregivers = bookings
        .filter { it.status == BookingStatus.ACCEPTED || it.status == BookingStatus.PENDING }
        .distinctBy { it.caregiverEmail }

    LaunchedEffect(Unit) {
        viewModel.userEmail?.let { viewModel.loadBookingsForTutor(it) }
        viewModel.loadTopCaregivers()
    }

    NanysScaffold(
        title = "Inicio",
        onProfileClick = { onNavigate("tutor_profile") },
        onMessagesClick = { onNavigate("chat_list") },
        onSettingsClick = { onNavigate("settings") },
        onLogout = onLogout
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            SearchShortcut(onClick = { onNavigate("tutor_search") })
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Reservas activas", "$upcoming", Modifier.weight(1f))
                StatCard("Pendientes", "$pending", Modifier.weight(1f))
                StatCard("Aceptadas", "$accepted", Modifier.weight(1f))
            }
            Spacer(Modifier.height(16.dp))
            Text("Cuidadores con cita", fontWeight = FontWeight.Bold)
            if (appointmentCaregivers.isEmpty()) {
                Text("Sin citas activas", style = MaterialTheme.typography.bodySmall)
            } else {
                appointmentCaregivers.forEach { booking ->
                    AppointmentCaregiverCard(booking.caregiverName.ifBlank { booking.caregiverEmail }, booking.date, booking.timeSlot) {
                        onNavigate("caregiver_public/${booking.caregiverEmail}")
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Cuidadores recomendados", fontWeight = FontWeight.Bold)
            searchResults.take(2).forEach { cg ->
                CaregiverListItem(cg, onClick = { onNavigate("caregiver_public/${cg.email}") })
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                DashboardGridCard("Mis reservas", Icons.AutoMirrored.Filled.EventNote, Modifier.weight(1f), "$upcoming activas") { onNavigate("tutor_bookings") }
                DashboardGridCard("Agenda", Icons.Default.CalendarMonth, Modifier.weight(1f), "Calendario") { onNavigate("tutor_agenda") }
            }
            Row(Modifier.fillMaxWidth()) {
                DashboardGridCard("Chat", Icons.AutoMirrored.Filled.Chat, Modifier.weight(1f), "Mensajes") { onNavigate("chat_list") }
                DashboardGridCard("Mi perfil", Icons.Default.Person, Modifier.weight(1f), "Familia e hijos") { onNavigate("tutor_profile") }
            }
        }
    }
}

@Composable
private fun SearchShortcut(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary)
            Text("Buscar cuidadores", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f))
        }
    }
}

@Composable
private fun AppointmentCaregiverCard(name: String, date: String, timeSlot: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(8.dp)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.EventAvailable, null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(name, fontWeight = FontWeight.SemiBold)
                Text("$date · $timeSlot", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun NavigationChip(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(text, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CaregiverListItem(cg: CaregiverProfile, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(8.dp)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(Icons.Default.ChildCare, null, tint = MaterialTheme.colorScheme.primary)
            Column(Modifier.weight(1f)) {
                Text(cg.fullName, fontWeight = FontWeight.SemiBold)
                RatingStars(cg.averageRating)
                Text("$${cg.hourlyRate}/h · ${cg.city}")
                Text("Niño extra: $${String.format("%.2f", cg.extraChildRate)}/h", style = MaterialTheme.typography.bodySmall)
                Text("${cg.experienceYears} años exp.")
            }
            if (cg.verified) AssistChip(onClick = {}, label = { Text("✓ Verificado") })
        }
    }
}

@Composable
fun SearchCaregiversScreen(viewModel: NanysViewModel, onBack: () -> Unit, onOpenProfile: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var minPrice by remember { mutableStateOf(0f) }
    var maxPrice by remember { mutableStateOf(500f) }
    var minExp by remember { mutableStateOf(0f) }
    var minRating by remember { mutableStateOf(0f) }
    var availability by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    val results by viewModel.searchResults.collectAsState()
    val cities by viewModel.cities.collectAsState()
    val states by viewModel.states.collectAsState()

    NanysScaffold(title = "Buscar cuidadores", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(query, { query = it }, label = { Text("Buscar") }, modifier = Modifier.fillMaxWidth(), trailingIcon = {
                IconButton(onClick = { showFilters = !showFilters }) { Icon(Icons.Default.FilterList, "Filtros") }
            })
            if (showFilters) {
                DropdownField("Ciudad", city, listOf("") + cities) { city = it }
                DropdownField("Estado", state, listOf("") + states) { state = it }
                Text("Precio: $${minPrice.toInt()} - $${maxPrice.toInt()}")
                RangeSlider(
                    value = minPrice..maxPrice,
                    onValueChange = { range -> minPrice = range.start; maxPrice = range.endInclusive },
                    valueRange = 0f..500f
                )
                Text("Experiencia mín: ${minExp.toInt()} años")
                Slider(minExp, { minExp = it }, valueRange = 0f..10f, steps = 10)
                Text("Calificación mín: ${minRating.toInt()}")
                Slider(minRating, { minRating = it }, valueRange = 0f..5f, steps = 5)
                OutlinedTextField(availability, { availability = it }, label = { Text("Disponibilidad") }, modifier = Modifier.fillMaxWidth())
            }
            Button(
                onClick = {
                    viewModel.searchCaregivers(
                        CaregiverSearchFilter(city, state, minPrice.toDouble(), maxPrice.toDouble(), minExp.toInt(), minRating, availability, query)
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                ButtonIcon(Icons.Default.Search, "Buscar")
            }
            LazyColumn {
                items(results) { cg ->
                    CaregiverListItem(cg) { onOpenProfile(cg.email) }
                }
            }
        }
    }
}

@Composable
fun CaregiverPublicProfileScreen(viewModel: NanysViewModel, email: String, onBack: () -> Unit, onBook: () -> Unit, onChat: () -> Unit) {
    LaunchedEffect(email) { viewModel.loadCaregiverPublic(email) }
    val profile by viewModel.selectedCaregiver.collectAsState()

    NanysScaffold(title = "Perfil cuidador", onLogout = onBack, showProfileMenu = false) { padding ->
        profile?.let { cg ->
            val availability = availabilityText(cg)
            Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
                Text(cg.fullName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                RatingStars(cg.averageRating)
                Text("${cg.reviewCount} reseñas · ${if (cg.verified) "Verificado ✓" else "No verificado"}")
                Spacer(Modifier.height(8.dp))
                ProfileInfoRow(Icons.Default.Place, "${cg.city}, ${cg.state}")
                ProfileInfoRow(Icons.Default.Work, "${cg.experienceYears} años de experiencia")
                ProfileInfoRow(Icons.Default.Payments, "$${String.format("%.2f", cg.hourlyRate)}/hora")
                ProfileInfoRow(Icons.Default.ChildCare, "Niño extra: $${String.format("%.2f", cg.extraChildRate)}/hora")
                ProfileInfoRow(Icons.Default.Schedule, availability)
                if (cg.availabilityExceptions.isNotBlank()) {
                    ProfileInfoRow(Icons.Default.EventBusy, "Excepciones: ${cg.availabilityExceptions}")
                }
                ProfileInfoRow(Icons.Default.School, cg.certifications)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onChat, modifier = Modifier.weight(1f)) {
                        ButtonIcon(Icons.Default.Email, "Contactar")
                    }
                    Button(onClick = onBook, modifier = Modifier.weight(1f)) {
                        ButtonIcon(Icons.Default.CalendarMonth, "Solicitar cita")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun availabilityText(cg: CaregiverProfile): String {
    return if (cg.availabilityStart.isNotBlank() && cg.availabilityEnd.isNotBlank()) {
        "${cg.availabilityStart} a ${cg.availabilityEnd}"
    } else {
        cg.availability.ifBlank { "Sin disponibilidad registrada" }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(viewModel: NanysViewModel, caregiverEmail: String, onBack: () -> Unit, onDone: () -> Unit) {
    val profile by viewModel.selectedCaregiver.collectAsState()
    val tutorProfile by viewModel.selectedTutor.collectAsState()
    var date by remember { mutableStateOf(LocalDate.now().plusDays(1).toString()) }
    var hour by remember { mutableIntStateOf(10) }
    var minute by remember { mutableIntStateOf(0) }
    var duration by remember { mutableIntStateOf(1) }
    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedChildIds by remember { mutableStateOf(setOf<Long>()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }

    LaunchedEffect(caregiverEmail) { viewModel.loadCaregiverPublic(caregiverEmail) }
    LaunchedEffect(viewModel.userEmail) { viewModel.userEmail?.let { viewModel.loadTutorPrivate(it) } }

    val hourlyRate = profile?.hourlyRate ?: 0.0
    val extraChildRate = profile?.extraChildRate ?: 0.0
    val children = tutorProfile?.children.orEmpty()
    val childIdSet = children.map { it.id }.toSet()
    val validSelectedChildIds = selectedChildIds.intersect(childIdSet)
    val extraChildren = (validSelectedChildIds.size - 1).coerceAtLeast(0)
    val total = (hourlyRate + extraChildren * extraChildRate) * duration

    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false; onDone() },
            title = { Text("Pago simulado") },
            text = { Text("Pago simulado de $${String.format("%.2f", total)} – Reserva confirmada") },
            confirmButton = { TextButton(onClick = { showPaymentDialog = false; onDone() }) { Text("OK") } }
        )
    }

    NanysScaffold(title = "Agendar cita", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text("Con: ${profile?.fullName ?: caregiverEmail}", fontWeight = FontWeight.Bold)
            OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) { Text("Fecha: $date") }
            OutlinedButton(onClick = { showTimePicker = true }, modifier = Modifier.fillMaxWidth()) { Text("Hora: ${"%02d".format(hour)}:${"%02d".format(minute)}") }
            Text("Duración (horas)")
            Row { (1..4).forEach { h ->
                FilterChip(selected = duration == h, onClick = { duration = h }, label = { Text("${h}h") })
                Spacer(Modifier.width(4.dp))
            }}
            OutlinedTextField(location, { location = it }, label = { Text("Ubicación (dirección)") }, modifier = Modifier.fillMaxWidth())
            Text("Hijos/as", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp))
            if (children.isEmpty()) {
                Text(
                    "Agrega al menos un hijo/a en tu perfil antes de reservar.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            children.forEach { child ->
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(
                        checked = child.id in validSelectedChildIds,
                        onCheckedChange = { checked ->
                            selectedChildIds = if (checked) selectedChildIds + child.id else selectedChildIds - child.id
                        }
                    )
                    Text("${child.name} (${child.age} años)")
                }
            }
            if (children.isNotEmpty() && validSelectedChildIds.isEmpty()) {
                Text(
                    "Selecciona al menos un hijo/a.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            OutlinedTextField(notes, { notes = it }, label = { Text("Notas adicionales") }, modifier = Modifier.fillMaxWidth())
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("Tarifa base: $${String.format("%.2f", hourlyRate)}/h", fontWeight = FontWeight.SemiBold)
                    Text("Aumento por niño extra: $${String.format("%.2f", extraChildRate)}/h")
                    Text("Total estimado: $${String.format("%.2f", total)}", fontWeight = FontWeight.Bold)
                }
            }
            Button(
                onClick = {
                    viewModel.createBooking(caregiverEmail, date, hour, minute, duration, location, validSelectedChildIds.toList(), notes, hourlyRate)
                    showPaymentDialog = true
                },
                enabled = validSelectedChildIds.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                ButtonIcon(Icons.Default.Check, "Confirmar reserva")
            }
        }
    }

    if (showDatePicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        date = java.time.Instant.ofEpochMilli(millis).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state) }
    }
    if (showTimePicker) {
        val state = rememberTimePickerState(hour, minute)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = { hour = state.hour; minute = state.minute; showTimePicker = false }) { Text("OK") }
            },
            text = { TimePicker(state) }
        )
    }
}

@Composable
fun TutorBookingsScreen(viewModel: NanysViewModel, onBack: () -> Unit, onReview: (Long) -> Unit) {
    val bookings by viewModel.bookings.collectAsState()
    var tab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Pendiente", "Aceptada", "Rechazada", "Completada")
    val statuses = listOf(BookingStatus.PENDING, BookingStatus.ACCEPTED, BookingStatus.REJECTED, BookingStatus.COMPLETED)
    val filtered = bookings.filter { it.status == statuses[tab] }

    NanysScaffold(title = "Mis reservas", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding)) {
            TabRow(selectedTabIndex = tab) {
                tabs.forEachIndexed { i, t -> Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t, maxLines = 1) }) }
            }
            LazyColumn(Modifier.padding(16.dp)) {
                items(filtered) { b ->
                    BookingCard(b)
                    if (b.status == BookingStatus.ACCEPTED) {
                        TextButton(onClick = { viewModel.completeBooking(b.id) }) {
                            ButtonIcon(Icons.Default.Check, "Marcar completada")
                        }
                    }
                    if (b.status == BookingStatus.COMPLETED) {
                        TextButton(onClick = { onReview(b.id) }) {
                            ButtonIcon(Icons.Default.Star, "Calificar cuidador")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubmitReviewScreen(viewModel: NanysViewModel, bookingId: Long, onBack: () -> Unit) {
    val booking = viewModel.bookings.collectAsState().value.find { it.id == bookingId }
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    NanysScaffold(title = "Calificar", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Calificación: $rating")
            Slider(rating.toFloat(), { rating = it.toInt() }, valueRange = 1f..5f, steps = 4)
            OutlinedTextField(comment, { comment = it }, label = { Text("Comentario") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                booking?.let { viewModel.submitReview(bookingId, it.caregiverEmail, rating, comment) }
                onBack()
            }) {
                ButtonIcon(Icons.Default.Star, "Enviar reseña")
            }
        }
    }
}

@Composable
fun TutorAgendaScreen(viewModel: NanysViewModel, onBack: () -> Unit) {
    val bookings by viewModel.bookings.collectAsState()
    NanysScaffold(title = "Agenda", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            VisualCalendarSection(bookings)
        }
    }
}

@Composable
fun TutorProfileScreen(
    viewModel: NanysViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit = onBack
) {
    val email = viewModel.userEmail ?: return
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var preferences by remember { mutableStateOf("") }
    var childName by remember { mutableStateOf("") }
    var childAge by remember { mutableStateOf("") }
    var childNeeds by remember { mutableStateOf("") }
    var childError by remember { mutableStateOf("") }
    val cities by viewModel.cities.collectAsState()
    val states by viewModel.states.collectAsState()

    LaunchedEffect(email) { viewModel.loadTutorPrivate(email) }
    val profile by viewModel.selectedTutor.collectAsState()
    LaunchedEffect(profile) {
        profile?.let { city = it.city; state = it.state; notes = it.notes; preferences = it.preferences }
    }

    NanysScaffold(title = "Mi perfil", onLogout = onBack, showProfileMenu = false) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text(profile?.fullName ?: "", style = MaterialTheme.typography.headlineSmall)
            DropdownField("Ciudad", city, cities) { city = it }
            DropdownField("Estado", state, states) { state = it }
            OutlinedTextField(notes, { notes = it }, label = { Text("Notas personales") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(preferences, { preferences = it }, label = { Text("Preferencias") }, modifier = Modifier.fillMaxWidth())
            Button(
                onClick = {
                    viewModel.updateTutorProfile(
                        TutorProfileEntity(email, city, state, notes, preferences),
                        onDone = onSaved
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                ButtonIcon(Icons.Default.Save, "Guardar")
            }
            HorizontalDivider(Modifier.padding(vertical = 16.dp))
            Text("Hijos", fontWeight = FontWeight.Bold)
            profile?.children?.forEach { c ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text(c.name); Text("${c.age} años · ${c.specialNeeds}") }
                        IconButton(onClick = { viewModel.deleteChild(c.id) }) { Icon(Icons.Default.Delete, null) }
                    }
                }
            }
            OutlinedTextField(
                childName,
                {
                    childName = it
                    childError = ""
                },
                label = { Text("Nombre hijo/a") },
                modifier = Modifier.fillMaxWidth(),
                isError = childError.isNotBlank() && childName.isBlank()
            )
            OutlinedTextField(
                childAge,
                {
                    childAge = it.filter(Char::isDigit).take(2)
                    childError = ""
                },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth(),
                isError = childError.isNotBlank() && (childAge.toIntOrNull() ?: 0) <= 0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(childNeeds, { childNeeds = it }, label = { Text("Necesidades especiales") }, modifier = Modifier.fillMaxWidth())
            if (childError.isNotBlank()) {
                Text(childError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Button(onClick = {
                val parsedAge = childAge.toIntOrNull() ?: 0
                if (childName.isBlank() || parsedAge <= 0) {
                    childError = "El nombre y la edad son obligatorios."
                } else {
                    viewModel.saveChild(ChildEntity(tutorEmail = email, name = childName, age = parsedAge, specialNeeds = childNeeds))
                    childName = ""; childAge = ""; childNeeds = ""; childError = ""
                }
            }) {
                ButtonIcon(Icons.Default.Add, "Agregar hijo/a")
            }
        }
    }
}

@Composable
fun TutorPrivateProfileScreen(viewModel: NanysViewModel, email: String, onBack: () -> Unit) {
    LaunchedEffect(email) { viewModel.loadTutorPrivate(email) }
    val profile by viewModel.selectedTutor.collectAsState()
    NanysScaffold(title = "Perfil tutor", onLogout = onBack, showProfileMenu = false) { padding ->
        profile?.let { t ->
            Column(Modifier.padding(padding).padding(16.dp)) {
                Text(t.fullName, fontWeight = FontWeight.Bold)
                Text("📍 ${t.city}, ${t.state}")
                Text("📝 Notas: ${t.notes}")
                Text("Preferencias: ${t.preferences}")
                t.children.forEach { c -> Text("👶 ${c.name}, ${c.age} años — ${c.specialNeeds}") }
            }
        }
    }
}
