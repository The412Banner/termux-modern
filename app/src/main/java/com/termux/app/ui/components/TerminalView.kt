package com.termux.app.ui.components

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.termux.view.TerminalView
import com.termux.terminal.TerminalSession

@Composable
fun TermuxTerminalView(
    session: TerminalSession,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TerminalView(context, null).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                attachSession(session)
            }
        },
        update = { view ->
            if (view.currentSession != session) {
                view.attachSession(session)
            }
        }
    )
}
