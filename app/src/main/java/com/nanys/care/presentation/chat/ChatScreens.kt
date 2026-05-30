package com.nanys.care.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nanys.care.core.util.DateUtil
import com.nanys.care.domain.model.ConversationSummary
import com.nanys.care.domain.model.Message
import com.nanys.care.presentation.common.ButtonIcon
import com.nanys.care.presentation.common.NanysScaffold
import com.nanys.care.presentation.viewmodel.NanysViewModel

@Composable
fun ChatListScreen(
    viewModel: NanysViewModel,
    readOnly: Boolean = false,
    onBack: () -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenProfile: (String) -> Unit = {}
) {
    val conversations by viewModel.conversations.collectAsState()
    LaunchedEffect(viewModel.userEmail) {
        viewModel.userEmail?.let {
            if (readOnly) viewModel.loadAllConversations() else viewModel.loadConversations(it)
        }
    }
    NanysScaffold(
        title = if (readOnly) "Todas las conversaciones" else "Mensajes",
        showProfileMenu = false,
        onLogout = onBack
    ) { padding ->
        if (conversations.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Sin conversaciones")
            }
        } else {
            LazyColumn(Modifier.padding(padding)) {
                items(conversations) { conv ->
                    ConversationItem(conv, onClick = { onOpenChat(conv.otherEmail) })
                }
            }
        }
    }
}

@Composable
private fun ConversationItem(conv: ConversationSummary, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(conv.otherName, style = MaterialTheme.typography.titleSmall)
                Text(conv.lastMessage, maxLines = 1, style = MaterialTheme.typography.bodySmall)
            }
            Text(DateUtil.formatDisplay(conv.lastTimestamp), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    viewModel: NanysViewModel,
    otherEmail: String,
    readOnly: Boolean = false,
    onBack: () -> Unit,
    onOpenProfile: (String) -> Unit = {}
) {
    val myEmail = viewModel.userEmail.orEmpty()
    val messages by viewModel.messages.collectAsState()
    var text by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(otherEmail) {
        if (readOnly && otherEmail.contains("|")) {
            val parts = otherEmail.split("|")
            if (parts.size == 2) viewModel.loadMessages(parts[0], parts[1])
        } else if (!readOnly) {
            viewModel.loadMessages(myEmail, otherEmail)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(otherEmail) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                },
                actions = {
                    if (!readOnly) {
                        TextButton(
                            onClick = { onOpenProfile(otherEmail) },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                        ) {
                            ButtonIcon(Icons.Default.Person, "Perfil")
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
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth().padding(8.dp),
                state = listState,
                reverseLayout = false
            ) {
                items(messages) { msg ->
                    MessageBubble(msg, myEmail)
                }
            }
            if (!readOnly) {
                Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Escribe un mensaje...") },
                        shape = RoundedCornerShape(24.dp)
                    )
                    IconButton(onClick = {
                        if (text.isNotBlank()) {
                            viewModel.sendMessage(otherEmail, text.trim())
                            text = ""
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, "Enviar")
                    }
                }
            }
            if (readOnly) {
                Row(Modifier.padding(8.dp)) {
                    OutlinedButton(onClick = { messages.lastOrNull()?.let { viewModel.flagMessage(it.id) } }) {
                        Icon(Icons.Default.Flag, null)
                        Spacer(Modifier.width(4.dp))
                        Text("Marcar inapropiado")
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: Message, myEmail: String) {
    val isMine = message.senderEmail == myEmail
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isMine) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(
                topStart = 16.dp, topEnd = 16.dp,
                bottomStart = if (isMine) 16.dp else 4.dp,
                bottomEnd = if (isMine) 4.dp else 16.dp
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text(message.content)
                Text(
                    DateUtil.formatDisplay(message.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
