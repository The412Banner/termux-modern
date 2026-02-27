package com.termux.app

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import com.termux.app.terminal.TermuxTerminalSessionActivityClient
import com.termux.app.terminal.TermuxTerminalViewClient
import com.termux.app.ui.AppNavHost
import com.termux.app.ui.theme.TermuxModernTheme
import com.termux.app.utils.PreferencesManager
import com.termux.shared.logger.Logger
import com.termux.shared.termux.TermuxConstants
import com.termux.shared.termux.settings.properties.TermuxAppSharedProperties
import com.termux.view.TerminalView

class TermuxActivity : AppCompatActivity(), ServiceConnection {

    private var mTermuxService by mutableStateOf<TermuxService?>(null)
    private var mCurrentSession by mutableStateOf<com.termux.terminal.TerminalSession?>(null)
    private lateinit var mProperties: TermuxAppSharedProperties
    private var mIsVisible = false
    
    // Termux internal clients
    private lateinit var mTermuxTerminalSessionActivityClient: TermuxTerminalSessionActivityClient
    private lateinit var mTermuxTerminalViewClient: TermuxTerminalViewClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        mProperties = TermuxAppSharedProperties.getProperties()
        
        // Initialize legacy clients needed by Termux logic
        mTermuxTerminalSessionActivityClient = TermuxTerminalSessionActivityClient(this)
        mTermuxTerminalViewClient = TermuxTerminalViewClient(this, mTermuxTerminalSessionActivityClient)
        
        mTermuxTerminalViewClient.onCreate()
        mTermuxTerminalSessionActivityClient.onCreate()

        setContent {
            val isDark = true // TODO: Observe from prefs
            TermuxModernTheme(darkTheme = isDark) {
                AppNavHost(
                    termuxService = mTermuxService,
                    onOpenOldSettings = { 
                        // Start old settings activity if needed
                        startActivity(Intent(this, com.termux.app.activities.SettingsActivity::class.java))
                    }
                )
            }
        }

        // Start and bind TermuxService
        val serviceIntent = Intent(this, TermuxService::class.java)
        startService(serviceIntent)
        bindService(serviceIntent, this, 0)
    }

    override fun onStart() {
        super.onStart()
        mIsVisible = true
        mTermuxTerminalSessionActivityClient.onStart()
    }

    override fun onResume() {
        super.onResume()
        mTermuxTerminalSessionActivityClient.onResume()
    }

    override fun onStop() {
        super.onStop()
        mIsVisible = false
        mTermuxTerminalSessionActivityClient.onStop()
    }

    // --- Legacy Methods for Java Clients ---

    fun getTermuxService(): TermuxService? = mTermuxService
    
    fun isVisible(): Boolean = mIsVisible
    
    fun getCurrentSession(): com.termux.terminal.TerminalSession? = mCurrentSession
    
    fun setCurrentSession(session: com.termux.terminal.TerminalSession?) {
        mCurrentSession = session
    }

    fun getProperties(): TermuxAppSharedProperties = mProperties
    
    fun getPreferences(): com.termux.shared.termux.settings.preferences.TermuxAppSharedPreferences {
        return com.termux.shared.termux.settings.preferences.TermuxAppSharedPreferences.build(this)
    }

    fun getTerminalView(): com.termux.view.TerminalView? {
        // This is tricky with Compose. For now, returning null or we need to capture it.
        return null 
    }

    fun showToast(message: String?, isLong: Boolean) {
        android.widget.Toast.makeText(this, message, if (isLong) android.widget.Toast.LENGTH_LONG else android.widget.Toast.LENGTH_SHORT).show()
    }

    fun finishActivityIfNotFinishing() {
        if (!isFinishing) finish()
    }

    fun termuxSessionListNotifyUpdated() {
        // Update UI if needed
    }

    fun getDrawer(): androidx.drawerlayout.widget.DrawerLayout? = null
    
    fun getTerminalToolbarViewPager(): Any? = null
    
    fun isTerminalViewSelected(): Boolean = true
    
    fun getTermuxActivityRootView(): android.view.View? = window.decorView
    
    fun isActivityRecreated(): Boolean = false
    
    fun toggleTerminalToolbar() {}

    // --- ServiceConnection ---

