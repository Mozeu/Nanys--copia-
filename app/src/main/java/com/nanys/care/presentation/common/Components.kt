package com.nanys.care.presentation.common

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.nanys.care.R
import com.nanys.care.core.util.ColorUtil
import com.nanys.care.domain.model.Booking
import com.nanys.care.domain.model.BookingStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NanysScaffold(
    title: String,
    onProfileClick: () -> Unit = {},
    onMessagesClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    showProfileMenu: Boolean = true,
    profilePhotoUri: String = "default",
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    if (!showProfileMenu) {
                        IconButton(onClick = onLogout) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Salir de pestaña"
                            )
                        }
                    }
                },
                actions = {
                    if (showProfileMenu) {
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                ProfilePhoto(photoUri = profilePhotoUri, size = 36.dp)
                            }
                            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                                DropdownMenuItem(
                                    text = { Text("Mi perfil") },
                                    onClick = { menuExpanded = false; onProfileClick() },
                                    leadingIcon = { Icon(Icons.Default.Person, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Mensajes") },
                                    onClick = { menuExpanded = false; onMessagesClick() },
                                    leadingIcon = { Icon(Icons.Default.Email, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Configuración") },
                                    onClick = { menuExpanded = false; onSettingsClick() },
                                    leadingIcon = { Icon(Icons.Default.Settings, null) }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Cerrar sesión") },
                                    onClick = { menuExpanded = false; onLogout() },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, null) }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background
    ) { padding -> content(padding) }
}

@Composable
fun ProfilePhoto(
    photoUri: String,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    val hasCustomPhoto = photoUri.isNotBlank() && photoUri != "default"
    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        if (hasCustomPhoto) {
            AsyncImage(
                model = photoUri,
                contentDescription = "Foto de perfil",
                placeholder = painterResource(R.drawable.ic_default_avatar),
                error = painterResource(R.drawable.ic_default_avatar),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_default_avatar),
                contentDescription = "Foto de perfil",
                modifier = Modifier.fillMaxSize().padding(8.dp)
            )
        }
    }
}

@Composable
fun EditableProfilePhoto(
    photoUri: String,
    onPhotoSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val openDocumentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // Some providers grant a temporary read URI only.
            }
            onPhotoSelected(it.toString())
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        openDocumentLauncher.launch(arrayOf("image/*"))
    }
    val photoPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    Box(modifier, contentAlignment = Alignment.BottomEnd) {
        ProfilePhoto(photoUri = photoUri, size = 96.dp)
        FilledIconButton(
            onClick = {
                val granted = ContextCompat.checkSelfPermission(
                    context,
                    photoPermission
                ) == PackageManager.PERMISSION_GRANTED
                if (granted) {
                    openDocumentLauncher.launch(arrayOf("image/*"))
                } else {
                    permissionLauncher.launch(photoPermission)
                }
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Cambiar foto", modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun ButtonIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
    Spacer(Modifier.width(8.dp))
    Text(text)
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DashboardGridCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    subtitle: String = "",
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .height(104.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Column {
                Text(title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                if (subtitle.isNotBlank()) {
                    Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(0.62f))
                }
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking, onClick: (() -> Unit)? = null) {
    val statusColor = when (booking.status) {
        BookingStatus.PENDING -> Color(0xFFF59E0B)
        BookingStatus.ACCEPTED -> ColorUtil.parseHex(booking.colorHex)
        BookingStatus.REJECTED -> Color(0xFFEF4444)
        BookingStatus.COMPLETED -> Color(0xFF64748B)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Box(
                Modifier
                    .width(4.dp)
                    .height(64.dp)
                    .background(statusColor, RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("${booking.date} · ${booking.timeSlot}", fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProfilePhoto(booking.caregiverPhotoUri, size = 32.dp)
                    Text("Cuidador: ${booking.caregiverName.ifBlank { booking.caregiverEmail }}")
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProfilePhoto(booking.tutorPhotoUri, size = 32.dp)
                    Text("Tutor: ${booking.tutorName.ifBlank { booking.tutorEmail }}")
                }
                if (booking.childName.isNotBlank()) {
                    val childLabel = if (booking.childIds.size == 1) "Niño/a" else "Niños/as"
                    Text("$childLabel: ${booking.childName}")
                }
                Text("📍 ${booking.location}")
                if (booking.tutorNotes.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(8.dp)) {
                        Text(
                            "📝 Notas del padre: ${booking.tutorNotes}",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if (booking.additionalNotes.isNotBlank()) {
                    Text("Notas: ${booking.additionalNotes}", style = MaterialTheme.typography.bodySmall)
                }
                if (booking.hourlyRate > 0.0) {
                    Text(
                        "Tarifa base: $${String.format("%.2f", booking.hourlyRate)}/h · Niño extra: $${String.format("%.2f", booking.extraChildRate)}/h",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text("Total: $${String.format("%.2f", booking.totalPrice)} · ${booking.status.name}")
            }
        }
    }
}

@Composable
fun RatingStars(rating: Double, modifier: Modifier = Modifier) {
    Row(modifier) {
        repeat(5) { i ->
            Icon(
                if (i < rating.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null,
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
