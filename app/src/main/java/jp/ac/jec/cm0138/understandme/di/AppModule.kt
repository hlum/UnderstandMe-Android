package jp.ac.jec.cm0138.understandme.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthenticationiRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.FirebaseAuthenticationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthenticationiRepository(): AuthenticationiRepository {
        return FirebaseAuthenticationRepository()
    }
}