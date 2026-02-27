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
import com.termux.app.TermuxService
import com.termux.app.ui.components.ExtraKeysBar
import com.termux.app.ui.components.TermuxTerminalView
import com.termux.terminal.TerminalSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    termuxService: TermuxService?,
    onOpenSettings: () -> Unit
) {
    var activeSessionIndex by remember { mutableStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }

    val sessions = termuxService?.getSessions() ?: emptyList<TerminalSession>()
    val activeSession = sessions.getOrNull(activeSessionIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Termux Modern") },
                actions = {
                    IconButton(onClick = {
                        termuxService?.createTermuxSession()
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
                activeSession?.write(key)
            })
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (activeSession != null) {
                TermuxTerminalView(
                    modifier = Modifier.fillMaxSize(),
                    onViewCreated = { view ->
                        view.attachSession(activeSession)
                    }
                )
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
