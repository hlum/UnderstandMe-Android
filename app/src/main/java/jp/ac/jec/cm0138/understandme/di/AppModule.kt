package jp.ac.jec.cm0138.understandme.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.ac.jec.cm0138.understandme.BuildConfig
import jp.ac.jec.cm0138.understandme.Helper.RemoteConfigManager
import jp.ac.jec.cm0138.understandme.LollipopResultRepository.Impl.LollipopResultRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AnswerRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AuthRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.AverageScoreRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ChoiceRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ClassRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.FCMTokenRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.HomeworkRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ProjectRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.QuestionWithChoicesRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.ResultRepository
import jp.ac.jec.cm0138.understandme.Repository.Abstract.UserDataRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.FirebaseAuthenticationRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopAnswerRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopAverageScoreRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopChoiceRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopClassRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopFCMTokenRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopHomeworkRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopProjectRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopQuestionWithChoicesRepository
import jp.ac.jec.cm0138.understandme.Repository.Impl.LollipopUserDataRepository
import jp.ac.jec.cm0138.understandme.Retrofit.APIKeyInterceptor
import jp.ac.jec.cm0138.understandme.Retrofit.BaseUrlInterceptor
import jp.ac.jec.cm0138.understandme.Retrofit.Services.AnswerAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.AverageScoreAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ChoiceAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ClassAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.FCMTokenAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.HomeworkAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ProjectAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.QuestionWithChoicesAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.ResultAPIService
import jp.ac.jec.cm0138.understandme.Retrofit.Services.UserAPIService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
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
    fun provideLollipopHomeworkRepository(
        homeworkAPIService: HomeworkAPIService
    ): HomeworkRepository {
        return LollipopHomeworkRepository(homeworkAPIService = homeworkAPIService)
    }


    @Provides
    @Singleton
    fun provideLollipopResultRepository(
        resultAPIService: ResultAPIService
    ): ResultRepository {
        return LollipopResultRepository(resultAPIService = resultAPIService)
    }


    @Provides
    @Singleton
    fun provideLollipopProjectRepository(
        projectAPI: ProjectAPIService
    ): ProjectRepository {
        return LollipopProjectRepository(projectAPIService = projectAPI)
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
    fun provideLollipopQuestionWithChoicesRepository(
        questionWithChoicesAPIService: QuestionWithChoicesAPIService
    ): QuestionWithChoicesRepository {
        return LollipopQuestionWithChoicesRepository(questionWithChoicesAPIService = questionWithChoicesAPIService)
    }

    @Provides
    @Singleton
    fun provideLollipopAnswerRepository(
        answerAPIService: AnswerAPIService
    ): AnswerRepository {
        return LollipopAnswerRepository(answerAPIService = answerAPIService)
    }

    @Provides
    @Singleton
    fun provideLollipopChoiceRepository(
        choiceAPIService: ChoiceAPIService
    ): ChoiceRepository {
        return LollipopChoiceRepository(choiceAPIService = choiceAPIService)
    }


    @Provides
    @Singleton
    fun provideAverageScoreRepository(
        averageScoreAPIService: AverageScoreAPIService
    ): AverageScoreRepository {
        return LollipopAverageScoreRepository(averageScoreAPIService = averageScoreAPIService)
    }

    @Provides
    @Singleton
    fun provideFCMTokenRepository(
        fcmTokenAPIService: FCMTokenAPIService
    ): FCMTokenRepository {
        return LollipopFCMTokenRepository(fcmTokenAPIService = fcmTokenAPIService)
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
    fun provideHomeworkAPIService(
        retrofit: Retrofit
    ): HomeworkAPIService {
        return retrofit.create(HomeworkAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideProjectAPIService(
        retrofit: Retrofit
    ): ProjectAPIService {
        return retrofit.create(ProjectAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideResultAPIService(
        retrofit: Retrofit
    ): ResultAPIService {
        return retrofit.create(ResultAPIService::class.java)
    }


    @Provides
    @Singleton
    fun provideQuestionWithChoicesAPIService(
        retrofit: Retrofit
    ): QuestionWithChoicesAPIService {
        return retrofit.create(QuestionWithChoicesAPIService::class.java)
    }


    @Provides
    @Singleton
    fun provideAnswerAPIService(
        retrofit: Retrofit
    ): AnswerAPIService {
        return retrofit.create(AnswerAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideChoiceAPIService(
        retrofit: Retrofit
    ): ChoiceAPIService {
        return retrofit.create(ChoiceAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideAverageScoreAPIService(
        retrofit: Retrofit
    ): AverageScoreAPIService {
        return retrofit.create(AverageScoreAPIService::class.java)
    }


    @Provides
    @Singleton
    fun provideFCMTokenAPIService(
        retrofit: Retrofit
    ): FCMTokenAPIService {
        return retrofit.create(FCMTokenAPIService::class.java)
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
    fun provideOkHttpClient(
        baseUrlInterceptor: BaseUrlInterceptor,
        firebaseAuth: FirebaseAuth
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(baseUrlInterceptor)
            .addInterceptor(APIKeyInterceptor(firebaseAuth))
            .build()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideBaseUrlInterceptor(
        remoteConfigManager: RemoteConfigManager
    ): BaseUrlInterceptor {
        return BaseUrlInterceptor(remoteConfigManager)
    }

}