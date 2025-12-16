package jp.ac.jec.cm0138.understandme.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.ac.jec.cm0138.understandme.BuildConfig
import jp.ac.jec.cm0138.understandme.Retrofit.APIKeyInterceptor
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ClassRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.UserDataRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.FirebaseAuthenticationRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopClassRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopUserDataRepository
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ClassAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.UserAPIService
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthenticationRepository(): AuthRepository {
        return FirebaseAuthenticationRepository()
    }


    @Provides
    @Singleton
    fun provideLollipopUserRepository(
        api: UserAPIService
    ): UserDataRepository {
        return LollipopUserDataRepository(api = api)
    }


    @Provides
    @Singleton
    fun provideLollipopClassRepository(
        classAPIService: ClassAPIService
    ): ClassRepository {
        return LollipopClassRepository(classAPIService = classAPIService)
    }


    @Provides
    @Singleton
    fun provideUserAPIService(
        retrofit: Retrofit
    ): UserAPIService {
        return retrofit.create(UserAPIService::class.java)
    }


    @Provides
    @Singleton
    fun provideClassAPIService(
        retrofit: Retrofit
    ): ClassAPIService {
        return retrofit.create(ClassAPIService::class.java)
    }


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }


    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(apiKey: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(APIKeyInterceptor(apiKey))
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIKey(): String {
        return BuildConfig.API_KEY
    }

}