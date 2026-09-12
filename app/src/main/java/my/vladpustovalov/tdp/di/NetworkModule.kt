package my.vladpustovalov.tdp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import my.vladpustovalov.tdp.data.local.TokenManager
import my.vladpustovalov.tdp.data.network.AuthInterceptor
import my.vladpustovalov.tdp.data.network.api.AuthService
import my.vladpustovalov.tdp.data.network.api.ProgramService
import my.vladpustovalov.tdp.data.network.api.TrainingLevelService
import my.vladpustovalov.tdp.data.network.api.UserPlansService
import my.vladpustovalov.tdp.data.network.api.UserService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideProgramService(retrofit: Retrofit): ProgramService =
        retrofit.create(ProgramService::class.java)

    @Provides
    @Singleton
    fun provideTrainingLevelService(retrofit: Retrofit): TrainingLevelService =
        retrofit.create(TrainingLevelService::class.java)

    @Provides
    @Singleton
    fun provideUserPlansService(retrofit: Retrofit): UserPlansService =
        retrofit.create(UserPlansService::class.java)

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)
}