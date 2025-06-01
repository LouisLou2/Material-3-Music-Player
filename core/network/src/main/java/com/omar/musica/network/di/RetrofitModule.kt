package com.omar.musica.network.di

import android.content.Context
import com.omar.musica.network.service.AudioRecognitionService
import com.omar.musica.network.service.LyricsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LyricsRetrofitService

@Qualifier  
@Retention(AnnotationRetention.BINARY)
annotation class AudioRecognitionRetrofitService

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideLyricsService(
        @LyricsRetrofitService lyricsRetrofitService: Retrofit
    ): LyricsService = lyricsRetrofitService.create()

    @LyricsRetrofitService
    @Provides
    @Singleton
    fun provideLyricsRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(LyricsService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAudioRecognitionService(
        @AudioRecognitionRetrofitService audioRecognitionRetrofitService: Retrofit
    ): AudioRecognitionService = audioRecognitionRetrofitService.create()

    @AudioRecognitionRetrofitService
    @Provides
    @Singleton
    fun provideAudioRecognitionRetrofit(): Retrofit {
        // 从配置类动态获取ACRCloud API host
        // 这样支持不同账户和不同地区的API endpoint
        val apiHost = try {
            // 使用反射获取配置，避免直接依赖feature模块
            val configClass = Class.forName("com.omar.musica.audiosearch.config.AudioSearchConfig")
            val apiHostField = configClass.getDeclaredField("API_HOST")
            apiHostField.get(null) as String
        } catch (e: Exception) {
            // 如果反射失败，使用默认的美西服务器
            "https://identify-us-west-2.acrcloud.com/"
        }
        
        return Retrofit.Builder()
            .baseUrl(apiHost)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}