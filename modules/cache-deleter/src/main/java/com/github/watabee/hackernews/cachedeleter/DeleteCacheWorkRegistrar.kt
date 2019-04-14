package com.github.watabee.hackernews.cachedeleter

import android.app.Application
import com.github.watabee.hackernews.AppInitializer
import javax.inject.Inject

internal class DeleteCacheWorkRegistrar @Inject constructor() : AppInitializer {

    override fun initialize(application: Application) {
        DeleteCacheWork.start()
    }
}