package com.codinghuseyn.exchangemaven.data.di

import com.codinghuseyn.exchangemaven.BuildConfig
import com.codinghuseyn.exchangemaven.data.remote.api.WebServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val headerInterceptor = Interceptor {
            val request = it.request()
                .newBuilder()
                .addHeader("X-RapidAPI-Key", BuildConfig.xRapidApiKey)
                .build()
            it.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWebServiceApi(retrofit: Retrofit): WebServiceApi {
        return retrofit.create(WebServiceApi::class.java)
    }
}