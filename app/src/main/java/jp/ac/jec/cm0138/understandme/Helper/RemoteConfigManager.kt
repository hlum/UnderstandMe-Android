package jp.ac.jec.cm0138.understandme.Helper

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import jp.ac.jec.cm0138.understandme.BuildConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class RemoteConfigManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val TAG = "RemoteConfig"
        private const val PREFS_NAME = "remote_config_prefs"
        private const val KEY_API_ENDPOINT = "cached_api_endpoint"
        private const val KEY_MAIN_TIMER_DURATION = "cached_main_timer_duration"
        private const val KEY_ARC_TIMER_DURATION = "cached_arc_timer_duration"
        private const val REMOTE_KEY_API_ENDPOINT = "API_ENDPOINT"
        private const val REMOTE_KEY_MAIN_TIMER_DURATION = "MAIN_TIMER_DURATION"
        private const val REMOTE_KEY_ARC_TIMER_DURATION = "ARC_TIMER_DURATION"
    }

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    var apiEndpoint: String
        get() = sharedPreferences.getString(KEY_API_ENDPOINT, BuildConfig.API_BASE_URL)
            ?: BuildConfig.API_BASE_URL
        private set(value) {
            sharedPreferences.edit { putString(KEY_API_ENDPOINT, value) }
        }


    var mainTimerDuration: Int
        get() = sharedPreferences.getInt(KEY_MAIN_TIMER_DURATION, BuildConfig.MAIN_TIMER_DURATION.toInt())
        private set(value) {
            sharedPreferences.edit { putInt(KEY_MAIN_TIMER_DURATION, value) }
        }

    var arcTimerDuration: Int
        get() = sharedPreferences.getInt(KEY_ARC_TIMER_DURATION, BuildConfig.ARC_TIMER_DURATION.toInt())
        private set(value) {
            sharedPreferences.edit { putInt(KEY_ARC_TIMER_DURATION, value) }
        }


    suspend fun fetchRemoteConfig() {
        try {
            val fetchResult = remoteConfig.fetchAndActivate().await()
            when {
                fetchResult -> {
                    Log.i(TAG, "Remote Configをリモートから取得しました")
                }
                else -> {
                    Log.i(TAG, "Remote Configのキャッシュを使用します")
                }
            }

            val newEndpoint = remoteConfig.getString(REMOTE_KEY_API_ENDPOINT)
            val newMainTimerDuration =
                remoteConfig.getLong(REMOTE_KEY_MAIN_TIMER_DURATION)
            val newArcTimerDuration =
                remoteConfig.getLong(REMOTE_KEY_ARC_TIMER_DURATION)

            if(newMainTimerDuration > 0) {
                mainTimerDuration = newMainTimerDuration.toInt()
                Log.i(TAG, "MainTimerDurationを更新しました: $mainTimerDuration")
            }
            if(newArcTimerDuration > 0) {
                arcTimerDuration = newArcTimerDuration.toInt()
                Log.i(TAG, "ArcTimerDurationを更新しました: $arcTimerDuration")
            }

            if (newEndpoint.isNotEmpty()) {
                apiEndpoint = newEndpoint
                Log.i(TAG, "API_ENDPOINTを更新しました: $apiEndpoint")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Remote Configの取得エラー: ${e.message}")
        }
    }
}
