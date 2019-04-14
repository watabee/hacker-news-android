package com.github.watabee.hackernews

import com.github.watabee.hackernews.appinitializers.AppInitializers
import com.github.watabee.hackernews.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class HackerNewsApplication : DaggerApplication() {

    @Inject lateinit var appInitializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        appInitializers.initialize(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}