package com.github.watabee.hackernews.workermanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

internal class AppWorkerFactoryFactory @Inject constructor(
    private val providers: @JvmSuppressWildcards Map<Class<out ListenableWorker>, Provider<AppWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        val clazz = Class.forName(workerClassName)
        val entry =
            providers.entries.find { clazz.isAssignableFrom(it.key) } ?: return null

        return entry.value.get().create(appContext, workerParameters)
    }
}