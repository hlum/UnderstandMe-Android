package jp.ac.jec.cm0138.understandme.Repository.Impl

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import jp.ac.jec.cm0138.understandme.Helper.GoogleAuthHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

sealed class AuthResult {
    object Success : AuthResult()
    object Failed: AuthResult()
    object Cancelled : AuthResult()
}


class FirebaseAuthenticationRepository(): AuthRepository {
    private val TAG = "FirebaseAuthentication"
    private val auth = Firebase.auth
    private val googleAuthHelper = GoogleAuthHelper()

    override suspend fun signInWithGoogle(context: Context): AuthResult {
        return try {

                // CredentialManagerだけはMainThreadで
                val googleIdTokenCredential = withContext(Dispatchers.Main) {
                    googleAuthHelper.getGoogleIdTokenCredential(context = context)
                } ?: return AuthResult.Cancelled

                // Firebase auth は IO
            withContext(Dispatchers.IO) {
                val credential =
                    GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                auth.signInWithCredential(credential).await()
            }

            AuthResult.Success
            } catch (e: Exception) {
                Log.e(TAG, "Google SignInに失敗しました。原因：${e}")
                AuthResult.Failed
            }
        }

    override fun getCurrentUser(): FirebaseUser {
        val currentUser = auth.currentUser ?: throw Exception("ログインしていない")
        return currentUser
    }
}