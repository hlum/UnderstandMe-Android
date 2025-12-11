package jp.ac.jec.cm0138.understandme.Repository.Impl

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import jp.ac.jec.cm0138.understandme.Helper.GoogleAuthHelper
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthenticationiRepository
import kotlinx.coroutines.tasks.await

sealed class AuthResult {
    object Success : AuthResult()
    object Cancelled : AuthResult()
}


class FirebaseAuthenticationRepository(val context: Context): AuthenticationiRepository {
    private val TAG = "FirebaseAuthentication"
    private val auth = Firebase.auth
    private val googleAuthHelper = GoogleAuthHelper(context = context)

    override suspend fun signInWithGoogle(): AuthResult {
        try {
            val googleIdTokenCredential = googleAuthHelper.getGoogleIdTokenCredential()

            // Google SignIn Sheetからuserがバックボタンを押した場合
            if (googleIdTokenCredential == null) { return AuthResult.Cancelled }


            val credential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
            auth.signInWithCredential(credential).await()

            return AuthResult.Success

        } catch (e: Exception) {
            Log.e(TAG, "Google SignInに失敗しました。原因：${e}")
            throw Exception("Google SignInに失敗しました。")
        }
    }
}