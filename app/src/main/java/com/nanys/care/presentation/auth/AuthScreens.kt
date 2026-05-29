package com.nanys.care.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nanys.care.domain.model.UserRole
import com.nanys.care.presentation.viewmodel.NanysViewModel

@Composable
fun LoginScreen(
    viewModel: NanysViewModel,
    onNavigateRegister: () -> Unit,
    onLoginSuccess: (UserRole) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val error by viewModel.error.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Nanys Care", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Conectando familias con cuidadores", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))
        OutlinedTextField(email, { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(password, { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(12.dp))
        error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.clearError()
                viewModel.login(email, password) {
                    viewModel.userRole?.let(onLoginSuccess)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) { Text("Iniciar sesión") }
        TextButton(onClick = onNavigateRegister) { Text("¿No tienes cuenta? Regístrate") }
        Spacer(Modifier.height(16.dp))
        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
            Column(Modifier.padding(12.dp)) {
                Text("Cuentas demo:", fontWeight = FontWeight.SemiBold)
                Text("tutor1@test.com / 123")
                Text("cuidador1@test.com / 123")
                Text("admin@nanys.com / admin123")
                Text("supervisor@nanys.com / super123")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: NanysViewModel,
    onNavigateLogin: () -> Unit,
    onRegisterSuccess: (UserRole) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(UserRole.TUTOR) }
    val error by viewModel.error.collectAsState()

    Column(Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(fullName, { fullName = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(email, { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(password, { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(8.dp))
        Text("Rol")
        Row {
            FilterChip(selected = role == UserRole.TUTOR, onClick = { role = UserRole.TUTOR }, label = { Text("Tutor") })
            Spacer(Modifier.width(8.dp))
            FilterChip(selected = role == UserRole.CUIDADOR, onClick = { role = UserRole.CUIDADOR }, label = { Text("Cuidador") })
        }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.clearError()
                viewModel.register(email, password, role, fullName) { onRegisterSuccess(role) }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrarse") }
        TextButton(onClick = onNavigateLogin) { Text("Ya tengo cuenta") }
    }
}
