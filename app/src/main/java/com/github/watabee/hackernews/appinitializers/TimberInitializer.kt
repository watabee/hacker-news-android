package com.github.watabee.hackernews.appinitializers

import android.app.Application
import com.github.watabee.hackernews.AppEnv
import com.github.watabee.hackernews.AppInitializer
import timber.log.Timber
import javax.inject.Inject

class TimberInitializer @Inject constructor(private val env: AppEnv) : AppInitializer {

    override fun initialize(application: Application) {
        if (env.debug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}