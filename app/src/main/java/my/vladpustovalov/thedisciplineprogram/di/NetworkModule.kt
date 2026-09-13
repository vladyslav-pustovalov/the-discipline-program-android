package my.vladpustovalov.thedisciplineprogram.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import my.vladpustovalov.thedisciplineprogram.data.local.TokenManager
import my.vladpustovalov.thedisciplineprogram.data.network.AuthInterceptor
import my.vladpustovalov.thedisciplineprogram.data.network.api.AuthService
import my.vladpustovalov.thedisciplineprogram.data.network.api.ProgramService
import my.vladpustovalov.thedisciplineprogram.data.network.api.TrainingLevelService
import my.vladpustovalov.thedisciplineprogram.data.network.api.UserPlansService
import my.vladpustovalov.thedisciplineprogram.data.network.api.UserService
import my.vladpustovalov.thedisciplineprogram.util.Constants
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

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

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
        }
        return Retrofit.Builder()
            .baseUrl(Constants.API.FULL_BASE_URL)
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
