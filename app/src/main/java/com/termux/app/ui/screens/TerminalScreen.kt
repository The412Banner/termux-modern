package com.termux.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
    service: TermuxService?,
    onNavigateToSettings: () -> Unit
) {
    val sessions = service?.termuxSessions?.map { it.terminalSession } ?: emptyList()
    var currentSession by remember { mutableStateOf<TerminalSession?>(null) }

    LaunchedEffect(sessions) {
        if (currentSession == null && sessions.isNotEmpty()) {
            currentSession = sessions.first()
        } else if (currentSession != null && !sessions.contains(currentSession)) {
            currentSession = sessions.lastOrNull()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (sessions.isNotEmpty()) {
                        ScrollableTabRow(
                            selectedTabIndex = sessions.indexOf(currentSession).coerceAtLeast(0),
                            edgePadding = 0.dp,
                            divider = {},
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ) {
                            sessions.forEach { session ->
                                Tab(
                                    selected = currentSession == session,
                                    onClick = { currentSession = session },
                                    text = { Text("Session ${sessions.indexOf(session) + 1}") }
                                )
                            }
                        }
                    } else {
                        Text("Termux")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        service?.createTermuxSession("/system/bin/sh", null, null, "/data/data/com.termux/files/home", false, "New Session") 
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "New Session")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            ExtraKeysBar(
                session = currentSession,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (currentSession != null) {
                TermuxTerminalView(
                    session = currentSession!!,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    if (service == null) {
                        Text("Service not bound")
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
