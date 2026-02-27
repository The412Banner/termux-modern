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
import com.termux.app.terminal.TermuxActivityRootView
import com.termux.app.terminal.io.TermuxTerminalExtraKeys
import com.termux.app.ui.AppNavHost
import com.termux.app.ui.theme.TermuxModernTheme
import com.termux.app.utils.PreferencesManager
import com.termux.shared.logger.Logger
import com.termux.shared.termux.TermuxConstants
import com.termux.shared.termux.settings.properties.TermuxAppSharedProperties
import com.termux.shared.termux.extrakeys.ExtraKeysView
import com.termux.view.TerminalView

class TermuxActivity : AppCompatActivity(), ServiceConnection {

    private var mTermuxService by mutableStateOf<TermuxService?>(null)
    private var mCurrentSession by mutableStateOf<com.termux.terminal.TerminalSession?>(null)
    private lateinit var mProperties: TermuxAppSharedProperties
    private var mIsVisible = false
    private var mIsOnResumeAfterOnCreate = false
    
    // Termux internal clients
    private lateinit var mTermuxTerminalSessionActivityClient: TermuxTerminalSessionActivityClient
    private lateinit var mTermuxTerminalViewClient: TermuxTerminalViewClient
    private var mTermuxTerminalExtraKeys: TermuxTerminalExtraKeys? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        mProperties = TermuxAppSharedProperties.getProperties()
        
        // Initialize legacy clients needed by Termux logic
        mTermuxTerminalSessionActivityClient = TermuxTerminalSessionActivityClient(this)
        mTermuxTerminalViewClient = TermuxTerminalViewClient(this, mTermuxTerminalSessionActivityClient)
        
        // We need a TerminalView for TermuxTerminalExtraKeys, even if it's not the one displayed.
        // This is a workaround for legacy code compatibility.
        val dummyTerminalView = TerminalView(this, null)
        mTermuxTerminalExtraKeys = TermuxTerminalExtraKeys(this, dummyTerminalView, mTermuxTerminalViewClient, mTermuxTerminalSessionActivityClient)
        
        mTermuxTerminalViewClient.onCreate()
        mTermuxTerminalSessionActivityClient.onCreate()

        mIsOnResumeAfterOnCreate = true

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
        mIsOnResumeAfterOnCreate = false
    }

    override fun onStop() {
        super.onStop()
        mIsVisible = false
        mTermuxTerminalSessionActivityClient.onStop()
    }

    // --- Legacy Methods for Java Clients ---

    fun getTermuxService(): TermuxService? = mTermuxService
    
    fun isVisible(): Boolean = mIsVisible
    
    fun isOnResumeAfterOnCreate(): Boolean = mIsOnResumeAfterOnCreate
    
    fun getCurrentSession(): com.termux.terminal.TerminalSession? = mCurrentSession
    
    fun setCurrentSession(session: com.termux.terminal.TerminalSession?) {
        mCurrentSession = session
    }

    fun getProperties(): TermuxAppSharedProperties = mProperties
    
    fun getPreferences(): com.termux.shared.termux.settings.preferences.TermuxAppSharedPreferences {
        return com.termux.shared.termux.settings.preferences.TermuxAppSharedPreferences.build(this)!!
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
    
    fun getTerminalToolbarViewPager(): android.view.View? = null
    
    fun isTerminalViewSelected(): Boolean = true
    
    fun getTermuxActivityRootView(): TermuxActivityRootView? = null
    
    fun getTermuxActivityBottomSpaceView(): android.view.View? = null
    
    fun getExtraKeysView(): ExtraKeysView? = null
    
    fun setExtraKeysView(view: ExtraKeysView?) {}
    
    fun getTermuxTerminalExtraKeys(): TermuxTerminalExtraKeys? = mTermuxTerminalExtraKeys
    
    fun getTermuxTerminalSessionClient(): TermuxTerminalSessionActivityClient = mTermuxTerminalSessionActivityClient
    
    fun getTerminalToolbarDefaultHeight(): Int = 0
    
    fun getNavBarHeight(): Int = 0
    
    fun isActivityRecreated(): Boolean = false
    
    fun toggleTerminalToolbar() {}

    // --- ServiceConnection ---

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as TermuxService.LocalBinder
        mTermuxService = binder.service
        
        mTermuxService?.setTermuxTerminalSessionClient(mTermuxTerminalSessionActivityClient)
        
        if (mTermuxService?.isTermuxSessionsEmpty == true) {
            TermuxInstaller.setupBootstrapIfNeeded(this) {
                mTermuxService?.createTermuxSession(null, null, null, mProperties.defaultWorkingDirectory, false, null)
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

    companion object {
        @JvmStatic
        fun updateTermuxActivityStyling(service: TermuxService, boolean: Boolean) {}
        
        @JvmStatic
        fun startTermuxActivity(context: Context) {
            context.startActivity(newInstance(context))
        }
        
        @JvmStatic
        fun newInstance(context: Context): Intent {
            return Intent(context, TermuxActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }
}
