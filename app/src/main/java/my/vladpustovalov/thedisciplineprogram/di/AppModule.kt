package my.vladpustovalov.thedisciplineprogram.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import my.vladpustovalov.thedisciplineprogram.data.local.TokenManager
import my.vladpustovalov.thedisciplineprogram.domain.notification.LocalNotificationManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideLocalNotificationManager(@ApplicationContext context: Context): LocalNotificationManager {
        return LocalNotificationManager(context)
    }
}