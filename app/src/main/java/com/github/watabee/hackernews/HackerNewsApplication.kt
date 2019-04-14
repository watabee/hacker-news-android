package com.github.watabee.hackernews

import android.app.Activity
import android.app.Application
import com.github.watabee.hackernews.appinitializers.AppInitializers
import com.github.watabee.hackernews.cachedeleter.registerDeleteCacheWork
import com.github.watabee.hackernews.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class HackerNewsApplication : Application(), HasActivityInjector {

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var appInitializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.factory()
            .create(this)
            .inject(this)

        appInitializers.initialize(this)

        registerDeleteCacheWork()
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}