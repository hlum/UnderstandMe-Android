package jp.ac.jec.cm0138.understandme

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import jp.ac.jec.cm0138.understandme.Helper.RemoteConfigManager
import jp.ac.jec.cm0138.understandme.Service.UnderstandMeFirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MainApplication: Application() {
    
    @Inject
    lateinit var remoteConfigManager: RemoteConfigManager
    
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        fetchRemoteConfig()
    }
    
    private fun fetchRemoteConfig() {
        applicationScope.launch {
            remoteConfigManager.fetchRemoteConfig()
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = UnderstandMeFirebaseMessagingService.CHANNEL_ID
            val channelName = "UnderstandMe Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Notifications for homework updates"
                enableLights(true)
                enableVibration(true)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}