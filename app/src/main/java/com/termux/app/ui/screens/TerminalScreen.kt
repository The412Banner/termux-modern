package com.termux.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.termux.app.TermuxService
import com.termux.app.ui.components.ExtraKeysBar
import com.termux.app.ui.components.TermuxTerminalView
import com.termux.terminal.TerminalSession
import com.termux.shared.termux.shell.command.runner.terminal.TermuxSession as SharedTermuxSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    termuxService: TermuxService?,
    onOpenSettings: () -> Unit
) {
    var activeSessionIndex by remember { mutableStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }
    var showSessionSelector by remember { mutableStateOf(false) }

    val termuxSessions = termuxService?.getTermuxSessions() ?: emptyList<SharedTermuxSession>()
    val activeTermuxSession = termuxSessions.getOrNull(activeSessionIndex)
    val activeTerminalSession = activeTermuxSession?.getTerminalSession()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Session ${activeSessionIndex + 1}",
                        modifier = androidx.compose.foundation.clickable { showSessionSelector = true }
                    )
                },
                actions = {
                    IconButton(onClick = {
                        termuxService?.createTermuxSession()
                        activeSessionIndex = termuxSessions.size // Switch to new session
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "New Session")
                    }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Reset Terminal") },
                                leadingIcon = { Icon(Icons.Default.Refresh, null) },
                                onClick = { 
                                    showMenu = false
                                    activeTerminalSession?.reset()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                leadingIcon = { Icon(Icons.Default.Settings, null) },
                                onClick = { showMenu = false; onOpenSettings() }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            ExtraKeysBar(onKeyPress = { key ->
                activeTerminalSession?.write(key)
            })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (activeTerminalSession != null) {
                TermuxTerminalView(
                    modifier = Modifier.fillMaxSize(),
                    onViewCreated = { view ->
                        view.attachSession(activeTerminalSession)
                    }
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    if (termuxService == null) {
                        CircularProgressIndicator()
                    } else {
                        Text("No active sessions")
                    }
                }
            }
        }

        if (showSessionSelector) {
            AlertDialog(
                onDismissRequest = { showSessionSelector = false },
                title = { Text("Select Session") },
                text = {
                    androidx.compose.foundation.lazy.LazyColumn {
                        items(termuxSessions.size) { index ->
                            ListItem(
                                headlineContent = { Text("Session ${index + 1}") },
                                modifier = androidx.compose.foundation.clickable {
                                    activeSessionIndex = index
                                    showSessionSelector = false
                                },
                                trailingContent = {
                                    if (activeSessionIndex == index) {
                                        Icon(androidx.compose.material.icons.filled.Check, null)
                                    }
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showSessionSelector = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}
