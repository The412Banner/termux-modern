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
    private lateinit var mProperties: TermuxAppSharedProperties
    
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

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as TermuxService.LocalBinder
        mTermuxService = binder.service
        
        mTermuxService?.setTermuxTerminalSessionClient(mTermuxTerminalSessionActivityClient)
        
        if (mTermuxService?.isTermuxSessionsEmpty == true) {
            TermuxInstaller.setupBootstrapIfNeeded(this) {
                mTermuxService?.createTermuxSession()
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mTermuxService = null
    }

    override fun onDestroy() {
        super.onDestroy()
        mTermuxService?.unsetTermuxTerminalSessionClient()
        try { unbindService(this) } catch (e: Exception) {}
    }
}
