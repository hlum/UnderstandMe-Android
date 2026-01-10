package jp.ac.jec.cm0138.understandme.Helper

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthHelper() {
    val TAG = "GoogleAuthHelper"

    suspend fun getGoogleIdTokenCredential(context: Context): GoogleIdTokenCredential? {
        try {
            val googleIDOption: GetGoogleIdOption =
                createGoogleIDOption(serverClientID = "660534500337-7m8qrrqn7ngpi3hsuen5ptf7ll8n289p.apps.googleusercontent.com")

            val request: GetCredentialRequest =
                createCredentialRequest(googleIDOption = googleIDOption)

            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            Log.i("GoogleAuthHelper", "Credential returned: ${result.credential}")
            return handleSignIn(result = result)
        } catch (e: Exception) {
            Log.e(TAG, "GoogleIdTokenCredentialの取得に失敗しました。原因：${e}")
            return null
        }

    }


    private fun handleSignIn(result: GetCredentialResponse): GoogleIdTokenCredential {
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        return googleIdTokenCredential
                    } catch (e: Exception) {
                        throw Exception("GoogleIdTokenCredentialの作成に失敗しました。")
                    }
                }
            }
        }

        throw Exception("Google id token Credential ではありません。")
    }


    private fun createCredentialRequest(googleIDOption: GetGoogleIdOption): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIDOption)
            .build()
    }
}

private fun createGoogleIDOption(serverClientID: String): GetGoogleIdOption {
    return GetGoogleIdOption.Builder()
//        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(serverClientID)
//        .setAutoSelectEnabled(false)
        .build()
}
