package com.github.watabee.hackernews.di

import android.content.Context
import android.os.Looper
import com.github.watabee.hackernews.AppEnv
import com.github.watabee.hackernews.AppRxSchedulers
import com.github.watabee.hackernews.BuildConfig
import com.github.watabee.hackernews.HackerNewsApplication
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppContext(app: HackerNewsApplication): Context = app.applicationContext

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppEnv() = AppEnv(
        debug = BuildConfig.DEBUG,
        apiBaseUrl = BuildConfig.API_BASE_URL
    )

    @JvmStatic
    @Provides
    @Singleton
    fun provideAppRxSchedulers() = AppRxSchedulers(
        io = Schedulers.io(),
        computation = Schedulers.computation(),
        main = AndroidSchedulers.from(Looper.getMainLooper(), true)
    )
}