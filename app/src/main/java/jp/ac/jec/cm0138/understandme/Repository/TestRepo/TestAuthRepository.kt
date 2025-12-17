package jp.ac.jec.cm0138.understandme.Repository.TestRepo

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.AuthResult

class TestAuthRepository() : AuthRepository {
    override suspend fun signInWithGoogle(context: Context): AuthResult {
        return AuthResult.Success
    }

    override fun getCurrentUser(): FirebaseUser {
        TODO("Not yet implemented")
    }
}