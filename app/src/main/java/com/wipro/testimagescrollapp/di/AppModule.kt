package com.wipro.testimagescrollapp.di

import android.app.Application
import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wipro.testimagescrollapp.common.Const
import com.wipro.testimagescrollapp.domain.rest.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    // region retrofit
    @Provides
    internal fun provideGson(): Gson =
        GsonBuilder().apply {
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        }.create()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okClient = OkHttpClient.Builder()
        okClient.connectTimeout(60L, TimeUnit.SECONDS)
        okClient.writeTimeout(60L, TimeUnit.SECONDS)
        okClient.readTimeout(60L, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okClient.interceptors().add(interceptor)

        okClient.interceptors().add(Interceptor { chain ->
            val response = chain.proceed(chain.request())
            response.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build()
            response
        })
        return okClient.build()
    }


    @Provides
    @Singleton
    @Named("Normal")
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiInterface(@Named("Normal") retrofit: Retrofit): RetrofitService =
        retrofit.create(RetrofitService::class.java)
    // endregion


}