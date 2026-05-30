package com.nanys.care.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nanys.care.core.di.AppContainer
import com.nanys.care.data.local.entity.CaregiverProfileEntity
import com.nanys.care.data.local.entity.CatalogItemEntity
import com.nanys.care.data.local.entity.ChildEntity
import com.nanys.care.data.local.entity.PrivateNoteEntity
import com.nanys.care.data.local.entity.ReviewEntity
import com.nanys.care.data.local.entity.TutorProfileEntity
import com.nanys.care.data.mapper.toDomain
import com.nanys.care.domain.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NanysViewModel(private val container: AppContainer) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchResults = MutableStateFlow<List<CaregiverProfile>>(emptyList())
    val searchResults: StateFlow<List<CaregiverProfile>> = _searchResults.asStateFlow()

    private val _selectedCaregiver = MutableStateFlow<CaregiverProfile?>(null)
    val selectedCaregiver: StateFlow<CaregiverProfile?> = _selectedCaregiver.asStateFlow()

    private val _selectedTutor = MutableStateFlow<TutorProfile?>(null)
    val selectedTutor: StateFlow<TutorProfile?> = _selectedTutor.asStateFlow()

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()
    private var bookingsJob: Job? = null

    private val _conversations = MutableStateFlow<List<ConversationSummary>>(emptyList())
    val conversations: StateFlow<List<ConversationSummary>> = _conversations.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _caregivers = MutableStateFlow<List<CaregiverProfile>>(emptyList())
    val caregivers: StateFlow<List<CaregiverProfile>> = _caregivers.asStateFlow()

    private val _catalogItems = MutableStateFlow<List<CatalogItem>>(emptyList())
    val catalogItems: StateFlow<List<CatalogItem>> = _catalogItems.asStateFlow()

    private val _privateNotes = MutableStateFlow<List<PrivateNote>>(emptyList())
    val privateNotes: StateFlow<List<PrivateNote>> = _privateNotes.asStateFlow()

    private val _cities = MutableStateFlow<List<String>>(emptyList())
    val cities: StateFlow<List<String>> = _cities.asStateFlow()

    private val _states = MutableStateFlow<List<String>>(emptyList())
    val states: StateFlow<List<String>> = _states.asStateFlow()

    private val _adminStats = MutableStateFlow(0 to 0)
    val adminStats: StateFlow<Pair<Int, Int>> = _adminStats.asStateFlow()

    private val _verifiedCount = MutableStateFlow(0)
    val verifiedCount: StateFlow<Int> = _verifiedCount.asStateFlow()

    private val _todayMessages = MutableStateFlow(0)
    val todayMessages: StateFlow<Int> = _todayMessages.asStateFlow()

    val isLoggedIn: Boolean get() = container.sessionManager.isLoggedIn
    val userRole: UserRole? get() = container.sessionManager.userRole
    val userEmail: String? get() = container.sessionManager.userEmail

    init {
        refreshSession()
        loadCatalogLocations()
    }

    fun refreshSession() {
        viewModelScope.launch {
            val email = container.sessionManager.userEmail
            _currentUser.value = email?.let { container.userRepository.getUser(it) }
            email?.let { loadRoleData(it) }
        }
    }

    private suspend fun loadRoleData(email: String) {
        when (container.sessionManager.userRole) {
            UserRole.CUIDADOR -> {
                loadBookingsForCaregiver(email)
                loadConversations(email)
                loadPrivateNotes(email)
            }
            UserRole.TUTOR -> {
                loadBookingsForTutor(email)
                loadConversations(email)
            }
            UserRole.ADMIN -> loadCaregivers()
            UserRole.SUPERVISOR -> {
                loadCaregivers()
                loadAllConversations()
            }
            null -> Unit
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            container.authRepository.login(email, password)
                .onSuccess {
                    _currentUser.value = it
                    loadRoleData(it.email)
                    onSuccess()
                }
                .onFailure { _error.value = it.message }
        }
    }

    fun register(email: String, password: String, role: UserRole, fullName: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            container.authRepository.register(email, password, role, fullName)
                .onSuccess {
                    _currentUser.value = it
                    onSuccess()
                }
                .onFailure { _error.value = it.message }
        }
    }

    fun logout(onDone: () -> Unit) {
        container.authRepository.logout()
        _currentUser.value = null
        onDone()
    }

    fun clearError() { _error.value = null }

    fun searchCaregivers(filter: CaregiverSearchFilter) {
        viewModelScope.launch {
            _searchResults.value = container.caregiverRepository.searchCaregivers(filter)
        }
    }

    fun loadCaregiverPublic(email: String) {
        viewModelScope.launch {
            _selectedCaregiver.value = container.caregiverRepository.getCaregiverPublic(email)
        }
    }

    fun loadTutorPrivate(email: String) {
        viewModelScope.launch {
            _selectedTutor.value = container.tutorRepository.getTutorPrivate(email)
        }
    }

    fun loadBookingsForCaregiver(email: String) {
        bookingsJob?.cancel()
        bookingsJob = viewModelScope.launch {
            container.bookingRepository.observeByCaregiver(email).collect { list ->
                _bookings.value = list.map { container.bookingRepository.enrichBooking(it) }
            }
        }
    }

    fun loadBookingsForTutor(email: String) {
        bookingsJob?.cancel()
        bookingsJob = viewModelScope.launch {
            container.bookingRepository.observeByTutor(email).collect { list ->
                _bookings.value = list.map { container.bookingRepository.enrichBooking(it) }
            }
        }
    }

    fun respondBooking(id: Long, accept: Boolean) {
        viewModelScope.launch {
            container.bookingRepository.respondBooking(id, accept)
            userEmail?.let { loadRoleData(it) }
        }
    }

    fun createBooking(
        caregiverEmail: String,
        date: String,
        hour: Int,
        minute: Int,
        duration: Int,
        location: String,
        childIds: List<Long>,
        notes: String,
        hourlyRate: Double
    ) {
        val tutor = userEmail ?: return
        if (childIds.isEmpty()) {
            _error.value = "Selecciona al menos un hijo/a"
            return
        }
        viewModelScope.launch {
            container.bookingRepository.createBooking(
                tutor, caregiverEmail, date, hour, minute, duration, location, childIds, notes, hourlyRate
            )
            loadBookingsForTutor(tutor)
        }
    }

    fun completeBooking(id: Long) {
        viewModelScope.launch {
            container.bookingRepository.completeBooking(id)
            userEmail?.let { loadRoleData(it) }
        }
    }

    fun submitReview(bookingId: Long, caregiverEmail: String, rating: Int, comment: String) {
        val tutor = userEmail ?: return
        viewModelScope.launch {
            container.reviewRepository.submitReview(
                ReviewEntity(fromTutorEmail = tutor, toCaregiverEmail = caregiverEmail, bookingId = bookingId, rating = rating, comment = comment)
            )
        }
    }

    fun loadConversations(email: String) {
        viewModelScope.launch {
            container.chatRepository.conversationsForUser(email).collect {
                _conversations.value = it
            }
        }
    }

    fun loadAllConversations() {
        viewModelScope.launch {
            container.chatRepository.observeAllMessages().collect { msgs ->
                val grouped = msgs.groupBy {
                    listOf(it.senderEmail, it.receiverEmail).sorted().joinToString("|")
                }
                _conversations.value = grouped.mapNotNull { (pairKey, list) ->
                    val last = list.maxByOrNull { it.timestamp } ?: return@mapNotNull null
                    val emails = pairKey.split("|")
                    val nameA = container.userRepository.getUser(emails.getOrElse(0) { "" })?.fullName ?: emails.getOrElse(0) { "" }
                    val nameB = container.userRepository.getUser(emails.getOrElse(1) { "" })?.fullName ?: emails.getOrElse(1) { "" }
                    ConversationSummary(
                        otherEmail = pairKey,
                        otherName = "$nameA ↔ $nameB",
                        lastMessage = last.content,
                        lastTimestamp = last.timestamp
                    )
                }.sortedByDescending { it.lastTimestamp }
            }
        }
    }

    fun loadMessages(myEmail: String, otherEmail: String) {
        viewModelScope.launch {
            container.chatRepository.observeConversation(myEmail, otherEmail).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(receiver: String, content: String) {
        val sender = userEmail ?: return
        viewModelScope.launch {
            container.chatRepository.sendMessage(sender, receiver, content)
            loadMessages(sender, receiver)
        }
    }

    fun flagMessage(id: Long) {
        viewModelScope.launch {
            container.chatRepository.flagMessage(id)
            container.simulationService.showToast("Mensaje marcado como inapropiado")
        }
    }

    fun updateCaregiverProfile(profile: CaregiverProfileEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            container.caregiverRepository.updateProfile(profile)
            userEmail?.let { loadCaregiverPublic(it) }
            onDone()
        }
    }

    fun updateTutorProfile(profile: TutorProfileEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            container.tutorRepository.saveProfile(profile)
            userEmail?.let { loadTutorPrivate(it) }
            onDone()
        }
    }

    fun saveChild(child: ChildEntity) {
        viewModelScope.launch {
            container.tutorRepository.saveChild(child)
            userEmail?.let { loadTutorPrivate(it) }
        }
    }

    fun deleteChild(id: Long) {
        viewModelScope.launch {
            container.tutorRepository.deleteChild(id)
            userEmail?.let { loadTutorPrivate(it) }
        }
    }

    fun loadCaregivers() {
        viewModelScope.launch {
            container.caregiverRepository.observeAllProfiles().collect {
                _caregivers.value = it
            }
        }
    }

    fun setCaregiverVerified(email: String, verified: Boolean) {
        viewModelScope.launch {
            container.caregiverRepository.setVerified(email, verified)
            loadCaregivers()
        }
    }

    fun loadCatalog(category: String) {
        viewModelScope.launch {
            container.catalogRepository.observeCategory(category).collect {
                _catalogItems.value = it
            }
        }
    }

    fun addCatalogItem(category: String, name: String) {
        viewModelScope.launch {
            container.catalogRepository.add(CatalogItemEntity(category = category, name = name, value = name))
            loadCatalog(category)
        }
    }

    fun deleteCatalogItem(item: CatalogItem) {
        viewModelScope.launch {
            container.catalogRepository.delete(
                CatalogItemEntity(id = item.id, category = item.category, name = item.name, value = item.value, extra = item.extra)
            )
            loadCatalog(item.category)
        }
    }

    fun loadPrivateNotes(email: String) {
        viewModelScope.launch {
            container.privateNoteRepository.observeByCaregiver(email).collect {
                _privateNotes.value = it
            }
        }
    }

    fun addPrivateNote(tutorEmail: String, note: String, rating: Int) {
        val caregiver = userEmail ?: return
        viewModelScope.launch {
            container.privateNoteRepository.add(
                PrivateNoteEntity(caregiverEmail = caregiver, tutorEmail = tutorEmail, note = note, rating = rating)
            )
        }
    }

    private fun loadCatalogLocations() {
        viewModelScope.launch {
            val cities = container.catalogRepository.observeCategory("city").first()
            val states = container.catalogRepository.observeCategory("state").first()
            _cities.value = cities.map { it.name }
            _states.value = states.map { it.name }
        }
    }

    fun loadAdminStats() {
        viewModelScope.launch {
            combine(
                container.db.userDao().countByRole("CUIDADOR"),
                container.db.userDao().countByRole("TUTOR")
            ) { c, t -> c to t }.collect { _adminStats.value = it }
        }
    }

    fun loadSupervisorStats() {
        viewModelScope.launch {
            container.caregiverRepository.countVerified().collect { _verifiedCount.value = it }
            container.chatRepository.countTodayConversations().collect { _todayMessages.value = it }
        }
    }

    fun loadTopCaregivers() {
        viewModelScope.launch {
            _searchResults.value = container.caregiverRepository.searchCaregivers(CaregiverSearchFilter())
        }
    }
}

class NanysViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NanysViewModel::class.java)) {
            return NanysViewModel(container) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
