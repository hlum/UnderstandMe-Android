package jp.ac.jec.cm0138.understandme.Repository.Abstract

import jp.ac.jec.cm0138.understandme.Repository.Impl.AuthResult

interface AuthenticationiRepository {
    suspend fun signInWithGoogle(): AuthResult
}