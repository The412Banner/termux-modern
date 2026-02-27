package com.termux.app.terminal

import androidx.compose.ui.graphics.Color
import com.termux.app.utils.ColorScheme

data class TerminalColors(
    val background: Color,
    val foreground: Color,
    val cursor: Color,
    val black: Color,
    val red: Color,
    val green: Color,
    val yellow: Color,
    val blue: Color,
    val magenta: Color,
    val cyan: Color,
    val white: Color,
    val brightBlack: Color,
    val brightRed: Color,
    val brightGreen: Color,
    val brightYellow: Color,
    val brightBlue: Color,
    val brightMagenta: Color,
    val brightCyan: Color,
    val brightWhite: Color,
)

object TerminalColorSchemes {

    val Dracula = TerminalColors(
        background = Color(0xFF282A36),
        foreground = Color(0xFFF8F8F2),
        cursor = Color(0xFFF8F8F2),
        black = Color(0xFF21222C),
        red = Color(0xFFFF5555),
        green = Color(0xFF50FA7B),
        yellow = Color(0xFFF1FA8C),
        blue = Color(0xFFBD93F9),
        magenta = Color(0xFFFF79C6),
        cyan = Color(0xFF8BE9FD),
        white = Color(0xFFBFBFBF),
        brightBlack = Color(0xFF6272A4),
        brightRed = Color(0xFFFF6E6E),
        brightGreen = Color(0xFF69FF94),
        brightYellow = Color(0xFFFFFFA5),
        brightBlue = Color(0xFFD6ACFF),
        brightMagenta = Color(0xFFFF92DF),
        brightCyan = Color(0xFFA4FFFF),
        brightWhite = Color(0xFFFFFFFF),
    )

    val Nord = TerminalColors(
        background = Color(0xFF2E3440),
        foreground = Color(0xFFD8DEE9),
        cursor = Color(0xFFD8DEE9),
        black = Color(0xFF3B4252),
        red = Color(0xFFBF616A),
        green = Color(0xFFA3BE8C),
        yellow = Color(0xFFEBCB8B),
        blue = Color(0xFF81A1C1),
        magenta = Color(0xFFB48EAD),
        cyan = Color(0xFF88C0D0),
        white = Color(0xFFE5E9F0),
        brightBlack = Color(0xFF4C566A),
        brightRed = Color(0xFFBF616A),
        brightGreen = Color(0xFFA3BE8C),
        brightYellow = Color(0xFFEBCB8B),
        brightBlue = Color(0xFF81A1C1),
        brightMagenta = Color(0xFFB48EAD),
        brightCyan = Color(0xFF8FBCBB),
        brightWhite = Color(0xFFECEFF4),
    )

    val OneDark = TerminalColors(
        background = Color(0xFF282C34),
        foreground = Color(0xFFABB2BF),
        cursor = Color(0xFF528BFF),
        black = Color(0xFF282C34),
        red = Color(0xFFE06C75),
        green = Color(0xFF98C379),
        yellow = Color(0xFFE5C07B),
        blue = Color(0xFF61AFEF),
        magenta = Color(0xFFC678DD),
        cyan = Color(0xFF56B6C2),
        white = Color(0xFFABB2BF),
        brightBlack = Color(0xFF5C6370),
        brightRed = Color(0xFFE06C75),
        brightGreen = Color(0xFF98C379),
        brightYellow = Color(0xFFE5C07B),
        brightBlue = Color(0xFF61AFEF),
        brightMagenta = Color(0xFFC678DD),
        brightCyan = Color(0xFF56B6C2),
        brightWhite = Color(0xFFFFFFFF),
    )

    val SolarizedDark = TerminalColors(
        background = Color(0xFF002B36),
        foreground = Color(0xFF839496),
        cursor = Color(0xFF839496),
        black = Color(0xFF073642),
        red = Color(0xFFDC322F),
        green = Color(0xFF859900),
        yellow = Color(0xFFB58900),
        blue = Color(0xFF268BD2),
        magenta = Color(0xFFD33682),
        cyan = Color(0xFF2AA198),
        white = Color(0xFFEEE8D5),
        brightBlack = Color(0xFF002B36),
        brightRed = Color(0xFFCB4B16),
        brightGreen = Color(0xFF586E75),
        brightYellow = Color(0xFF657B83),
        brightBlue = Color(0xFF839496),
        brightMagenta = Color(0xFF6C71C4),
        brightCyan = Color(0xFF93A1A1),
        brightWhite = Color(0xFFFDF6E3),
    )

    val Gruvbox = TerminalColors(
        background = Color(0xFF282828),
        foreground = Color(0xFFEBDBB2),
        cursor = Color(0xFFEBDBB2),
        black = Color(0xFF282828),
        red = Color(0xFFCC241D),
        green = Color(0xFF98971A),
        yellow = Color(0xFFD79921),
        blue = Color(0xFF458588),
        magenta = Color(0xFFB16286),
        cyan = Color(0xFF689D6A),
        white = Color(0xFFA89984),
        brightBlack = Color(0xFF928374),
        brightRed = Color(0xFFFB4934),
        brightGreen = Color(0xFFB8BB26),
        brightYellow = Color(0xFFFABD2F),
        brightBlue = Color(0xFF83A598),
        brightMagenta = Color(0xFFD3869B),
        brightCyan = Color(0xFF8EC07C),
        brightWhite = Color(0xFFEBDBB2),
    )

    fun fromScheme(scheme: ColorScheme): TerminalColors = when (scheme) {
        ColorScheme.DRACULA -> Dracula
        ColorScheme.NORD -> Nord
        ColorScheme.ONE_DARK -> OneDark
        ColorScheme.SOLARIZED_DARK -> SolarizedDark
        ColorScheme.GRUVBOX -> Gruvbox
        else -> Dracula
    }
}
