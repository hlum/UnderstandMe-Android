package jp.ac.jec.cm0138.understandme.Retrofit

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor(
    private val firebaseAuth: FirebaseAuth
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // Get Firebase ID token
        val authToken = getAuthToken()

        val newRequest = original.newBuilder()
            .addHeader("Authorization", authToken)
            .build()

        return chain.proceed(newRequest)
    }

    /**
     * Get Firebase ID token from current user
     */
    private fun getAuthToken(): String {
        return runBlocking {
            val currentUser = firebaseAuth.currentUser
                ?: throw IllegalStateException("User must be authenticated to make API requests")

            try {
                val token = currentUser.getIdToken(false).await().token
                    ?: throw IllegalStateException("Failed to retrieve Firebase ID token")

                token
            } catch (e: Exception) {
                throw e
            }
        }
    }
}