package jp.ac.jec.cm0138.understandme.Repository.Abstract

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import jp.ac.jec.cm0138.understandme.Repository.Impl.AuthResult

interface AuthRepository {
    suspend fun signInWithGoogle(context: Context): AuthResult
    fun getCurrentUser(): FirebaseUser
}