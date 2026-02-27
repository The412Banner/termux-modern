package com.termux.app.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.termux.terminal.TerminalSession

data class ExtraKey(val label: String, val value: String)

val EXTRA_KEYS = listOf(
    ExtraKey("ESC", "\u001B"),
    ExtraKey("TAB", "\t"),
    ExtraKey("CTRL", ""),  // TODO: Special handling
    ExtraKey("ALT", ""),   // TODO: Special handling
    ExtraKey("|", "|"),
    ExtraKey("-", "-"),
    ExtraKey("_", "_"),
    ExtraKey("/", "/"),
    ExtraKey("~", "~"),
    ExtraKey("`", "`"),
    ExtraKey("'", "'"),
    ExtraKey("\"", "\""),
    ExtraKey("{", "{"),
    ExtraKey("}", "}"),
    ExtraKey("[", "["),
    ExtraKey("]", "]"),
    ExtraKey("(", "("),
    ExtraKey(")", ")"),
    ExtraKey("<", "<"),
    ExtraKey(">", ">"),
    ExtraKey("↑", "\u001B[A"),
    ExtraKey("↓", "\u001B[B"),
    ExtraKey("←", "\u001B[D"),
    ExtraKey("→", "\u001B[C"),
)

@Composable
fun ExtraKeysBar(
    session: TerminalSession?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 3.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            EXTRA_KEYS.forEach { key ->
                ExtraKeyButton(
                    label = key.label,
                    onClick = { 
                        if (key.value.isNotEmpty()) {
                            session?.write(key.value)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ExtraKeyButton(
    label: String,
    onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.height(32.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
        shape = MaterialTheme.shapes.small,
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
        )
    }
}
