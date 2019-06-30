package com.github.watabee.hackernews.api

import android.content.Context
import com.github.watabee.hackernews.AppEnv
import com.github.watabee.hackernews.AppRxSchedulers
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

private const val MAX_CACHE_SIZE: Long = 10 * 1024 * 1024

@Module
object ApiModule {

    @Provides
    @JvmStatic
    internal fun provideCache(appContext: Context) = Cache(appContext.cacheDir, MAX_CACHE_SIZE)

    @Provides
    @JvmStatic
    @Singleton
    internal fun provideOkHttpClient(cache: Cache, appEnv: AppEnv): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .cache(cache)

        if (appEnv.debug) {
            builder.addInterceptor(
                HttpLoggingInterceptor(
                    object : HttpLoggingInterceptor.Logger {
                        override fun log(message: String) = Timber.tag("OkHttp").w(message)
                    }
                ).apply { level = HttpLoggingInterceptor.Level.BODY }
            )
        }

        return builder.build()
    }

    @Provides
    @JvmStatic
    @Singleton
    internal fun provideMoshi(): Moshi = Moshi.Builder()
        .build()

    @Provides
    @JvmStatic
    @Singleton
    internal fun provideRetrofit(
        okHttpClient: Lazy<OkHttpClient>,
        moshi: Moshi,
        appRxSchedulers: AppRxSchedulers,
        appEnv: AppEnv
    ): Retrofit {
        return Retrofit.Builder()
            .callFactory(object : Call.Factory {
                override fun newCall(request: Request): Call = okHttpClient.get().newCall(request)
            })
            .baseUrl(appEnv.apiBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.createWithScheduler(appRxSchedulers.io)
            )
            .build()
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideHackerNewsApi(retrofit: Retrofit): HackerNewsApi =
        retrofit.create(HackerNewsApi::class.java)
}