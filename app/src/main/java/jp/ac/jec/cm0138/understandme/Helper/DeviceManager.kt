package jp.ac.jec.cm0138.understandme.Helper
import android.content.Context
import android.os.Build
import java.util.UUID

/**
 * Generate, store, and retrieve a device-unique ID.
 * Used when a user logs in on multiple devices with the same userId.
 */
object DeviceManager {

    private const val PREF_NAME = "device_prefs"
    private const val DEVICE_ID_KEY = "device_id_key"

    fun getDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val existingId = prefs.getString(DEVICE_ID_KEY, null)

        return existingId ?: createNewDeviceId(context)
    }

    private fun createNewDeviceId(context: Context): String {
        val newId = UUID.randomUUID().toString()
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .putString(DEVICE_ID_KEY, newId)
            .apply()

        return newId
    }

    /**
     * Similar to UIDevice info in iOS
     */
    fun getDeviceType(): String {
        val manufacturer = Build.MANUFACTURER      // e.g. Samsung
        val model = Build.MODEL                    // e.g. Galaxy S23
        val osName = "Android"
        val osVersion = Build.VERSION.RELEASE      // e.g. 14

        return "$manufacturer-$model-$osName-$osVersion"
    }
}
