package com.github.watabee.hackernews.workermanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface ListenableWorkerFactory {
    fun create(context: Context, params: WorkerParameters): ListenableWorker
}