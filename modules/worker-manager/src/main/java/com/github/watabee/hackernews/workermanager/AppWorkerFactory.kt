package com.github.watabee.hackernews.workermanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface AppWorkerFactory {
    fun create(context: Context, params: WorkerParameters): ListenableWorker
}