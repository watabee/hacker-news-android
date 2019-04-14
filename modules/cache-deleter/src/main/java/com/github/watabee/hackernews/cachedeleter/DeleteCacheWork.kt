package com.github.watabee.hackernews.cachedeleter

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.watabee.hackernews.db.dao.StoryDao
import com.github.watabee.hackernews.workermanager.ListenableWorkerFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import timber.log.Timber
import java.util.concurrent.TimeUnit

internal class DeleteCacheWork @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val storyDao: StoryDao
) : Worker(context, params) {

    @AssistedInject.Factory
    interface Factory : ListenableWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker
    }

    override fun doWork(): Result {
        Timber.i("start doWork()")
        val time = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        try {
            storyDao.deleteStories(time)
        } catch (e: Throwable) {
            Timber.e(e)
        }
        Timber.i("end doWork()")
        return Result.success()
    }

    companion object {
        fun start() {
            WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                    "delete_cache_work",
                    ExistingPeriodicWorkPolicy.KEEP,
                    PeriodicWorkRequestBuilder<DeleteCacheWork>(1, TimeUnit.DAYS).build()
                )
        }
    }
}