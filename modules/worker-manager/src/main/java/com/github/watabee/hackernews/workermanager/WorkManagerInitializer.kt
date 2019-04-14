package com.github.watabee.hackernews.workermanager

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.github.watabee.hackernews.AppInitializer
import javax.inject.Inject

class WorkManagerInitializer @Inject internal constructor(
    private val appWorkerFactoryFactory: AppWorkerFactoryFactory
) : AppInitializer {

    override fun initialize(application: Application) {
        WorkManager.initialize(
            application.applicationContext,
            Configuration.Builder().setWorkerFactory(appWorkerFactoryFactory).build()
        )
    }
}