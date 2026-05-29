package com.nanys.care.presentation.tutor

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
import androidx.compose.ui.text.font.FontWeight
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
    val searchResults by viewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.userEmail?.let { viewModel.loadBookingsForTutor(it) }
        viewModel.loadTopCaregivers()
    }

    NanysScaffold(
        title = "Panel Tutor",
        onProfileClick = { onNavigate("tutor_profile") },
        onMessagesClick = { onNavigate("chat_list") },
        onSettingsClick = { onNavigate("settings") },
        onLogout = onLogout
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            StatCard("Próximas reservas", "$upcoming", Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Text("Cuidadores recomendados", fontWeight = FontWeight.Bold)
            searchResults.take(3).forEach { cg ->
                CaregiverListItem(cg, onClick = { onNavigate("caregiver_public/${cg.email}") })
            }
            Spacer(Modifier.height(8.dp))
            NavigationChip("Buscar cuidadores", { onNavigate("tutor_search") })
            NavigationChip("Mis reservas", { onNavigate("tutor_bookings") })
            NavigationChip("Agenda", { onNavigate("tutor_agenda") })
            NavigationChip("Chat", { onNavigate("chat_list") })
            NavigationChip("Mi perfil", { onNavigate("tutor_profile") })
        }
    }
}

@Composable
private fun NavigationChip(text: String, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp)) {
        Text(text, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CaregiverListItem(cg: CaregiverProfile, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(Modifier.weight(1f)) {
                Text(cg.fullName, fontWeight = FontWeight.SemiBold)
                RatingStars(cg.averageRating)
                Text("$${cg.hourlyRate}/h · ${cg.city}")
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
            ) { Text("Buscar") }
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
            Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
                Text(cg.fullName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                RatingStars(cg.averageRating)
                Text("${cg.reviewCount} reseñas · ${if (cg.verified) "Verificado ✓" else "No verificado"}")
                Spacer(Modifier.height(8.dp))
                Text("📍 ${cg.city}, ${cg.state}")
                Text("💼 ${cg.experienceYears} años de experiencia")
                Text("💰 $${cg.hourlyRate}/hora")
                Text("📅 ${cg.availability}")
                Text("🎓 ${cg.certifications}")
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onChat, modifier = Modifier.weight(1f)) { Text("Contactar") }
                    Button(onClick = onBook, modifier = Modifier.weight(1f)) { Text("Solicitar cita") }
                }
            }
        }
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
    val total = hourlyRate * duration
    val children = tutorProfile?.children.orEmpty()
    val childIdSet = children.map { it.id }.toSet()
    val validSelectedChildIds = selectedChildIds.intersect(childIdSet)

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
                Text("Total estimado: $${String.format("%.2f", total)}", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = {
                    viewModel.createBooking(caregiverEmail, date, hour, minute, duration, location, validSelectedChildIds.toList(), notes, hourlyRate)
                    showPaymentDialog = true
                },
                enabled = validSelectedChildIds.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Confirmar reserva") }
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
                        TextButton(onClick = { viewModel.completeBooking(b.id) }) { Text("Marcar completada") }
                    }
                    if (b.status == BookingStatus.COMPLETED) {
                        TextButton(onClick = { onReview(b.id) }) { Text("Calificar cuidador") }
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
            }) { Text("Enviar reseña") }
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
fun TutorProfileScreen(viewModel: NanysViewModel, onBack: () -> Unit) {
    val email = viewModel.userEmail ?: return
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var preferences by remember { mutableStateOf("") }
    var childName by remember { mutableStateOf("") }
    var childAge by remember { mutableStateOf("") }
    var childNeeds by remember { mutableStateOf("") }
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
            Button(onClick = { viewModel.updateTutorProfile(TutorProfileEntity(email, city, state, notes, preferences)) }, modifier = Modifier.fillMaxWidth()) { Text("Guardar") }
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
            OutlinedTextField(childName, { childName = it }, label = { Text("Nombre hijo/a") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(childAge, { childAge = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(childNeeds, { childNeeds = it }, label = { Text("Necesidades especiales") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                viewModel.saveChild(ChildEntity(tutorEmail = email, name = childName, age = childAge.toIntOrNull() ?: 0, specialNeeds = childNeeds))
                childName = ""; childAge = ""; childNeeds = ""
            }) { Text("Agregar hijo/a") }
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
