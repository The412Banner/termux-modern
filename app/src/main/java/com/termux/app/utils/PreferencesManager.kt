package com.termux.app.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode { SYSTEM, LIGHT, DARK }
enum class ColorScheme { DRACULA, NORD, ONE_DARK, SOLARIZED_DARK, SOLARIZED_LIGHT, MONOKAI, GRUVBOX, MATERIAL }
enum class BellMode { NONE, VIBRATE, BEEP }

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {

    companion object {
        val THEME_MODE             = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLORS         = booleanPreferencesKey("dynamic_colors")
        val COLOR_SCHEME           = stringPreferencesKey("color_scheme")
        val FONT_SIZE              = floatPreferencesKey("font_size")
        val AUTOCORRECT_ENABLED    = booleanPreferencesKey("autocorrect_enabled")
        val SPELLCHECK_ENABLED     = booleanPreferencesKey("spellcheck_enabled")
        val EXTRA_KEYS_ENABLED     = booleanPreferencesKey("extra_keys_enabled")
        val HAPTIC_FEEDBACK        = booleanPreferencesKey("haptic_feedback")
        val BELL_MODE              = stringPreferencesKey("bell_mode")
        val SCROLLBACK_LINES       = intPreferencesKey("scrollback_lines")
        val CURSOR_BLINK           = booleanPreferencesKey("cursor_blink")
        val ROOT_ENABLED           = booleanPreferencesKey("root_enabled")
        val BOOTSTRAP_INSTALLED    = booleanPreferencesKey("bootstrap_installed")
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        ThemeMode.valueOf(prefs[THEME_MODE] ?: ThemeMode.SYSTEM.name)
    }
    val dynamicColors: Flow<Boolean> = context.dataStore.data.map { it[DYNAMIC_COLORS] ?: true }
    val colorScheme: Flow<ColorScheme> = context.dataStore.data.map { prefs ->
        ColorScheme.valueOf(prefs[COLOR_SCHEME] ?: ColorScheme.DRACULA.name)
    }
    val fontSize: Flow<Float>         = context.dataStore.data.map { it[FONT_SIZE] ?: 14f }
    val autocorrectEnabled: Flow<Boolean> = context.dataStore.data.map { it[AUTOCORRECT_ENABLED] ?: false }
    val spellcheckEnabled: Flow<Boolean>  = context.dataStore.data.map { it[SPELLCHECK_ENABLED] ?: false }
    val extraKeysEnabled: Flow<Boolean>   = context.dataStore.data.map { it[EXTRA_KEYS_ENABLED] ?: true }
    val hapticFeedback: Flow<Boolean>     = context.dataStore.data.map { it[HAPTIC_FEEDBACK] ?: true }
    val bellMode: Flow<BellMode>          = context.dataStore.data.map { prefs ->
        BellMode.valueOf(prefs[BELL_MODE] ?: BellMode.VIBRATE.name)
    }
    val scrollbackLines: Flow<Int>  = context.dataStore.data.map { it[SCROLLBACK_LINES] ?: 3000 }
    val cursorBlink: Flow<Boolean>  = context.dataStore.data.map { it[CURSOR_BLINK] ?: true }
    val rootEnabled: Flow<Boolean>  = context.dataStore.data.map { it[ROOT_ENABLED] ?: false }
    val bootstrapInstalled: Flow<Boolean> = context.dataStore.data.map { it[BOOTSTRAP_INSTALLED] ?: false }

    suspend fun setThemeMode(mode: ThemeMode)       { context.dataStore.edit { it[THEME_MODE] = mode.name } }
    suspend fun setDynamicColors(v: Boolean)         { context.dataStore.edit { it[DYNAMIC_COLORS] = v } }
    suspend fun setColorScheme(s: ColorScheme)       { context.dataStore.edit { it[COLOR_SCHEME] = s.name } }
    suspend fun setFontSize(v: Float)                { context.dataStore.edit { it[FONT_SIZE] = v } }
    suspend fun setAutocorrect(v: Boolean)           { context.dataStore.edit { it[AUTOCORRECT_ENABLED] = v } }
    suspend fun setSpellcheck(v: Boolean)            { context.dataStore.edit { it[SPELLCHECK_ENABLED] = v } }
    suspend fun setExtraKeys(v: Boolean)             { context.dataStore.edit { it[EXTRA_KEYS_ENABLED] = v } }
    suspend fun setHapticFeedback(v: Boolean)        { context.dataStore.edit { it[HAPTIC_FEEDBACK] = v } }
    suspend fun setBellMode(m: BellMode)             { context.dataStore.edit { it[BELL_MODE] = m.name } }
    suspend fun setScrollbackLines(v: Int)           { context.dataStore.edit { it[SCROLLBACK_LINES] = v } }
    suspend fun setCursorBlink(v: Boolean)           { context.dataStore.edit { it[CURSOR_BLINK] = v } }
    suspend fun setRootEnabled(v: Boolean)           { context.dataStore.edit { it[ROOT_ENABLED] = v } }
    suspend fun setBootstrapInstalled(v: Boolean)    { context.dataStore.edit { it[BOOTSTRAP_INSTALLED] = v } }
}
