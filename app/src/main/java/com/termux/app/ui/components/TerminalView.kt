package com.termux.app.ui.components

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.termux.view.TerminalView

@Composable
fun TermuxTerminalView(
    modifier: Modifier = Modifier,
    onViewCreated: (TerminalView) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TerminalView(context, null).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                onViewCreated(this)
            }
        },
        update = { view ->
            // Update view properties if needed
        }
    )
}
