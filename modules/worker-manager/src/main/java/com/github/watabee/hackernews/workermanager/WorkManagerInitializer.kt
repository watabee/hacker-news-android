package com.github.watabee.hackernews.workermanager

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.android.DaggerContentProvider
import javax.inject.Inject

internal class WorkManagerInitializer : DaggerContentProvider() {

    @Inject lateinit var appWorkerFactoryFactory: AppWorkerFactoryFactory

    override fun onCreate(): Boolean {
        super.onCreate()

        WorkManager.initialize(
            context!!,
            Configuration.Builder().setWorkerFactory(appWorkerFactoryFactory).build()
        )
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}