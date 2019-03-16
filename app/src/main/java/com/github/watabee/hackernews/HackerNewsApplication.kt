package com.github.watabee.hackernews

import android.app.Application
import com.github.watabee.hackernews.di.AppComponent
import timber.log.Timber
import javax.inject.Inject

class HackerNewsApplication : Application() {

    val appComponent: AppComponent by lazy(LazyThreadSafetyMode.NONE) {
        AppComponent.create(this)
    }

    @Inject lateinit var appEnv: AppEnv

    override fun onCreate() {
        super.onCreate()

        appComponent.inject(this)

        if (appEnv.debug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}